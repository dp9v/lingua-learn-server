import { useState } from 'react';
import { useAuth } from '../context/AuthContext.tsx';
import { Link, useHistory } from 'react-router-dom';
import { login } from '../api/auth.ts';
import { Button } from 'reactstrap';

export default function LoginPage() {
    const [form, setForm] = useState({
        email: '',
        password: '',
    });
    const { login: loginUser } = useAuth();
    const history = useHistory();

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const data = await login(form);
        if (data.token) {
            loginUser(data.token);
            history.push('/');
        }
    };

    return (
        <div className="flex flex-col items-center justify-center min-h-screen px-4">
            <h2 className="text-2xl font-semibold mb-4">Вход</h2>
            <form onSubmit={handleSubmit} className="w-full max-w-sm space-y-4">
                <input
                    type="email"
                    placeholder="Email"
                    value={form.email}
                    onChange={(e) => setForm({ ...form, email: e.target.value })}
                    required
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={form.password}
                    onChange={(e) => setForm({ ...form, password: e.target.value })}
                    required
                />
                <Button type="submit" className="w-full">
                    Login
                </Button>
            </form>
            <p className="mt-4 text-sm text-gray-500">
                Нет аккаунта?{' '}
                <Link to="/register" className="text-blue-600 hover:underline">
                    Зарегистрируйтесь
                </Link>
            </p>
        </div>
    );
}
