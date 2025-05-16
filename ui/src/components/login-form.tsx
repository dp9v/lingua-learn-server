import { cn } from '@/lib/utils.ts';
import { Loader2 } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card.tsx';
import { Input } from '@/components/ui/input.tsx';
import { Label } from '@/components/ui/label.tsx';
import { Button } from '@/components/ui/button.tsx';
import { useState } from 'react';

interface LoginFormProps extends React.ComponentPropsWithoutRef<'div'> {
    onFormSubmit: (value: { email: string; password: string }) => Promise<void>;
}

export function LoginForm({ className, onFormSubmit, ...props }: LoginFormProps) {
    const [form, setForm] = useState({ email: '', password: '' });
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError(null);
        setIsLoading(true);
        onFormSubmit(form)
            .catch((reason) => {
                setError(reason.message);
            })
            .finally(() => {
                setIsLoading(false);
            });
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setForm((prev) => ({
            ...prev,
            [e.target.name]: e.target.value,
        }));
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
                                        placeholder="name@email.com"
                                        required
                                        name="email"
                                        value={form.email}
                                        onChange={handleChange}
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
                                        name="password"
                                        value={form.password}
                                        onChange={handleChange}
                                    />
                                </div>
                                {error && <Label className="block text-center text-red-500">{error}</Label>}
                                <Button type="submit" className="w-full" disabled={isLoading}>
                                    {isLoading ? <Loader2 className="animate-spin h-4 w-4" /> : 'Login'}
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
