import { render, screen, fireEvent } from '@testing-library/react';
import AddWordsFromJson from './add-words-from-json-modal.tsx';
import type { Language } from '@/api/types';

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
        expect(screen.getByText('Import Words from JSON')).toBeInTheDocument();
        expect(screen.getByPlaceholderText('Paste JSON array of words here')).toBeInTheDocument();
        expect(screen.getByText(/Use only available languages/)).toBeInTheDocument();
    });

    test('does not render modal when closed', () => {
        render(<AddWordsFromJson open={false} onClose={onClose} onImport={onImport} languages={mockLanguages} />);
        expect(screen.queryByText('Import Words from JSON')).not.toBeInTheDocument();
    });

    test('calls onClose when cancel button is clicked', () => {
        render(<AddWordsFromJson open={true} onClose={onClose} onImport={onImport} languages={mockLanguages} />);
        fireEvent.click(screen.getByText('Cancel'));
        expect(onClose).toHaveBeenCalledTimes(1);
    });

    test('shows error for invalid JSON', () => {
        render(<AddWordsFromJson open={true} onClose={onClose} onImport={onImport} languages={mockLanguages} />);
        const textarea = screen.getByPlaceholderText('Paste JSON array of words here');
        fireEvent.change(textarea, { target: { value: '{ invalid json' } });
        fireEvent.click(screen.getByText('Import'));
        expect(screen.getByText('Invalid JSON format')).toBeInTheDocument();
        expect(onImport).not.toHaveBeenCalled();
    });

    test('calls onImport with valid JSON', () => {
        const validJson = JSON.stringify([
            {
                from: { text: 'cat', language: 'en' },
                to: { text: 'kot', language: 'ru' },
            },
        ]);
        render(<AddWordsFromJson open={true} onClose={onClose} onImport={onImport} languages={mockLanguages} />);
        const textarea = screen.getByPlaceholderText('Paste JSON array of words here');
        fireEvent.change(textarea, { target: { value: validJson } });
        fireEvent.click(screen.getByText('Import'));
        expect(onImport).toHaveBeenCalledWith([
            {
                from: { text: 'cat', language: 'en' },
                to: { text: 'kot', language: 'ru' },
            },
        ]);
        expect(onClose).toHaveBeenCalled();
    });

    test('ignores invalid word pairs in JSON', () => {
        const mixedJson = JSON.stringify([
            {
                from: { text: 'cat', language: 'en' },
                to: { text: 'kot', language: 'ru' },
            },
            {
                from: { text: '', language: 'en' },
                to: { text: 'sobaka', language: 'ru' },
            },
        ]);
        render(<AddWordsFromJson open={true} onClose={onClose} onImport={onImport} languages={mockLanguages} />);
        const textarea = screen.getByPlaceholderText('Paste JSON array of words here');
        fireEvent.change(textarea, { target: { value: mixedJson } });
        fireEvent.click(screen.getByText('Import'));
        expect(onImport).toHaveBeenCalledWith([
            {
                from: { text: 'cat', language: 'en' },
                to: { text: 'kot', language: 'ru' },
            },
        ]);
    });
});
