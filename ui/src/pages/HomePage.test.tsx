import { render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { HomePage } from '@/pages/HomePage';
import * as authApi from '@/api/auth';
import * as groupsApi from '@/api/groups';
import { useAuth } from '@/context/AuthContext';

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
        // Задержим ответ промисов
        jest.spyOn(authApi, 'me').mockResolvedValueOnce({ name: 'Test User' });
        jest.spyOn(groupsApi, 'getUserGroups').mockResolvedValueOnce([]);

        render(<HomePage />, { wrapper: MemoryRouter });

        expect(screen.getAllByRole('progressbar').length).toBeGreaterThan(0);

        // Подождем снятия загрузки
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
});
