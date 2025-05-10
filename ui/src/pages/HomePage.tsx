import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext.tsx';
import { me } from '../api/auth.ts';
import { SidebarInset, SidebarProvider, SidebarTrigger } from '@/components/ui/sidebar.tsx';
import { AppSidebar } from '@/components/app-sidebar.tsx';
import { Separator } from '@/components/ui/separator.tsx';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';

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
                <Header title="Page name" />
                <main className="bg-muted/40 min-h-[calc(100vh-3rem)] p-6">
                    <Card>
                        <CardHeader>
                            <CardTitle>Добро пожаловать</CardTitle>
                        </CardHeader>
                        <CardContent>
                            <p className="text-xl font-semibold">Привет, {name ? name : 'Человек'} 👋</p>
                            <p className="text-muted-foreground mt-1">
                                Это ваша домашняя страница. Используйте меню слева для навигации.
                            </p>
                        </CardContent>
                    </Card>
                </main>
            </SidebarInset>
        </SidebarProvider>
    );
}

function Header({ title }: { title: string }) {
    return (
        <header className="flex h-12 items-center border-b px-4 lg:px-4">
            <SidebarTrigger className="mr-1" />
            <Separator orientation="vertical" className="mx-2 data-[orientation=vertical]:h-8" />
            <h1 className="text-lg font-medium mx-2">{title}</h1>
            <div className="ml-auto"></div>
        </header>
    );
}
