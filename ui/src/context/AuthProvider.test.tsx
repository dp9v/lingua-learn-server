// AuthProvider.test.tsx
import { act, render, waitFor } from '@testing-library/react';
import { AuthProvider, useAuth } from './AuthContext';

jest.useFakeTimers();

describe('AuthProvider', () => {
    const TestComponent = () => {
        const { token, login, logout } = useAuth();

        return (
            <div>
                <div>Token: {token}</div>
                <button onClick={() => login('mock-token', new Date(Date.now() + 5000).toISOString())}>Login</button>
                <button onClick={logout}>Logout</button>
            </div>
        );
    };

    const renderWithProvider = () =>
        render(
            <AuthProvider>
                <TestComponent />
            </AuthProvider>
        );

    afterEach(() => {
        localStorage.clear();
        jest.clearAllTimers();
    });

    it('save token and expireAt when login', () => {
        const { getByText } = renderWithProvider();

        act(() => {
            getByText('Login').click();
        });

        expect(localStorage.getItem('token')).toBe('mock-token');
        expect(localStorage.getItem('expiresAt')).not.toBeNull();
    });

    it('call logout if token expired', () => {
        const expired = new Date(Date.now() - 1000).toISOString();
        localStorage.setItem('token', 'expired-token');
        localStorage.setItem('expiresAt', expired);

        const logoutSpy = jest.spyOn(Storage.prototype, 'removeItem');

        renderWithProvider();

        expect(logoutSpy).toHaveBeenCalledWith('token');
        expect(logoutSpy).toHaveBeenCalledWith('expiresAt');
    });

    it('start timer if login is valid', async () => {
        const future = new Date(Date.now() + 5000).toISOString();
        localStorage.setItem('token', 'valid-token');
        localStorage.setItem('expiresAt', future);

        renderWithProvider();

        // fast-forward 5 секунд
        act(() => {
            jest.advanceTimersByTime(5000);
        });

        await waitFor(() => {
            expect(localStorage.getItem('token')).toBeNull();
            expect(localStorage.getItem('expiresAt')).toBeNull();
        });
    });
});
