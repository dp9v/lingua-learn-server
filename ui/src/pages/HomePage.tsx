import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext.tsx';
import { me } from '../api/auth.ts';
import { getUserGroups, Group } from '@/api/groups.ts';
import { useNavigate } from 'react-router-dom';
import { GroupList, GroupListSkeleton } from '@/components/group-list.tsx';

export function HomePage() {
    const navigate = useNavigate();
    const [name, setName] = useState('');
    const [groups, setGroups] = useState<Group[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const { token } = useAuth();

    useEffect(() => {
        if (!token) {
            navigate('/login');
            return;
        }
        Promise.all([me(token), getUserGroups(token)])
            .then(([meResponse, groupsResponse]) => {
                setName(meResponse.name);
                setGroups(groupsResponse);
            })
            .catch((error) => setError('Ошиба при загрузке данных:' + error))
            .finally(() => setLoading(false));
    }, [navigate, token]);

    if (error) {
        return (
            <main className="bg-muted/40 min-h-screen p-6">
                <p className="text-red-500">{error}</p>
            </main>
        );
    }

    return (
        <main className="bg-muted/40 min-h-[calc(100vh-3rem)] p-6">
            <div className="text-xl font-semibold mb-6">Привет, {name ? name : 'Пользователь'} 👋</div>
            {loading ? <GroupListSkeleton /> : <GroupList groups={groups} />}
        </main>
    );
}
