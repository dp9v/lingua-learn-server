import { render, screen, fireEvent } from '@testing-library/react';
import AddWordsFromJson from './add-words-from-json-modal.tsx';
import type { Language } from '@/api/groups';

const mockLanguages: Language[] = [
    { code: 'en', name: 'English' },
    { code: 'ru', name: 'Russian' },
];

describe('AddWordsFromJson', () => {
    const onClose = jest.fn();
    const onImport = jest.fn();

    beforeEach(() => {
        jest.clearAllMocks();
    });

    test('renders modal when open', () => {
        render(<AddWordsFromJson open={true} onClose={onClose} onImport={onImport} languages={mockLanguages} />);
        expect(screen.getByText('Импорт слов из JSON')).toBeInTheDocument();
        expect(screen.getByPlaceholderText('Вставьте сюда JSON-массив слов')).toBeInTheDocument();
        expect(screen.getByText(/Используй только доступные языки/)).toBeInTheDocument();
    });

    test('does not render modal when closed', () => {
        render(<AddWordsFromJson open={false} onClose={onClose} onImport={onImport} languages={mockLanguages} />);
        expect(screen.queryByText('Импорт слов из JSON')).not.toBeInTheDocument();
    });

    test('calls onClose when cancel button is clicked', () => {
        render(<AddWordsFromJson open={true} onClose={onClose} onImport={onImport} languages={mockLanguages} />);
        fireEvent.click(screen.getByText('Отмена'));
        expect(onClose).toHaveBeenCalledTimes(1);
    });

    test('shows error for invalid JSON', () => {
        render(<AddWordsFromJson open={true} onClose={onClose} onImport={onImport} languages={mockLanguages} />);
        const textarea = screen.getByPlaceholderText('Вставьте сюда JSON-массив слов');
        fireEvent.change(textarea, { target: { value: '{ invalid json' } });
        fireEvent.click(screen.getByText('Импортировать'));
        expect(screen.getByText('Неверный формат JSON')).toBeInTheDocument();
        expect(onImport).not.toHaveBeenCalled();
    });

    test('calls onImport with valid JSON', () => {
        const validJson = JSON.stringify([
            {
                from: { text: 'cat', language: 'en' },
                to: { text: 'кот', language: 'ru' },
            },
        ]);
        render(<AddWordsFromJson open={true} onClose={onClose} onImport={onImport} languages={mockLanguages} />);
        const textarea = screen.getByPlaceholderText('Вставьте сюда JSON-массив слов');
        fireEvent.change(textarea, { target: { value: validJson } });
        fireEvent.click(screen.getByText('Импортировать'));
        expect(onImport).toHaveBeenCalledWith([
            {
                from: { text: 'cat', language: 'en' },
                to: { text: 'кот', language: 'ru' },
            },
        ]);
        expect(onClose).toHaveBeenCalled();
    });

    test('ignores invalid word pairs in JSON', () => {
        const mixedJson = JSON.stringify([
            {
                from: { text: 'cat', language: 'en' },
                to: { text: 'кот', language: 'ru' },
            },
            {
                from: { text: '', language: 'en' },
                to: { text: 'собака', language: 'ru' },
            },
        ]);
        render(<AddWordsFromJson open={true} onClose={onClose} onImport={onImport} languages={mockLanguages} />);
        const textarea = screen.getByPlaceholderText('Вставьте сюда JSON-массив слов');
        fireEvent.change(textarea, { target: { value: mixedJson } });
        fireEvent.click(screen.getByText('Импортировать'));
        expect(onImport).toHaveBeenCalledWith([
            {
                from: { text: 'cat', language: 'en' },
                to: { text: 'кот', language: 'ru' },
            },
        ]);
    });
});
