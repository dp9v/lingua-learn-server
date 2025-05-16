import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { LoginForm } from '@/components/login-form'; // Adjust the path to where your LoginForm is located
import '@testing-library/jest-dom';

describe('LoginForm', () => {
    const setup = (onFormSubmit = jest.fn()) => {
        render(<LoginForm onFormSubmit={onFormSubmit} />);
        return {
            emailInput: screen.getByLabelText(/email/i),
            passwordInput: screen.getByLabelText(/password/i),
            submitButton: screen.getByRole('button', { name: /login/i }),
            onFormSubmit,
        };
    };

    test('renders all input fields and submit button', () => {
        const { emailInput, passwordInput, submitButton } = setup();
        expect(emailInput).toBeInTheDocument();
        expect(passwordInput).toBeInTheDocument();
        expect(submitButton).toBeInTheDocument();
    });

    test('validates required fields', async () => {
        const { submitButton } = setup();

        // Try to submit without filling any fields
        await userEvent.click(submitButton);

        // Ensure validation errors appear
        expect(await screen.findAllByText(/Required/i)).toHaveLength(2);
    });

    test('validates email format', async () => {
        const { emailInput, submitButton } = setup();

        await userEvent.type(emailInput, 'invalid-email');
        await userEvent.click(submitButton);

        expect(await screen.findByText(/invalid email/i)).toBeInTheDocument();
    });

    test('calls onFormSubmit with correct data', async () => {
        const onFormSubmit = jest.fn().mockResolvedValue(undefined);
        const { emailInput, passwordInput, submitButton } = setup(onFormSubmit);

        await userEvent.type(emailInput, 'test@example.com');
        await userEvent.type(passwordInput, 'supersecret');

        fireEvent.click(submitButton);

        await waitFor(() =>
            expect(onFormSubmit).toHaveBeenCalledWith({
                email: 'test@example.com',
                password: 'supersecret',
            })
        );
    });
});
