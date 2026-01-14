import type { WordPair } from '@/api/types';
import { Skeleton } from '@/components/ui/skeleton.tsx';

interface WordsTableProps {
    words: WordPair[];
}

export function WordsTable({ words }: WordsTableProps) {
    return (
        <div className="overflow-x-auto -mx-4 sm:mx-0">
            <div className="min-w-full inline-block align-middle">
                <table className="text-left border-separate border-spacing-y-2 w-full min-w-[500px] sm:min-w-0">
                    <thead>
                        <tr>
                            <th className="px-2 sm:px-4 py-2 text-sm sm:text-base">Слово (из)</th>
                            <th className="px-2 sm:px-4 py-2 text-sm sm:text-base">Слово (в)</th>
                        </tr>
                    </thead>
                    <tbody>
                        {words.map((pair) => (
                            <tr key={pair.id} className="bg-white rounded shadow-sm">
                                <td className="px-2 sm:px-4 py-2 text-sm sm:text-base">
                                    {pair.from.text} ({pair.from.language})
                                </td>
                                <td className="px-2 sm:px-4 py-2 text-sm sm:text-base">
                                    {pair.to.text} ({pair.to.language})
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export function WordsTableSkeleton() {
    return (
        <div className="space-y-2">
            {Array.from({ length: 5 }).map((_, index) => (
                <Skeleton key={index} className="h-6 w-full" />
            ))}
        </div>
    );
}
