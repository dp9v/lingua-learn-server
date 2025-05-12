import { useAuth } from '../context/AuthContext.tsx';
import { useNavigate } from 'react-router-dom';
import { login } from '../api/auth.ts';
import { LoginForm } from '../components/login-form.tsx';

export default function LoginPage() {
    const { login: loginUser } = useAuth();
    const navigate = useNavigate();

    const handleLogin = async (value: { email: string; password: string }) => {
        return login(value).then((data) => {
            if (data.token) {
                loginUser(data.token);
                navigate('/');
            }
        });
    };

    return <LoginForm onFormSubmit={handleLogin} />;
}
