import { cn } from '@/lib/utils.ts';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card.tsx';
import { Input } from '@/components/ui/input.tsx';
import { Label } from '@/components/ui/label.tsx';
import { Button } from '@/components/ui/button.tsx';
import { useState } from 'react';

interface LoginFormProps extends React.ComponentPropsWithoutRef<'div'> {
    onFormSubmit: (value: { email: string; password: string }) => void;
}

export function LoginForm({ className, onFormSubmit, ...props }: LoginFormProps) {
    const [form, setForm] = useState({
        email: '',
        password: '',
    });

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        onFormSubmit(form);
    };
    return (
        <div className={cn('flex flex-col gap-6', className)} {...props}>
            <Card>
                <CardHeader className="text-center">
                    <CardTitle className="text-xl">Welcome back</CardTitle>
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
                                        placeholder="m@example.com"
                                        required
                                        value={form.email}
                                        onChange={(e) => setForm({ ...form, email: e.target.value })}
                                    />
                                </div>
                                <div className="grid gap-2">
                                    <div className="flex items-center">
                                        <Label htmlFor="password">Password</Label>
                                        {/*<a href="#" className="ml-auto text-sm underline-offset-4 hover:underline">*/}
                                        {/*    Forgot your password?*/}
                                        {/*</a>*/}
                                    </div>
                                    <Input
                                        id="password"
                                        type="password"
                                        required
                                        value={form.password}
                                        onChange={(e) => setForm({ ...form, password: e.target.value })}
                                    />
                                </div>
                                <Button type="submit" className="w-full">
                                    Login
                                </Button>
                            </div>
                            <div className="text-center text-sm">
                                Don&apos;t have an account?{' '}
                                <a href="/register" className="underline underline-offset-4">
                                    Register here
                                </a>
                            </div>
                        </div>
                    </form>
                </CardContent>
            </Card>
        </div>
    );
}
