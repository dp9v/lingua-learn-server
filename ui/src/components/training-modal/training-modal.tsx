import { useEffect, useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card.tsx';
import { Button } from '@/components/ui/button.tsx';
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from '@/components/ui/dialog.tsx';
import { Checkbox } from '@/components/ui/checkbox.tsx';
import { Slider } from '@/components/ui/slider.tsx';
import { Input } from '@/components/ui/input.tsx';
import { Label } from '@/components/ui/label.tsx';
import { XIcon } from 'lucide-react';
import { useAuth } from '@/context/AuthContext.tsx';
import { getUserGroups, Group } from '@/api/groups.ts';

interface TrainingModalProps {
    open: boolean;
    onClose: () => void;
    initialGroupId?: number;
}

export function TrainingModal({ open, onClose, initialGroupId }: TrainingModalProps) {
    const [showConfirmDialog, setShowConfirmDialog] = useState(false);
    const [groups, setGroups] = useState<Group[]>([]);
    const [selectedGroupIds, setSelectedGroupIds] = useState<number[]>([]);
    const [wordsCount, setWordsCount] = useState<number>(10);
    const [isLoading, setIsLoading] = useState(false);
    const { token } = useAuth();

    // Load user groups
    useEffect(() => {
        if (!open || !token) return;

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
    }, [open, token]);

    // Auto-select initial group
    useEffect(() => {
        if (initialGroupId && groups.length > 0 && selectedGroupIds.length === 0) {
            const groupExists = groups.some((g) => g.id === initialGroupId);
            if (groupExists) {
                setSelectedGroupIds([initialGroupId]);
            }
        }
    }, [initialGroupId, groups, selectedGroupIds]);

    // Handle browser back button
    useEffect(() => {
        if (!open) return;

        const handlePopState = (e: PopStateEvent) => {
            e.preventDefault();
            handleClose();
        };

        // Add entry to history when modal opens
        window.history.pushState({ modal: 'training' }, '');

        window.addEventListener('popstate', handlePopState);

        return () => {
            window.removeEventListener('popstate', handlePopState);
        };
    }, [open]);

    const handleClose = () => {
        setShowConfirmDialog(true);
    };

    const handleConfirmClose = () => {
        setShowConfirmDialog(false);
        // Remove entry from history if it exists
        if (window.history.state?.modal === 'training') {
            window.history.back();
        }
        // Reset selected groups on close
        setSelectedGroupIds([]);
        onClose();
    };

    const handleCancelClose = () => {
        setShowConfirmDialog(false);
    };

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

    const handleStartTraining = () => {
        if (selectedGroupIds.length === 0) {
            alert('Please select at least one group');
            return;
        }
        // TODO: Training start logic
        console.log('Start training:', {
            selectedGroupIds,
            wordsCount,
        });
    };

    if (!open) return null;

    return (
        <>
            <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4">
                <Card className="w-full max-w-2xl max-h-[90vh] overflow-y-auto">
                    <CardHeader className="relative">
                        <Button
                            variant="ghost"
                            size="icon"
                            className="absolute right-4 top-4"
                            onClick={handleClose}
                            aria-label="Close"
                        >
                            <XIcon className="h-4 w-4" />
                        </Button>
                        <CardTitle className="pr-10">Training</CardTitle>
                    </CardHeader>
                    <CardContent>
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
                                                    <span className="text-muted-foreground ml-2">
                                                        ({group.wordsCount} words)
                                                    </span>
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
                                    onClick={handleStartTraining}
                                    disabled={selectedGroupIds.length === 0 || isLoading}
                                    className="w-full"
                                    size="lg"
                                >
                                    Start Training
                                </Button>
                            </div>
                        </div>
                    </CardContent>
                </Card>
            </div>

            {/* Exit confirmation dialog */}
            <Dialog open={showConfirmDialog} onOpenChange={setShowConfirmDialog}>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle>Confirm Exit</DialogTitle>
                        <DialogDescription>
                            Are you sure you want to exit? All unsaved data will be lost.
                        </DialogDescription>
                    </DialogHeader>
                    <DialogFooter className="flex-col sm:flex-row gap-2">
                        <Button variant="outline" onClick={handleCancelClose} className="w-full sm:w-auto">
                            Cancel
                        </Button>
                        <Button variant="destructive" onClick={handleConfirmClose} className="w-full sm:w-auto">
                            Exit
                        </Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </>
    );
}
