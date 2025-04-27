import { useHistory } from 'react-router-dom';
import { register, RegisterRequiestData } from '../api/auth.ts';
import { RegisterForm } from '@/components/register-form.tsx';

export default function RegisterPage() {
    const history = useHistory();

    const handleRegister = async (value: RegisterRequiestData) => {
        const result = await register(value);
        if (result == 'OK') {
            history.push('/login');
        }
    };

    return (
        <div className="flex min-h-svh flex-col items-center justify-center gap-6 bg-muted p-6 md:p-10">
            <div className="flex w-full max-w-sm flex-col gap-6">
                <a href="#" className="flex items-center gap-2 self-center font-medium">
                    LinguaLearn
                </a>
                <RegisterForm onFormSubmit={handleRegister} />
            </div>
        </div>
    );
}
