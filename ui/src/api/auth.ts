import { api } from '@/api/utils.ts';

export interface RegisterRequestData {
    email: string;
    password: string;
    name?: string;
    surname?: string;
}

export type LoginForm = {
    email: string;
    password: string;
};

export type LoginResponse = {
    token: string;
};

export const register = async (data: RegisterRequestData) => {
    return await api<string>(`/api/auth/register`, {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json',
        },
    });
};

export const login = async (data: LoginForm) => {
    return await api<LoginResponse>(`/api/auth/login`, {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json',
        },
    });
};

export const me = async (token: string) => {
    const response = await fetch(`/api/auth/me`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
    });
    return response.json();
};
