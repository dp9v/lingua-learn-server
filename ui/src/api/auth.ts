export type RegisterRequiestData = {
    email: string;
    password: string;
    name: string;
    surname: string;
};

export type LoginForm = {
    email: string;
    password: string;
};

export const register = async (data: RegisterRequiestData) => {
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
