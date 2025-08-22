import { render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import GroupDetailedPage from '@/pages/GroupDetailedPage';
import { useAuth } from '@/context/AuthContext';
import * as api from '@/api/groups';
import { getAvailableLanguages, Language } from '@/api/groups';

const LANGS: Language[] = [
    { code: 'en', name: 'English' },
    { code: 'ru', name: 'Русский' },
];
// Мокаем useAuth
jest.mock('@/context/AuthContext', () => ({
    useAuth: jest.fn(),
}));

// Мокаем API
jest.mock('@/api/groups', () => ({
    getGroupWords: jest.fn(),
    getAvailableLanguages: jest.fn(),
}));

const mockUseAuth = useAuth as jest.Mock;
const mockGetGroupWords = api.getGroupWords as jest.Mock;
const mockedGetAvailableLanguages = getAvailableLanguages as jest.MockedFunction<typeof getAvailableLanguages>;

function renderWithRouter(id: string) {
    return render(
        <MemoryRouter initialEntries={[`/groups/${id}`]}>
            <Routes>
                <Route path="/groups/:id" element={<GroupDetailedPage />} />
                <Route path="/login" element={<div>Login Page</div>} />
            </Routes>
        </MemoryRouter>
    );
}

describe('GroupDetailedPage', () => {
    beforeEach(() => {
        mockUseAuth.mockReturnValue({ token: 'mock-token' });
        mockedGetAvailableLanguages.mockResolvedValue(LANGS);
    });

    it('shows loading skeletons initially', () => {
        mockGetGroupWords.mockReturnValue(new Promise(() => {}));

        renderWithRouter('1');

        expect(screen.getAllByRole('progressbar').length).toBeGreaterThan(0);
    });

    it('renders group data correctly', async () => {
        mockGetGroupWords.mockResolvedValue({
            group: {
                id: 1,
                name: 'My Group',
                description: 'Описание группы',
                isPublic: true,
                wordsCount: 2,
            },
            words: [
                {
                    id: 101,
                    from: { id: 1, text: 'Hello', language: 'EN' },
                    to: { id: 2, text: 'Привет', language: 'RU' },
                },
                {
                    id: 102,
                    from: { id: 3, text: 'Bye', language: 'EN' },
                    to: { id: 4, text: 'Пока', language: 'RU' },
                },
            ],
        });

        renderWithRouter('1');

        await waitFor(() => {
            expect(screen.getByText('My Group')).toBeInTheDocument();
            expect(screen.getByText('Описание группы')).toBeInTheDocument();
            expect(screen.getByText('Всего слов: 2')).toBeInTheDocument();
            expect(screen.getByText(/Hello.*EN/)).toBeInTheDocument();
            expect(screen.getByText(/Привет.*RU/)).toBeInTheDocument();
        });
    });

    it('shows empty state if no words', async () => {
        mockGetGroupWords.mockResolvedValue({
            group: {
                id: 1,
                name: 'Empty Group',
                description: 'Нет слов',
                isPublic: true,
                wordsCount: 0,
            },
            words: [],
        });

        renderWithRouter('1');

        expect(await screen.findByText('Список слов в этой группе пуст.')).toBeInTheDocument();
    });

    it('shows error on API failure', async () => {
        mockGetGroupWords.mockRejectedValue(new Error('Failed to load'));

        renderWithRouter('1');

        expect(await screen.findByText('Ошибка при загрузке слов')).toBeInTheDocument();
    });

    it('shows error if ID is missing', async () => {
        render(
            <MemoryRouter initialEntries={['/groups/']}>
                <Routes>
                    <Route path="/groups/:id?" element={<GroupDetailedPage />} />
                </Routes>
            </MemoryRouter>
        );

        expect(await screen.findByText('404: Group ID is missing')).toBeInTheDocument();
    });
});
