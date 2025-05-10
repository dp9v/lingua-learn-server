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

    return <RegisterForm onFormSubmit={handleRegister} />;
}
