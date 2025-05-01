import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext.tsx';
import { me } from '../api/auth.ts';
import { SidebarInset, SidebarProvider, SidebarTrigger } from '@/components/ui/sidebar.tsx';
import { AppSidebar } from '@/components/app-sidebar.tsx';
import { Separator } from '@/components/ui/separator.tsx';

export default function HomePage() {
    const [name, setName] = useState('');
    const { token } = useAuth();

    useEffect(() => {
        if (!token) return;
        me(token).then((response) => setName(response.name));
    }, [token]);
    return (
        <SidebarProvider style={{ '--sidebar-width': '19rem' } as React.CSSProperties}>
            <AppSidebar />
            <SidebarInset>
                <header className="group-has-data-[collapsible=icon]/sidebar-wrapper:h-12 flex h-12 shrink-0 items-center gap-2 border-b transition-[width,height] ease-linear">
                    <div className="flex w-full items-center gap-1 px-4 lg:gap-2 lg:px-6">
                        <SidebarTrigger className="-ml-1" />
                        <Separator orientation="vertical" className="mx-2 data-[orientation=vertical]:h-4" />
                        <h1 className="text-base font-medium">PageName</h1>
                    </div>
                </header>
                <main className="bg-white shadow-sm">
                    <div className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8">
                        <h1 className="text-3xl font-bold tracking-tight text-gray-900">Hello, {name}</h1>
                    </div>
                </main>
            </SidebarInset>
        </SidebarProvider>
    );
}
