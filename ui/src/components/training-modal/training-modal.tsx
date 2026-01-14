import { useEffect, useState, useCallback } from 'react';
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
import { XIcon } from 'lucide-react';
import { SetupStep } from './setup-step.tsx';
import { TrainingStep } from './training-step.tsx';
interface TrainingModalProps {
    open: boolean;
    onClose: () => void;
    initialGroupId?: number;
}
export function TrainingModal({ open, onClose, initialGroupId }: TrainingModalProps) {
    const [showConfirmDialog, setShowConfirmDialog] = useState(false);
    const [currentStep, setCurrentStep] = useState<'setup' | 'training'>('setup');
    const [trainingParams, setTrainingParams] = useState<{
        selectedGroupIds: number[];
        wordsCount: number;
    } | null>(null);

    const handleConfirmClose = useCallback(() => {
        setShowConfirmDialog(false);
        // Navigate back in history if the modal was added to it
        if (window.history.state?.modal === 'training') {
            window.history.back();
        }
        // Reset state on close
        setCurrentStep('setup');
        setTrainingParams(null);
        onClose();
    }, [onClose]);

    const handleClose = useCallback(() => {
        // Show confirmation dialog only if on training step
        if (currentStep === 'training') {
            setShowConfirmDialog(true);
        } else {
            // On setup step, close directly
            handleConfirmClose();
        }
    }, [currentStep, handleConfirmClose]);

    // Handle browser back button
    useEffect(() => {
        if (!open) return;
        const handlePopState = (e: PopStateEvent) => {
            e.preventDefault();
            handleClose();
        };
        // Add an entry to history when modal opens
        window.history.pushState({ modal: 'training' }, '');
        window.addEventListener('popstate', handlePopState);
        return () => {
            window.removeEventListener('popstate', handlePopState);
        };
    }, [open, handleClose]);

    const handleCancelClose = () => {
        setShowConfirmDialog(false);
    };

    const handleStartTraining = (params: { selectedGroupIds: number[]; wordsCount: number }) => {
        setTrainingParams(params);
        setCurrentStep('training');
    };
    if (!open) return null;
    return (
        <>
            <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-3 sm:p-4 overflow-y-auto">
                <div className="w-full max-w-2xl my-auto">
                    <Card className="w-full max-h-[calc(100svh-2rem)] overflow-y-auto">
                        <CardHeader className="relative p-4 sm:p-6">
                            <Button
                                variant="ghost"
                                size="icon"
                                className="absolute right-2 top-2 sm:right-4 sm:top-4"
                                onClick={handleClose}
                                aria-label="Close"
                            >
                                <XIcon className="h-4 w-4" />
                            </Button>
                            <CardTitle className="pr-10 text-lg sm:text-xl">
                                {currentStep === 'setup' ? 'Training Setup' : 'Training'}
                            </CardTitle>
                        </CardHeader>
                        <CardContent className="p-4 sm:p-6">
                            {currentStep === 'setup' ? (
                                <SetupStep initialGroupId={initialGroupId} onStartTraining={handleStartTraining} />
                            ) : trainingParams ? (
                                <TrainingStep
                                    selectedGroupIds={trainingParams.selectedGroupIds}
                                    wordsCount={trainingParams.wordsCount}
                                    onBackToSetup={() => setCurrentStep('setup')}
                                />
                            ) : null}
                        </CardContent>
                    </Card>
                </div>
            </div>
            {/* Exit confirmation dialog */}
            <Dialog open={showConfirmDialog} onOpenChange={setShowConfirmDialog}>
                <DialogContent className="w-[calc(100%-2rem)] sm:max-w-md">
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
