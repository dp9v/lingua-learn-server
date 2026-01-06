import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext.tsx';
import { me } from '../api/auth.ts';
import { getUserGroups, Group } from '@/api/groups.ts';
import { useNavigate } from 'react-router-dom';
import { GroupList, GroupListSkeleton } from '@/components/group-list.tsx';
import { Button } from '@/components/ui/button';
import { PlusIcon, DumbbellIcon } from 'lucide-react';
import { CreateGroupModal } from '@/components/create-group-modal.tsx';
import { useTrainingModal } from '@/hooks/use-training-modal';
import { TrainingModal } from '@/components/training-modal/training-modal.tsx';

export function HomePage() {
    const navigate = useNavigate();
    const { isOpen, openTraining, closeTraining } = useTrainingModal();
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
            .catch((error) => setError('Error loading data:' + error))
            .finally(() => setLoading(false));
    }, [navigate, token]);

    const onGroupCreated = () => {
        setLoading(true);
        getUserGroups(token!)
            .then(setGroups)
            .catch(() => setError('Error updating groups list'))
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
                <div className="text-xl font-semibold">Hello, {name ? name : 'User'} 👋</div>
                <div className="flex gap-2">
                    <Button onClick={openTraining} variant="outline" className="flex items-center gap-2">
                        <DumbbellIcon className="w-4 h-4" />
                        <span className="hidden sm:inline">Training</span>
                    </Button>
                    <Button onClick={() => setModalOpen(true)} className="flex items-center gap-2">
                        <PlusIcon className="w-4 h-4" />
                        <span className="hidden sm:inline">Add Group</span>
                    </Button>
                </div>
            </div>
            {loading ? <GroupListSkeleton /> : <GroupList groups={groups} />}
            <CreateGroupModal
                open={modalOpen}
                onClose={() => setModalOpen(false)}
                onGroupCreated={onGroupCreated}
                token={token!}
            />
            <TrainingModal open={isOpen} onClose={closeTraining} />
        </main>
    );
}
