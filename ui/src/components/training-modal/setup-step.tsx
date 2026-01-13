import { useEffect, useState } from 'react';
import { Checkbox } from '@/components/ui/checkbox.tsx';
import { Slider } from '@/components/ui/slider.tsx';
import { Input } from '@/components/ui/input.tsx';
import { Label } from '@/components/ui/label.tsx';
import { Button } from '@/components/ui/button.tsx';
import { Group } from '@/api/types.ts';
import { getUserGroups } from '@/api/groups';
import { useAuth } from '@/context/AuthContext.tsx';

interface SetupStepProps {
    initialGroupId?: number;
    onStartTraining: (params: { selectedGroupIds: number[]; wordsCount: number }) => void;
}

export function SetupStep({ initialGroupId, onStartTraining }: SetupStepProps) {
    const [groups, setGroups] = useState<Group[]>([]);
    const [selectedGroupIds, setSelectedGroupIds] = useState<number[]>([]);
    const [wordsCount, setWordsCount] = useState<number>(10);
    const [isLoading, setIsLoading] = useState(false);
    const { token } = useAuth();

    // Load user groups
    useEffect(() => {
        if (!token) return;

        const loadGroups = async () => {
            setIsLoading(true);
            try {
                const data = await getUserGroups(token);
                setGroups(data);
            } catch (error) {
                console.error('Error loading groups:', error);
            } finally {
                setIsLoading(false);
            }
        };

        loadGroups();
    }, [token]);

    // Auto-select initial group
    useEffect(() => {
        if (initialGroupId && groups.length > 0 && selectedGroupIds.length === 0) {
            const groupExists = groups.some((g) => g.id === initialGroupId);
            if (groupExists) {
                setSelectedGroupIds([initialGroupId]);
            }
        }
    }, [initialGroupId, groups, selectedGroupIds]);

    const handleGroupToggle = (groupId: number) => {
        setSelectedGroupIds((prev) =>
            prev.includes(groupId) ? prev.filter((id) => id !== groupId) : [...prev, groupId]
        );
    };

    const handleWordsCountChange = (value: number[]) => {
        setWordsCount(value[0]);
    };

    const handleWordsCountInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = parseInt(e.target.value, 10);
        if (!isNaN(value) && value >= 5 && value <= 30) {
            setWordsCount(value);
        }
    };

    const handleStart = () => {
        if (selectedGroupIds.length === 0) {
            console.warn('Please select at least one group before starting training.');
            return;
        }

        onStartTraining({ selectedGroupIds, wordsCount });
    };

    return (
        <div className="space-y-6">
            {/* Groups list */}
            <div className="space-y-3">
                <Label className="text-base font-semibold">Select groups for training</Label>
                {isLoading ? (
                    <p className="text-sm text-muted-foreground">Loading groups...</p>
                ) : groups.length === 0 ? (
                    <p className="text-sm text-muted-foreground">
                        You don't have any groups yet. Create a group to start training.
                    </p>
                ) : (
                    <div className="space-y-2 max-h-48 overflow-y-auto border rounded-md p-3">
                        {groups.map((group) => (
                            <div key={group.id} className="flex items-center space-x-2">
                                <Checkbox
                                    id={`group-${group.id}`}
                                    checked={selectedGroupIds.includes(group.id)}
                                    onCheckedChange={() => handleGroupToggle(group.id)}
                                />
                                <label
                                    htmlFor={`group-${group.id}`}
                                    className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70 cursor-pointer flex-1"
                                >
                                    {group.name}
                                    <span className="text-muted-foreground ml-2">({group.wordsCount} words)</span>
                                </label>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {/* Words count slider */}
            <div className="space-y-3">
                <Label className="text-base font-semibold">Number of words for training</Label>
                <div className="flex items-center space-x-4">
                    <div className="flex-1">
                        <Slider
                            value={[wordsCount]}
                            onValueChange={handleWordsCountChange}
                            min={5}
                            max={30}
                            step={1}
                            className="w-full"
                        />
                    </div>
                    <Input
                        type="number"
                        min={5}
                        max={30}
                        value={wordsCount}
                        onChange={handleWordsCountInputChange}
                        className="w-20"
                    />
                </div>
                <p className="text-xs text-muted-foreground">Choose from 5 to 30 words</p>
            </div>

            {/* Start button */}
            <div className="pt-4">
                <Button
                    onClick={handleStart}
                    disabled={selectedGroupIds.length === 0 || isLoading}
                    className="w-full"
                    size="lg"
                >
                    {isLoading ? 'Loading...' : 'Start Training'}
                </Button>
            </div>
        </div>
    );
}
