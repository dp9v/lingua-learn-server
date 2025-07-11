import { Group } from '@/api/groups';
import { Skeleton } from '@/components/ui/skeleton.tsx';

interface GroupInfoProps {
    group: Group;
    wordsCount: number;
}

export function GroupInfo({ group, wordsCount }: GroupInfoProps) {
    return (
        <div className="mb-4 space-y-1">
            <p className="text-muted-foreground">{group.description}</p>
            <p className="text-sm text-muted-foreground">Всего слов: {wordsCount}</p>
        </div>
    );
}

export function GroupInfoSkeleton() {
    return (
        <div className="mb-4 space-y-2">
            <Skeleton className="h-4 w-full" />
            <Skeleton className="h-4 w-1/3" />
            <Skeleton className="h-4 w-1/4" />
        </div>
    );
}
