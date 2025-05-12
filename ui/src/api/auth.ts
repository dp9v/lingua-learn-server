export type RegisterRequestData = {
    email: string;
    password: string;
    name?: string;
    surname?: string;
};

export type LoginForm = {
    email: string;
    password: string;
};

export const register = async (data: RegisterRequestData) => {
    const response = await fetch(`/api/auth/register`, {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json',
        },
    });
    return response.text();
};

export const login = async (data: LoginForm) => {
    const response = await fetch(`/api/auth/login`, {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json',
        },
    });

    if (!response.ok) {
        if (response.status === 401) {
            throw new Error('Invalid credentials');
        }
    }

    return response.json();
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
