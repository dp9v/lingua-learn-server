import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import LoginPage from '../pages/LoginPage.tsx';
import RegisterPage from '../pages/RegisterPage.tsx';
import HomePage from '../pages/HomePage.tsx';
import { useAuth } from '../context/AuthContext.tsx';
import { AuthLayout } from '@/layouts/AuthLayout.tsx';
import { MainLayout } from '@/layouts/MainLayout.tsx';

export default function AppRoutes() {
    const { token } = useAuth();

    return (
        <BrowserRouter>
            <Routes>
                <Route element={<AuthLayout />}>
                    <Route path="/login" element={token ? <Navigate to="/" replace /> : <LoginPage />} />
                    <Route path="/register" element={<RegisterPage />} />
                </Route>
                <Route element={<MainLayout />}>
                    <Route path="/" element={token ? <HomePage /> : <Navigate to="/login" replace />} />
                </Route>
            </Routes>
        </BrowserRouter>
    );
}
