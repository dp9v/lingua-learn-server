import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from '@/components/ui/dialog';
import { Textarea } from '@/components/ui/textarea';
import type { WordPair, Language } from '@/api/groups';

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
                throw new Error('Ожидается массив объектов');
            }

            const validPairs: WordPair[] = parsed.filter(
                (item) => item.from?.text && item.from?.language && item.to?.text && item.to?.language
            );

            if (validPairs.length === 0) {
                throw new Error('Не найдено корректных пар слов');
            }

            onImport(validPairs);
            setJsonInput('');
            setError('');
            onClose();
        } catch (err) {
            const errorMessage =
                err instanceof Error
                    ? err.message
                    : err instanceof SyntaxError
                      ? 'Неверный формат JSON'
                      : 'Ошибка при парсинге JSON';
            setError(errorMessage);
        }
    };

    // 👇 получаем список языков из props
    const availableLangs = languages.map((l) => `${l.name} (${l.code})`).join(', ');

    return (
        <Dialog open={open} onOpenChange={onClose}>
            <DialogContent className="max-w-xl">
                <DialogHeader>
                    <DialogTitle>Импорт слов из JSON</DialogTitle>
                </DialogHeader>

                <div className="space-y-3">
                    <Textarea
                        value={jsonInput}
                        onChange={(e) => setJsonInput(e.target.value)}
                        placeholder="Вставьте сюда JSON-массив слов"
                        className="h-48 resize-none"
                    />

                    {error && <p className="text-sm text-red-500">{error}</p>}

                    {/* 👇 блок с подсказкой теперь под textarea */}
                    <div className="text-xs bg-muted p-2 rounded space-y-1">
                        <p>
                            ⚠️ Приложение не интегрировано с AI. Вы можете использовать этот промпт для преобразования
                            своих слов в нужный формат:
                        </p>
                        <pre className="whitespace-pre-wrap">
                            {`Преобразуй мой список слов в JSON-массив такого вида:
[
  {
    "from": { "text": "cat", "language": "en" },
    "to":   { "text": "кот", "language": "ru" }
  }
]

Условия:
1. Поле "text" должно содержать только одно слово или одну короткую фразу (без перечислений).
2. Используй только доступные языки: ${availableLangs}.
`}
                        </pre>
                    </div>
                </div>

                <DialogFooter>
                    <Button variant="secondary" onClick={onClose}>
                        Отмена
                    </Button>
                    <Button onClick={handleImport}>Импортировать</Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}
