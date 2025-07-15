import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import { HomePage } from '@/pages/HomePage';
import * as authApi from '@/api/auth';
import * as groupsApi from '@/api/groups';
import { useAuth } from '@/context/AuthContext';
import { Group } from '@/api/groups';

// Мокаем useAuth
jest.mock('@/context/AuthContext', () => ({
    useAuth: jest.fn(),
}));

// Мокаем useNavigate
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
        jest.spyOn(authApi, 'me').mockResolvedValueOnce({ name: 'Анна' });
        jest.spyOn(groupsApi, 'getUserGroups').mockResolvedValueOnce([
            {
                id: 1,
                name: 'Группа A',
                description: 'Описание A',
                isPublic: true,
                wordsCount: 10,
            },
        ]);

        render(<HomePage />, { wrapper: MemoryRouter });

        await waitFor(() => {
            expect(screen.getByText(/Привет, Анна/i)).toBeInTheDocument();
            expect(screen.getByText('Группа A')).toBeInTheDocument();
            expect(screen.getByText('Описание A')).toBeInTheDocument();
            expect(screen.getByText(/10 слов/i)).toBeInTheDocument();
        });
    });

    test('shows message if no groups', async () => {
        jest.spyOn(authApi, 'me').mockResolvedValueOnce({ name: 'Анна' });
        jest.spyOn(groupsApi, 'getUserGroups').mockResolvedValueOnce([]);

        render(<HomePage />, { wrapper: MemoryRouter });

        await waitFor(() => {
            expect(screen.getByText(/у вас пока нет групп/i)).toBeInTheDocument();
        });
    });

    test('shows error message if fetch fails', async () => {
        jest.spyOn(authApi, 'me').mockRejectedValueOnce(new Error('Ошибка'));
        jest.spyOn(groupsApi, 'getUserGroups').mockResolvedValueOnce([]);

        render(<HomePage />, { wrapper: MemoryRouter });

        await waitFor(() => {
            expect(screen.getByText(/ошиба при загрузке данных/i)).toBeInTheDocument();
        });
    });

    test('allows creating a new group', async () => {
        const user = userEvent.setup();

        jest.spyOn(authApi, 'me').mockResolvedValueOnce({ name: 'Анна' });

        const initialGroups: Group[] = [];
        const updatedGroups = [
            {
                id: 1,
                name: 'Новая группа',
                description: 'Описание новой группы',
                isPublic: true,
                wordsCount: 0,
            },
        ];

        const getGroupsMock = jest
            .spyOn(groupsApi, 'getUserGroups')
            .mockResolvedValueOnce(initialGroups) // первая загрузка
            .mockResolvedValueOnce(updatedGroups); // после создания

        const createGroupMock = jest.spyOn(groupsApi, 'createGroup').mockResolvedValueOnce(1);

        render(<HomePage />, { wrapper: MemoryRouter });

        await waitFor(() => {
            expect(screen.getByText(/Привет, Анна/i)).toBeInTheDocument();
        });

        await user.click(screen.getByRole('button', { name: /добавить группу/i }));
        expect(screen.getByText(/создать новую группу/i)).toBeInTheDocument();

        const nameInput = screen.getByLabelText(/название/i);
        const descriptionInput = screen.getByLabelText(/описание/i);

        await user.type(nameInput, 'Новая группа');
        await user.type(descriptionInput, 'Описание новой группы');

        await user.click(screen.getByRole('button', { name: /создать/i }));

        await waitFor(() => {
            expect(createGroupMock).toHaveBeenCalledWith('Новая группа', 'Описание новой группы', 'mock-token');
        });

        await waitFor(() => {
            expect(getGroupsMock).toHaveBeenCalledTimes(2);
            expect(screen.getByText('Новая группа')).toBeInTheDocument();
        });

        expect(screen.queryByText(/создать новую группу/i)).not.toBeInTheDocument();
    });
});
