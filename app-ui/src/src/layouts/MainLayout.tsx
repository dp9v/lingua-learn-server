import { SidebarInset, SidebarProvider, SidebarTrigger } from '@/components/ui/sidebar.tsx';
import { AppSidebar } from '@/components/app-sidebar.tsx';
import { Separator } from '@/components/ui/separator.tsx';
import { Outlet } from 'react-router-dom';

export const MainLayout = () => {
    return (
        <SidebarProvider style={{ '--sidebar-width': '19rem' } as React.CSSProperties}>
            <AppSidebar />
            <SidebarInset>
                <Header title="Page name" />
                <Outlet />
            </SidebarInset>
        </SidebarProvider>
    );
};

function Header({ title }: { title: string }) {
    return (
        <header className="flex h-12 sm:h-14 items-center border-b px-3 sm:px-4 lg:px-4">
            <SidebarTrigger className="mr-1" />
            <Separator orientation="vertical" className="mx-2 data-[orientation=vertical]:h-8" />
            <h1 className="text-base sm:text-lg font-medium mx-2 truncate">{title}</h1>
            <div className="ml-auto"></div>
        </header>
    );
}
