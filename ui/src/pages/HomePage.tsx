import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext.tsx';
import { me } from '../api/auth.ts';

export default function HomePage() {
    const [name, setName] = useState('');
    const { token } = useAuth();

    useEffect(() => {
        if (!token) return;
        me(token).then((response) => setName(response.name));
    }, [token]);
    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-50 px-4">
            <h1 className="text-3xl font-bold mb-6">Добро пожаловать, {name}!</h1>
        </div>
    );
}
