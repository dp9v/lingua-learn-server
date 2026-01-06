import { useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '@/context/AuthContext.tsx';
import { useCallback, useEffect, useState } from 'react';
import { getGroupWords, GroupWithWords } from '@/api/groups.ts';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card.tsx';
import { GroupInfo, GroupInfoSkeleton } from '@/components/gruop-info.tsx';
import { Skeleton } from '@/components/ui/skeleton.tsx';
import { WordsTable, WordsTableSkeleton } from '@/components/words-table';
import AddWordsForm from '@/components/add-words-form';
import { Button } from '@/components/ui/button';
import { DumbbellIcon } from 'lucide-react';
import { useTrainingModal } from '@/hooks/use-training-modal';
import { TrainingModal } from '@/components/training-modal/training-modal.tsx';

export default function GroupDetailedPage() {
    const navigate = useNavigate();
    const { id } = useParams();
    const { token } = useAuth();
    const { isOpen, openTraining, closeTraining } = useTrainingModal();

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
                    <div className="flex justify-between items-start">
                        <CardTitle className="text-3xl font-bold">
                            {loading ? <Skeleton className="h-8 w-1/2" /> : (group?.name ?? 'Words in Group')}
                        </CardTitle>
                        {!loading && !error && words.length > 0 && (
                            <Button onClick={openTraining} variant="default" className="flex items-center gap-2">
                                <DumbbellIcon className="w-4 h-4" />
                                <span className="hidden sm:inline">Training</span>
                            </Button>
                        )}
                    </div>
                </CardHeader>
                <CardContent>
                    {error && <p className="text-red-500">{error}</p>}

                    {loading && <GroupInfoSkeleton />}
                    {loading && <WordsTableSkeleton />}

                    {!loading && !error && group && <GroupInfo group={group} wordsCount={words.length} />}

                    {!loading && !error && words.length === 0 && (
                        <p className="text-muted-foreground">The word list in this group is empty.</p>
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
            <TrainingModal open={isOpen} onClose={closeTraining} initialGroupId={group?.id} />
        </main>
    );
}
