import { useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import { register } from '../api/auth.ts';
import { Button, Input } from 'reactstrap';

export default function RegisterPage() {
    const [form, setForm] = useState({
        email: '',
        password: '',
        name: '',
        surname: '',
    });
    const history = useHistory();

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        await register(form);
        history.push('/login');
    };

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-white px-4">
            <h2 className="text-2xl font-semibold mb-4">Регистрация</h2>
            <form onSubmit={handleSubmit} className="w-full max-w-sm space-y-4">
                <Input
                    type="email"
                    placeholder="Email"
                    value={form.email}
                    onChange={(e) => setForm({ ...form, email: e.target.value })}
                    required
                />
                <Input
                    type="password"
                    placeholder="Password"
                    value={form.password}
                    onChange={(e) => setForm({ ...form, password: e.target.value })}
                    required
                />
                <Input
                    type="text"
                    placeholder="Name"
                    value={form.name}
                    onChange={(e) => setForm({ ...form, name: e.target.value })}
                />
                <Input
                    type="text"
                    placeholder="Surname"
                    value={form.surname}
                    onChange={(e) => setForm({ ...form, surname: e.target.value })}
                />
                <Button type="submit" className="w-full">
                    Зарегистрироваться
                </Button>
            </form>
            <p className="mt-4 text-sm text-gray-500">
                Уже есть аккаунт?{' '}
                <Link to="/login" className="text-blue-600 hover:underline">
                    Войти
                </Link>
            </p>
        </div>
    );
}
