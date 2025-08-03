import { useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '@/context/AuthContext.tsx';
import { useCallback, useEffect, useState } from 'react';
import { getGroupWords, GroupWithWords } from '@/api/groups.ts';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card.tsx';
import { GroupInfo, GroupInfoSkeleton } from '@/components/gruop-info.tsx';
import { Skeleton } from '@/components/ui/skeleton.tsx';
import { WordsTable, WordsTableSkeleton } from '@/components/words-table';
import AddWordsForm from '@/components/add-words-form';

export default function GroupDetailedPage() {
    const navigate = useNavigate();
    const { id } = useParams();
    const { token } = useAuth();

    const [groupData, setGroupData] = useState<GroupWithWords>();
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [saving, setSaving] = useState(false);

    const fetchGroupWords = useCallback(() => {
        if (!token || !id) return;
        setLoading(true);
        getGroupWords(Number(id), token)
            .then(setGroupData)
            .catch(() => setError('Ошибка при загрузке слов'))
            .finally(() => setLoading(false));
    }, [id, token]);

    useEffect(() => {
        if (!token) {
            navigate('/login');
            return;
        }
        if (!id) {
            setError('404: Group ID is missing');
            setLoading(false);
            return;
        }

        fetchGroupWords();
    }, [token, id, navigate, fetchGroupWords]);

    const words = groupData?.words ?? [];
    const group = groupData?.group;

    return (
        <main className="flex flex-col bg-muted/40 min-h-screen p-6">
            <Card className="w-full max-w-3xl">
                <CardHeader>
                    <CardTitle className="text-3xl font-bold">
                        {loading ? <Skeleton className="h-8 w-1/2" /> : (group?.name ?? 'Слова в группе')}
                    </CardTitle>
                </CardHeader>
                <CardContent>
                    {error && <p className="text-red-500">{error}</p>}

                    {loading && <GroupInfoSkeleton />}
                    {loading && <WordsTableSkeleton />}

                    {!loading && !error && group && <GroupInfo group={group} wordsCount={words.length} />}

                    {!loading && !error && words.length === 0 && (
                        <p className="text-muted-foreground">Список слов в этой группе пуст.</p>
                    )}

                    {!loading && !error && words.length > 0 && <WordsTable words={words} />}

                    {!loading && !error && group && (
                        <div className="mt-8">
                            {saving ? (
                                <Skeleton className="h-48 w-full rounded-lg" />
                            ) : (
                                <AddWordsForm
                                    groupId={group.id}
                                    token={token ?? ''}
                                    onWordsAdded={() => {
                                        setSaving(true);
                                        fetchGroupWords();
                                        setTimeout(() => {
                                            setSaving(false);
                                        }, 500);
                                    }}
                                />
                            )}
                        </div>
                    )}
                </CardContent>
            </Card>
        </main>
    );
}
