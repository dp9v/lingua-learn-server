import { useEffect, useState } from 'react';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Card, CardContent } from '@/components/ui/card';
import { Trash2 } from 'lucide-react';
import type { Language, WordPair } from '@/api/groups';
import { addWords, getAvailableLanguages } from '@/api/groups';

interface AddWordsFormProps {
    groupId: number;
    token: string;
    onWordsAdded?: () => void;
}

export default function AddWordsForm({ groupId, token, onWordsAdded }: AddWordsFormProps) {
    const [languages, setLanguages] = useState<Language[]>([]);
    const [newWords, setNewWords] = useState<WordPair[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        getAvailableLanguages(token)
            .then((langs) => {
                setLanguages(langs);
            })
            .catch(() => setError('Не удалось загрузить список языков'));
    }, [token]);

    const handleAddRow = () => {
        const lastPair = newWords[newWords.length - 1];
        setNewWords([
            ...newWords,
            {
                from: { text: '', language: lastPair?.from.language ?? languages[0]?.code ?? '' },
                to: { text: '', language: lastPair?.to.language ?? languages[1]?.code ?? '' },
            },
        ]);
    };

    const handleRemoveRow = (index: number) => {
        if (newWords.length === 1) {
            setNewWords([]);
        } else {
            setNewWords(newWords.filter((_, i) => i !== index));
        }
    };

    const handleChange = (index: number, field: 'from' | 'to', subfield: 'text' | 'language', value: string) => {
        const copy = [...newWords];
        copy[index][field][subfield] = value;
        setNewWords(copy);
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const validPairs = newWords.filter((w) => w.from.text.trim() && w.to.text.trim());
        if (validPairs.length === 0) return;

        setLoading(true);
        setError('');

        try {
            await addWords(groupId, token, validPairs);
            onWordsAdded?.();
            setNewWords([]);
        } catch (err) {
            console.error(err);
            setError('Не удалось сохранить слова. Попробуйте ещё раз.');
        } finally {
            setLoading(false);
        }
    };

    if (newWords.length === 0) {
        return (
            <Card className="mt-6 w-full max-w-3xl">
                <CardContent className="flex justify-center">
                    <Button
                        type="button"
                        variant="secondary"
                        onClick={() =>
                            setNewWords([
                                {
                                    from: { text: '', language: languages[0]?.code ?? '' },
                                    to: { text: '', language: languages[1]?.code ?? '' },
                                },
                            ])
                        }
                    >
                        + Добавить слова
                    </Button>
                </CardContent>
            </Card>
        );
    }

    return (
        <Card className="mt-6 w-full max-w-3xl">
            <CardContent>
                <form onSubmit={handleSubmit} className="space-y-4">
                    {error && <p className="text-sm text-red-500">{error}</p>}

                    <WordPairsList
                        pairs={newWords}
                        languages={languages}
                        onChange={handleChange}
                        onRemove={handleRemoveRow}
                    />

                    <div className="flex justify-between">
                        <Button type="button" variant="secondary" onClick={handleAddRow}>
                            + Добавить строку
                        </Button>
                        <Button type="submit" disabled={loading || languages.length === 0}>
                            {loading ? 'Сохраняем...' : 'Сохранить'}
                        </Button>
                    </div>
                </form>
            </CardContent>
        </Card>
    );
}

function WordPairsList({
    pairs,
    languages,
    onChange,
    onRemove,
}: {
    pairs: WordPair[];
    languages: Language[];
    onChange: (index: number, field: 'from' | 'to', subfield: 'text' | 'language', value: string) => void;
    onRemove: (index: number) => void;
}) {
    return (
        <div className="space-y-3">
            {pairs.map((pair, index) => (
                <div key={index} className="flex flex-col md:flex-row md:items-center gap-2 bg-muted/30 p-3 rounded-lg">
                    {/* FROM */}
                    <div className="flex flex-col md:flex-row gap-2 flex-1 min-w-[220px]">
                        <Input
                            value={pair.from.text}
                            onChange={(e) => onChange(index, 'from', 'text', e.target.value)}
                            placeholder="Слово (из)"
                            className="flex-1 min-w-0"
                        />
                        <Select
                            value={pair.from.language}
                            onValueChange={(v) => onChange(index, 'from', 'language', v)}
                        >
                            <SelectTrigger className="min-w-[110px]">
                                <SelectValue placeholder="Язык" />
                            </SelectTrigger>
                            <SelectContent>
                                {languages.map((lang) => (
                                    <SelectItem key={lang.code} value={lang.code}>
                                        {lang.fullName} ({lang.code})
                                    </SelectItem>
                                ))}
                            </SelectContent>
                        </Select>
                    </div>

                    {/* TO */}
                    <div className="flex flex-col md:flex-row gap-2 flex-1 min-w-[220px]">
                        <Input
                            value={pair.to.text}
                            onChange={(e) => onChange(index, 'to', 'text', e.target.value)}
                            placeholder="Слово (в)"
                            className="flex-1 min-w-0"
                        />
                        <Select value={pair.to.language} onValueChange={(v) => onChange(index, 'to', 'language', v)}>
                            <SelectTrigger className="min-w-[110px]">
                                <SelectValue placeholder="Язык" />
                            </SelectTrigger>
                            <SelectContent>
                                {languages.map((lang) => (
                                    <SelectItem key={lang.code} value={lang.code}>
                                        {lang.fullName} ({lang.code})
                                    </SelectItem>
                                ))}
                            </SelectContent>
                        </Select>
                    </div>

                    {/* DELETE BUTTON */}
                    <Button
                        type="button"
                        variant="ghost"
                        size="icon"
                        className="text-destructive hover:bg-destructive/10 shrink-0 self-start md:self-center"
                        onClick={() => onRemove(index)}
                        aria-label="Удалить строку"
                    >
                        <Trash2 size={18} />
                    </Button>
                </div>
            ))}
        </div>
    );
}
