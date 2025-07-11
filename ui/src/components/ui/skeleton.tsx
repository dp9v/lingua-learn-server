import { cn } from '@/lib/utils';

function Skeleton({ className, ...props }: React.ComponentProps<'div'>) {
    return (
        <div
            role="progressbar" // need for tests
            data-slot="skeleton"
            className={cn('bg-accent animate-pulse rounded-md', className)}
            {...props}
        />
    );
}

export { Skeleton };
