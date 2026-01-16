import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from '@/components/ui/dialog';
import { Textarea } from '@/components/ui/textarea';
import type { WordPair, Language } from '@/api/types';

interface AddWordsFromJsonProps {
    open: boolean;
    onClose: () => void;
    onImport: (pairs: WordPair[]) => void;
    languages: Language[];
}

export default function AddWordsFromJson({ open, onClose, onImport, languages }: AddWordsFromJsonProps) {
    const [jsonInput, setJsonInput] = useState('');
    const [error, setError] = useState('');

    const handleImport = () => {
        try {
            const parsed = JSON.parse(jsonInput);

            if (!Array.isArray(parsed)) {
                throw new Error('Expected array of objects');
            }

            const validPairs: WordPair[] = parsed.filter(
                (item) => item.from?.text && item.from?.language && item.to?.text && item.to?.language
            );

            if (validPairs.length === 0) {
                throw new Error('No valid word pairs found');
            }

            onImport(validPairs);
            setJsonInput('');
            setError('');
            onClose();
        } catch (err) {
            const errorMessage =
                err instanceof SyntaxError
                    ? 'Invalid JSON format'
                    : err instanceof Error
                      ? err.message
                      : 'JSON parsing error';
            setError(errorMessage);
        }
    };

    const availableLangs = languages.map((l) => `${l.name} (${l.code})`).join(', ');

    return (
        <Dialog open={open} onOpenChange={onClose}>
            <DialogContent className="max-w-xl">
                <DialogHeader>
                    <DialogTitle>Import Words from JSON</DialogTitle>
                </DialogHeader>

                <div className="space-y-3">
                    <Textarea
                        value={jsonInput}
                        onChange={(e) => setJsonInput(e.target.value)}
                        placeholder="Paste JSON array of words here"
                        className="h-48 resize-none"
                    />

                    {error && <p className="text-sm text-red-500">{error}</p>}

                    <div className="text-xs bg-muted p-2 rounded space-y-1">
                        <p>
                            If you want to add multiple words at once, you need to provide them in the correct format. 
                            To convert your words into the required format, it's easiest to use a chatbot. 
                            Below is a prompt you can use for this purpose.
                        </p>
                        <br />
                        <pre className="whitespace-pre-wrap">
                            {`Convert my word list into a JSON array in this format:
[
  {
    "from": { "text": "cat", "language": "en" },
    "to":   { "text": "кот", "language": "ru" }
  }
]

Requirements:
1. The "text" field should contain only one word or one short phrase (no lists).
2. Use only available languages: ${availableLangs}.
3. If there's no translation for a word, indicate it.
`}
                        </pre>
                    </div>
                </div>

                <DialogFooter>
                    <Button variant="secondary" onClick={onClose}>
                        Cancel
                    </Button>
                    <Button onClick={handleImport}>Import</Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}
