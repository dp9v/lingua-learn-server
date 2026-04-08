import { useEffect, useState } from 'react';
import { Label } from '@/components/ui/label.tsx';
import { Button } from '@/components/ui/button.tsx';
import { WordPair } from '@/api/types.ts';
import { getRandomWords } from '@/api/words.ts';
import { useAuth } from '@/context/AuthContext.tsx';
interface TrainingStepProps {
    selectedGroupIds: number[];
    wordsCount: number;
    onBackToSetup: () => void;
}
export function TrainingStep({ selectedGroupIds, wordsCount, onBackToSetup }: TrainingStepProps) {
    const [words, setWords] = useState<WordPair[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const { token } = useAuth();
    // Load words on mount
    useEffect(() => {
        const loadWords = async () => {
            if (!token) {
                setError('Authentication required');
                setIsLoading(false);
                return;
            }
            setIsLoading(true);
            setError(null);
            try {
                const response = await getRandomWords(
                    {
                        groups: selectedGroupIds,
                        count: wordsCount,
                    },
                    token
                );
                setWords(response.words);
            } catch (err) {
                console.error('Error loading words:', err);
                setError('Failed to load words. Please try again.');
            } finally {
                setIsLoading(false);
            }
        };
        loadWords();
    }, [selectedGroupIds, wordsCount, token]);
    return (
        <div className="space-y-6">
            {/* Training step - display loaded words */}
            <div className="space-y-3">
                <Label className="text-base font-semibold">
                    {isLoading ? 'Loading Words...' : `Loaded Words (${words.length})`}
                </Label>
                <div className="space-y-2 max-h-96 overflow-y-auto border rounded-md p-4">
                    {isLoading ? (
                        <div className="flex flex-col items-center justify-center py-8">
                            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mb-4"></div>
                            <p className="text-sm text-muted-foreground">Loading words...</p>
                        </div>
                    ) : error ? (
                        <div className="flex flex-col items-center justify-center py-8">
                            <p className="text-sm text-destructive mb-4">{error}</p>
                            <Button variant="outline" size="sm" onClick={() => window.location.reload()}>
                                Retry
                            </Button>
                        </div>
                    ) : words.length === 0 ? (
                        <p className="text-sm text-muted-foreground">No words loaded</p>
                    ) : (
                        words.map((wordPair, index) => (
                            <div
                                key={wordPair.id || index}
                                className="flex flex-col sm:flex-row sm:items-center justify-between p-3 bg-muted rounded-md gap-2"
                            >
                                <div className="flex-1">
                                    <p className="font-medium text-sm sm:text-base">{wordPair.from.text}</p>
                                    <p className="text-xs sm:text-sm text-muted-foreground">{wordPair.from.language}</p>
                                </div>
                                <div className="mx-0 sm:mx-4 text-muted-foreground text-center sm:text-left">→</div>
                                <div className="flex-1 text-left sm:text-right">
                                    <p className="font-medium text-sm sm:text-base">{wordPair.to.text}</p>
                                    <p className="text-xs sm:text-sm text-muted-foreground">{wordPair.to.language}</p>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </div>
            {/* Back button */}
            <div className="pt-4">
                <Button variant="outline" onClick={onBackToSetup} className="w-full" size="lg" disabled={isLoading}>
                    Back to Setup
                </Button>
            </div>
        </div>
    );
}
