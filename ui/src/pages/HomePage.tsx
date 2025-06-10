import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext.tsx';
import { me } from '../api/auth.ts';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { getUserGroups, Group } from '@/api/groups.ts';
import { useNavigate } from 'react-router-dom';

export default function HomePage() {
    const navigate = useNavigate();
    const [name, setName] = useState('');
    const [groups, setGroups] = useState<Group[]>([]);
    const { token } = useAuth();

    useEffect(() => {
        if (!token) {
            navigate('/login');
            return;
        }
        me(token).then((response) => setName(response.name));
        getUserGroups(token).then((response) => setGroups(response));
    }, [token]);

    return (
        <main className="bg-muted/40 min-h-[calc(100vh-3rem)] p-6">
            <div className="welcome-message text-xl font-semibold mb-6">
                <p>Привет, {name ? name : 'Пользователь'} 👋</p>
                <p className="text-muted-foreground mt-2">Ниже представлены группы, которые вы добавили к себе.</p>
            </div>

            {groups.length === 0 ? (
                <div className="text-center mt-6">
                    <p className="text-muted-foreground text-lg">
                        У вас пока нет групп. Вы можете{' '}
                        <a href="/groups" className="text-blue-500 underline hover:text-blue-700">
                            просмотреть список публичных групп
                        </a>
                        .
                    </p>
                </div>
            ) : (
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
            )}
        </main>
    );
}
