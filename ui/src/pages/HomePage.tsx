import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext.tsx';
import { me } from '../api/auth.ts';
import { getUserGroups, Group } from '@/api/groups.ts';
import { useNavigate } from 'react-router-dom';
import { GroupList, GroupListSkeleton } from '@/components/group-list.tsx';
import { Button } from '@/components/ui/button';
import { PlusIcon } from 'lucide-react';
import { NewGroupModal } from '@/components/create-group-modal.tsx';

export function HomePage() {
    const navigate = useNavigate();
    const [name, setName] = useState('');
    const [groups, setGroups] = useState<Group[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [modalOpen, setModalOpen] = useState(false);
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

    const handleGroupCreated = () => {
        setLoading(true);
        getUserGroups(token!)
            .then(setGroups)
            .catch(() => setError('Ошибка при обновлении списка групп'))
            .finally(() => setLoading(false));
    };

    if (error) {
        return (
            <main className="bg-muted/40 min-h-screen p-6">
                <p className="text-red-500">{error}</p>
            </main>
        );
    }

    return (
        <main className="bg-muted/40 min-h-[calc(100vh-3rem)] p-6">
            <div className="flex justify-between items-center mb-6">
                <div className="text-xl font-semibold">Привет, {name ? name : 'Пользователь'} 👋</div>
                <Button onClick={() => setModalOpen(true)} className="flex items-center gap-2">
                    <PlusIcon className="w-4 h-4" />
                    Добавить группу
                </Button>
            </div>
            {loading ? <GroupListSkeleton /> : <GroupList groups={groups} />}
            <NewGroupModal
                open={modalOpen}
                onClose={() => setModalOpen(false)}
                onGroupCreated={handleGroupCreated}
                token={token!}
            />
        </main>
    );
}
