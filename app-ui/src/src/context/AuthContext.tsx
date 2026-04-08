import { createContext, useContext, useEffect, useState } from 'react';

type AuthContextType = {
    token: string | null;
    login: (token: string, expiresAt: string) => void;
    logout: () => void;
};

const AuthContext = createContext<AuthContextType>({
    token: null,
    login: () => {},
    logout: () => {},
});

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
    const [token, setToken] = useState<string | null>(localStorage.getItem('token'));

    useEffect(() => {
        const expiresAt = localStorage.getItem('expiresAt');
        if (expiresAt) {
            const expiry = new Date(expiresAt).getTime();
            const now = Date.now();

            if (now >= expiry) {
                logout();
            } else {
                // Automatic logout on timer
                const timeout = setTimeout(() => logout(), expiry - now);
                return () => clearTimeout(timeout); // Cleanup on unmount
            }
        }
    }, []);

    const login = (token: string, expiresAt: string) => {
        localStorage.setItem('token', token);
        localStorage.setItem('expiresAt', expiresAt);
        setToken(token);
    };

    const logout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('expiresAt');
        setToken(null);
    };

    return <AuthContext.Provider value={{ token, login, logout }}>{children}</AuthContext.Provider>;
};

export const useAuth = () => useContext(AuthContext);
