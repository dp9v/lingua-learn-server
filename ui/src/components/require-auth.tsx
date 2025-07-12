import {useAuth} from '@/context/AuthContext';
import {Navigate} from 'react-router-dom';

interface RequireAuthProps {
    children: React.ReactNode;
}

export function RequireAuth({children}: RequireAuthProps) {
    const {token} = useAuth();

    if (!token) {
        return <Navigate to="/login" replace/>;
    }

    return <>{children}</>;
}
