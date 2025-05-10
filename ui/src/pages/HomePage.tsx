import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext.tsx';
import { me } from '../api/auth.ts';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';

export default function HomePage() {
    const [name, setName] = useState('');
    const { token } = useAuth();

    useEffect(() => {
        if (!token) return;
        me(token).then((response) => setName(response.name));
    }, [token]);

    return (
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
    );
}
