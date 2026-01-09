import { Group } from '@/api/types.ts';
import { useNavigate } from 'react-router-dom';
import { Card, CardContent, CardHeader, CardTitle } from './ui/card';
import { Skeleton } from './ui/skeleton';

interface GroupListProps {
    groups: Group[];
}

export function GroupList({ groups }: GroupListProps) {
    const navigate = useNavigate();

    if (groups.length === 0) {
        return (
            <div className="text-center mt-6">
                <p className="text-muted-foreground text-lg">
                    У вас пока нет групп. Вы можете{' '}
                    <a href="/groups" className="text-blue-500 underline hover:text-blue-700">
                        просмотреть список публичных групп
                    </a>
                    или добавить новую группу вручную.
                </p>
            </div>
        );
    }

    return (
        <div>
            <div className="welcome-message text-xl font-semibold mb-6">
                <p className="text-muted-foreground mt-2">Ниже представлены группы, которые вы добавили к себе.</p>
            </div>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                {groups.map((group) => (
                    <Card
                        key={group.id}
                        onClick={() => navigate(`/groups/${group.id}`)}
                        className="cursor-pointer hover:shadow-lg transition-shadow"
                    >
                        <CardHeader>
                            <CardTitle>{group.name}</CardTitle>
                        </CardHeader>
                        <CardContent>
                            <p className="text-sm text-muted-foreground">{group.description}</p>
                            <p className="text-xs mt-2">
                                {group.wordsCount} слов • {group.isPublic ? 'Публичная' : 'Приватная'}
                            </p>
                        </CardContent>
                    </Card>
                ))}
            </div>
        </div>
    );
}

export function GroupListSkeleton() {
    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
            {Array.from({ length: 8 }).map((_, index) => (
                <div key={index} className="p-4 border rounded shadow">
                    <Skeleton className="h-6 w-3/4 mb-2" />
                    <Skeleton className="h-4 w-full mb-2" />
                    <Skeleton className="h-4 w-1/2" />
                </div>
            ))}
        </div>
    );
}
