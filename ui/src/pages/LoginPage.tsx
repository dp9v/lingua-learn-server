import { useAuth } from '../context/AuthContext.tsx';
import { useHistory } from 'react-router-dom';
import { login } from '../api/auth.ts';
import { LoginForm } from '../components/login-form.tsx';

export default function LoginPage() {
    const { login: loginUser } = useAuth();
    const history = useHistory();

    const handleLogin = async (value: { email: string; password: string }) => {
        const data = await login(value);
        if (data.token) {
            loginUser(data.token);
            history.push('/');
        }
    };

    return (
        <div className="flex min-h-svh flex-col items-center justify-center gap-6 bg-muted p-6 md:p-10">
            <div className="flex w-full max-w-sm flex-col gap-6">
                <a href="#" className="flex items-center gap-2 self-center font-medium">
                    LinguaLearn
                </a>
                <LoginForm onFormSubmit={handleLogin} />
            </div>
        </div>
    );
}
