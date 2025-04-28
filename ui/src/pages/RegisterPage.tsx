import { useNavigate } from 'react-router-dom';
import { register, RegisterRequestData } from '../api/auth.ts';
import { RegisterForm } from '@/components/register-form.tsx';

export default function RegisterPage() {
    const navigate = useNavigate();

    const handleRegister = async (value: RegisterRequestData) => {
        const result = await register(value);
        if (result == 'OK') {
            navigate('/login');
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
