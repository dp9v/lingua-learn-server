import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import { HomePage } from '@/pages/HomePage';
import * as authApi from '@/api/auth';
import * as groupsApi from '@/api/groups';
import { useAuth } from '@/context/AuthContext';
import type { Group } from '@/api/types';

// Mock useAuth
jest.mock('@/context/AuthContext', () => ({
    useAuth: jest.fn(),
}));

// Mock useNavigate
const mockedNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockedNavigate,
}));

describe('HomePage', () => {
    const mockToken = 'mock-token';

    beforeEach(() => {
        jest.clearAllMocks();
        (useAuth as jest.Mock).mockReturnValue({ token: mockToken });
    });

    test('shows loading skeletons while loading', async () => {
        jest.spyOn(authApi, 'me').mockResolvedValueOnce({ name: 'Test User' });
        jest.spyOn(groupsApi, 'getUserGroups').mockResolvedValueOnce([]);

        render(<HomePage />, { wrapper: MemoryRouter });

        expect(screen.getAllByRole('progressbar').length).toBeGreaterThan(0);

        await waitFor(() => {
            expect(screen.queryByRole('progressbar')).not.toBeInTheDocument();
        });
    });

    test('renders greeting and groups on success', async () => {
        jest.spyOn(authApi, 'me').mockResolvedValueOnce({ name: 'Anna' });
        jest.spyOn(groupsApi, 'getUserGroups').mockResolvedValueOnce([
            {
                id: 1,
                name: 'Group A',
                description: 'Description A',
                isPublic: true,
                wordsCount: 10,
            },
        ]);

        render(<HomePage />, { wrapper: MemoryRouter });

        await waitFor(() => {
            expect(screen.getByText(/Hello, Anna/i)).toBeInTheDocument();
            expect(screen.getByText('Group A')).toBeInTheDocument();
            expect(screen.getByText('Description A')).toBeInTheDocument();
            expect(screen.getByText(/10 words/i)).toBeInTheDocument();
        });
    });

    test('shows message if no groups', async () => {
        jest.spyOn(authApi, 'me').mockResolvedValueOnce({ name: 'Anna' });
        jest.spyOn(groupsApi, 'getUserGroups').mockResolvedValueOnce([]);

        render(<HomePage />, { wrapper: MemoryRouter });

        await waitFor(() => {
            expect(screen.getByText(/you don't have any groups yet/i)).toBeInTheDocument();
        });
    });

    test('shows error message if fetch fails', async () => {
        jest.spyOn(authApi, 'me').mockRejectedValueOnce(new Error('Error'));
        jest.spyOn(groupsApi, 'getUserGroups').mockResolvedValueOnce([]);

        render(<HomePage />, { wrapper: MemoryRouter });

        await waitFor(() => {
            expect(screen.getByText(/Error loading data:/i)).toBeInTheDocument();
        });
    });

    test('allows creating a new group', async () => {
        const user = userEvent.setup();

        jest.spyOn(authApi, 'me').mockResolvedValueOnce({ name: 'Anna' });

        const initialGroups: Group[] = [];
        const updatedGroups = [
            {
                id: 1,
                name: 'New Group',
                description: 'New group description',
                isPublic: true,
                wordsCount: 0,
            },
        ];

        const getGroupsMock = jest
            .spyOn(groupsApi, 'getUserGroups')
            .mockResolvedValueOnce(initialGroups) // first load
            .mockResolvedValueOnce(updatedGroups); // after creation

        const createGroupMock = jest.spyOn(groupsApi, 'createGroup').mockResolvedValueOnce(1);

        render(<HomePage />, { wrapper: MemoryRouter });

        await waitFor(() => {
            expect(screen.getByText(/Hello, Anna/i)).toBeInTheDocument();
        });

        await user.click(screen.getByRole('button', { name: /Add Group/i }));
        expect(screen.getByText(/create new group/i)).toBeInTheDocument();

        const nameInput = screen.getByLabelText(/name/i);
        const descriptionInput = screen.getByLabelText(/description/i);

        await user.type(nameInput, 'New Group');
        await user.type(descriptionInput, 'New group description');

        await user.click(screen.getByRole('button', { name: /create/i }));

        await waitFor(() => {
            expect(createGroupMock).toHaveBeenCalledWith('New Group', 'New group description', 'mock-token');
        });

        await waitFor(() => {
            expect(getGroupsMock).toHaveBeenCalledTimes(2);
            expect(screen.getByText('New Group')).toBeInTheDocument();
        });

        expect(screen.queryByText(/create new group/i)).not.toBeInTheDocument();
    });
});
