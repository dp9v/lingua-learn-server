import { render, screen, waitFor, fireEvent, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { addWords, getAvailableLanguages } from '@/api/groups';
import type { Language } from '@/api/types';
import AddWordsForm from '@/components/add-words-form.tsx';

jest.mock('@/api/groups', () => ({
    addWords: jest.fn(),
    getAvailableLanguages: jest.fn(),
}));

const mockedAddWords = addWords as jest.MockedFunction<typeof addWords>;
const mockedGetAvailableLanguages = getAvailableLanguages as jest.MockedFunction<typeof getAvailableLanguages>;

const LANGS: Language[] = [
    { code: 'en', name: 'English' },
    { code: 'ru', name: 'Русский' },
];

describe('AddWordsForm', () => {
    beforeEach(() => {
        jest.clearAllMocks();
        mockedGetAvailableLanguages.mockResolvedValue(LANGS as Language[]);
        mockedAddWords.mockResolvedValue(undefined);
    });

    it('shows "+ Добавить слова" when rendered for the first time', async () => {
        render(<AddWordsForm groupId={123} token="TEST_TOKEN" />);

        await waitFor(() => expect(mockedGetAvailableLanguages).toHaveBeenCalledWith('TEST_TOKEN'));

        expect(screen.getByRole('button', { name: '+ Добавить слова' })).toBeInTheDocument();
    });

    it('adds a row, inputs a word pair, submits to server, clears the form and calls the callback', async () => {
        const onWordsAdded = jest.fn();
        const user = userEvent.setup();

        const { container } = render(<AddWordsForm groupId={777} token="TEST_TOKEN" onWordsAdded={onWordsAdded} />);

        // Wait for languages to load so default codes are available
        await waitFor(() => expect(mockedGetAvailableLanguages).toHaveBeenCalled());

        // Open the add-words form
        await user.click(screen.getByRole('button', { name: '+ Добавить слова' }));

        // The form should now contain inputs for the word pair
        const form = container.querySelector('form') as HTMLFormElement;
        expect(form).toBeInTheDocument();

        const inputs = within(form).getAllByRole('textbox');
        expect(inputs.length).toBeGreaterThanOrEqual(2);

        await user.type(inputs[0], 'hello');
        await user.type(inputs[1], 'привет');

        // Submit the form
        fireEvent.submit(form);

        await waitFor(() => {
            expect(mockedAddWords).toHaveBeenCalledTimes(1);
        });

        // Check that arguments are correct (texts exactly, languages as is)
        const [groupIdArg, tokenArg, pairsArg] = mockedAddWords.mock.calls[0];
        expect(groupIdArg).toBe(777);
        expect(tokenArg).toBe('TEST_TOKEN');
        expect(Array.isArray(pairsArg)).toBe(true);
        expect(pairsArg).toEqual(
            expect.arrayContaining([
                expect.objectContaining({
                    from: expect.objectContaining({ text: 'hello' }),
                    to: expect.objectContaining({ text: 'привет' }),
                }),
            ])
        );

        // Callback called
        expect(onWordsAdded).toHaveBeenCalledTimes(1);

        // Form cleared — "+ Добавить слова" button visible again
        expect(await screen.findByRole('button', { name: '+ Добавить слова' })).toBeInTheDocument();
    });

    it('does not submit empty rows (does not call anything)', async () => {
        const { container } = render(<AddWordsForm groupId={1} token="TEST_TOKEN" />);

        await waitFor(() => expect(mockedGetAvailableLanguages).toHaveBeenCalled());

        // Open the add-words form
        await userEvent.click(screen.getByRole('button', { name: '+ Добавить слова' }));

        const form = container.querySelector('form') as HTMLFormElement;
        fireEvent.submit(form);

        await waitFor(() => {
            expect(mockedAddWords).not.toHaveBeenCalled();
        });
    });

    it('shows an error message when saving fails', async () => {
        mockedAddWords.mockRejectedValueOnce(new Error('fail'));
        const user = userEvent.setup();

        const { container } = render(<AddWordsForm groupId={5} token="TEST_TOKEN" />);

        await waitFor(() => expect(mockedGetAvailableLanguages).toHaveBeenCalled());

        // Open form and enter data
        await user.click(screen.getByRole('button', { name: '+ Добавить слова' }));

        const form = container.querySelector('form') as HTMLFormElement;
        const inputs = within(form).getAllByRole('textbox');
        expect(inputs.length).toBeGreaterThanOrEqual(2);

        await user.type(inputs[0], 'cat');
        await user.type(inputs[1], 'кот');

        fireEvent.submit(form);

        // Expect error message to be shown
        expect(await screen.findByText('Не удалось сохранить слова. Попробуйте ещё раз.')).toBeInTheDocument();
    });

    it('shows an error message when loading languages fails (after opening the form)', async () => {
        mockedGetAvailableLanguages.mockRejectedValueOnce(new Error('network'));

        render(<AddWordsForm groupId={1} token="TEST_TOKEN" />);

        await waitFor(() => expect(mockedGetAvailableLanguages).toHaveBeenCalled());

        // Error will appear in the form markup when it becomes visible
        await userEvent.click(screen.getByRole('button', { name: '+ Добавить слова' }));

        expect(await screen.findByText('Не удалось загрузить список языков')).toBeInTheDocument();
    });
});
