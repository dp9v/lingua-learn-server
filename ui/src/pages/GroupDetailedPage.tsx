import { useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '@/context/AuthContext.tsx';
import { useEffect, useState } from 'react';
import { getGroupWords, WordPair } from '@/api/groups.ts';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card.tsx';

export default function GroupDetailedPage() {
    const navigate = useNavigate();
    const { id } = useParams();
    const { token } = useAuth();
    const [words, setWords] = useState<WordPair[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        if (!token) {
            navigate('/login');
            return;
        }
        if (!id) {
            setError('404: Group ID is missing');
        }

        getGroupWords(Number(id), token)
            .then(setWords)
            .catch(() => setError('Ошибка при загрузке слов'))
            .finally(() => setLoading(false));
    }, [token, id, navigate]);

    return (
        <main className="bg-muted/40 min-h-screen p-6">
            <Card className="max-w-3xl w-full">
                <CardHeader>
                    <CardTitle>Слова в группе</CardTitle>
                </CardHeader>
                <CardContent>
                    {loading && <p>Загрузка...</p>}
                    {error && <p className="text-red-500">{error}</p>}
                    {!loading && !error && words.length === 0 && (
                        <p className="text-muted-foreground">Список слов в этой группе пуст.</p>
                    )}
                    {!loading && !error && words.length > 0 && (
                        <div className="overflow-x-auto">
                            <table className="text-left border-separate border-spacing-y-2 w-full">
                                <thead>
                                    <tr>
                                        <th className="px-2 py-1">Слово (из)</th>
                                        <th className="px-2 py-1">Слово (в)</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {words.map((pair) => (
                                        <tr key={pair.id} className="bg-white rounded shadow-sm">
                                            <td className="px-2 py-1">
                                                {pair.from.text} ({pair.from.language})
                                            </td>
                                            <td className="px-2 py-1">
                                                {pair.to.text} ({pair.to.language})
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    )}
                </CardContent>
            </Card>
        </main>
    );
}
