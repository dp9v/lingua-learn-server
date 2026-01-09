import { RegisterRequestData } from '@/api/types.ts';
import { Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';

import { cn } from '@/lib/utils.ts';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card.tsx';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button.tsx';
import { z } from 'zod';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { ValidationError } from '@/api/utils.ts';
import { useState } from 'react';
import { Loader2 } from 'lucide-react';

interface RegisterFormProps extends React.ComponentPropsWithoutRef<'div'> {
    onFormSubmit: (value: RegisterRequestData) => Promise<void>;
}

const formSchema = z.object({
    email: z.string().email('Invalid email'),
    password: z.string().min(8, 'Password must be at least 8 characters long.'),
    name: z.string().optional(),
    surname: z.string().optional(),
});

export function RegisterForm({ className, onFormSubmit, ...props }: RegisterFormProps) {
    const [isLoading, setIsLoading] = useState(false);
    const registerForm = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
    });

    const onSubmit = async (data: z.infer<typeof formSchema>) => {
        try {
            setIsLoading(true);
            await onFormSubmit(data);
        } catch (error) {
            if (error instanceof ValidationError) {
                const errors = error.validationErrors;
                Object.keys(errors).forEach((field) => {
                    registerForm.setError(field as keyof z.infer<typeof formSchema>, {
                        type: 'server',
                        message: errors[field],
                    });
                });
            }
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className={cn('flex flex-col gap-6', className)} {...props}>
            <Card>
                <CardHeader className="text-center">
                    <CardTitle className="text-xl">Welcome to LinguaLearn</CardTitle>
                </CardHeader>
                <CardContent>
                    <Form {...registerForm}>
                        <form onSubmit={registerForm.handleSubmit(onSubmit)} className="space-y-4">
                            <FormField
                                control={registerForm.control}
                                name={'email'}
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>Email</FormLabel>
                                        <FormControl>
                                            <Input {...field} />
                                        </FormControl>
                                        <FormDescription>Email will be used as login</FormDescription>
                                        <FormMessage />
                                    </FormItem>
                                )}
                            />
                            <FormField
                                control={registerForm.control}
                                name={'password'}
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>Password</FormLabel>
                                        <FormControl>
                                            <Input {...field} />
                                        </FormControl>
                                        <FormMessage />
                                    </FormItem>
                                )}
                            />
                            <FormField
                                control={registerForm.control}
                                name={'name'}
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>Name</FormLabel>
                                        <FormControl>
                                            <Input {...field} />
                                        </FormControl>
                                        <FormMessage />
                                    </FormItem>
                                )}
                            />
                            <FormField
                                control={registerForm.control}
                                name={'surname'}
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>Surname</FormLabel>
                                        <FormControl>
                                            <Input {...field} />
                                        </FormControl>
                                        <FormMessage />
                                    </FormItem>
                                )}
                            />
                            <Button type="submit" className="w-full" disabled={isLoading}>
                                {isLoading ? <Loader2 className="animate-spin h-4 w-4" /> : 'Register'}
                            </Button>

                            <div className="text-center text-sm">
                                Already have an account?{' '}
                                <a href="/login" className="underline underline-offset-4">
                                    Sign in
                                </a>
                            </div>
                        </form>
                    </Form>
                </CardContent>
            </Card>
        </div>
    );
}
