const API_URL = 'http://localhost:3000/api';

type RegisterForm = {
    email: string;
    password: string;
    name: string;
    surname: string;
}

type LoginForm = {
    email: string;
    password: string;
}

export const register = async (data: RegisterForm) => {
    const response = await fetch(`${API_URL}/auth/register`, {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    })
    return response.json()
}

export const login = async (data: LoginForm) => {
    const response = await fetch(`${API_URL}/auth/login`, {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    })
    return response.json()
}

export const me = async (token: string) => {
    const response = await fetch(`${API_URL}/auth/me`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    return response.text()
}