import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext.tsx';
import { me } from '../api/auth.ts';
import { getUserGroups } from '@/api/groups.ts';
import { Group } from '@/api/types.ts';
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
        <main className="bg-muted/40 min-h-[100svh] lg:min-h-[calc(100vh-3rem)] p-3 sm:p-6">
            <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-3 mb-4 sm:mb-6">
                <div className="text-lg sm:text-xl font-semibold">Hello, {name ? name : 'User'} 👋</div>
                <div className="flex gap-2 w-full sm:w-auto">
                    <Button
                        onClick={openTraining}
                        variant="outline"
                        className="flex items-center gap-2 flex-1 sm:flex-none"
                    >
                        <DumbbellIcon className="w-4 h-4" />
                        <span className="sm:inline">Training</span>
                    </Button>
                    <Button onClick={() => setModalOpen(true)} className="flex items-center gap-2 flex-1 sm:flex-none">
                        <PlusIcon className="w-4 h-4" />
                        <span className="sm:inline">Add Group</span>
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
