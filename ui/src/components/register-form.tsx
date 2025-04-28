import { RegisterRequestData } from '@/api/auth.ts';
import { useState } from 'react';
import { cn } from '@/lib/utils.ts';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card.tsx';
import { Label } from '@/components/ui/label.tsx';
import { Input } from '@/components/ui/input.tsx';
import { Button } from '@/components/ui/button.tsx';

interface RegisterFormProps extends React.ComponentPropsWithoutRef<'div'> {
    onFormSubmit: (value: RegisterRequestData) => void;
}

export function RegisterForm({ className, onFormSubmit, ...props }: RegisterFormProps) {
    const [form, setForm] = useState({
        email: '',
        password: '',
        name: '',
        surname: '',
    });
    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        onFormSubmit(form);
    };
    return (
        <div className={cn('flex flex-col gap-6', className)} {...props}>
            <Card>
                <CardHeader className="text-center">
                    <CardTitle className="text-xl">Welcome to LinguaLearn</CardTitle>
                </CardHeader>
                <CardContent>
                    <form onSubmit={handleSubmit}>
                        <div className="grid gap-6">
                            <div className="grid gap-6">
                                <div className="grid gap-2">
                                    <Label htmlFor="email">Email</Label>
                                    <Input
                                        id="email"
                                        type="email"
                                        placeholder="email@example.com"
                                        required
                                        value={form.email}
                                        onChange={(e) => setForm({ ...form, email: e.target.value })}
                                    />
                                </div>
                                <div className="grid gap-2">
                                    <Label htmlFor="password">Password</Label>
                                    <Input
                                        id="password"
                                        type="password"
                                        required
                                        value={form.password}
                                        onChange={(e) => setForm({ ...form, password: e.target.value })}
                                    />
                                </div>
                                <div className="grid gap-2">
                                    <Label htmlFor="name">Name</Label>
                                    <Input
                                        id="name"
                                        type="text"
                                        required
                                        value={form.name}
                                        onChange={(e) => setForm({ ...form, name: e.target.value })}
                                    />
                                </div>
                                <div className="grid gap-2">
                                    <Label htmlFor="surname">Surname</Label>
                                    <Input
                                        id="surname"
                                        type="text"
                                        required
                                        value={form.surname}
                                        onChange={(e) => setForm({ ...form, surname: e.target.value })}
                                    />
                                </div>
                                <Button type="submit" className="w-full">
                                    Register
                                </Button>
                            </div>
                            <div className="text-center text-sm">
                                Already have an account?{' '}
                                <a href="/login" className="underline underline-offset-4">
                                    Sign in
                                </a>
                            </div>
                        </div>
                    </form>
                </CardContent>
            </Card>
            <form onSubmit={handleSubmit}>
                <div className="grid gap-6">
                    <div className="grid gap-6"></div>
                </div>
            </form>
        </div>
    );
}
