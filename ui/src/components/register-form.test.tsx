import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { ValidationError } from '@/api/utils.ts';
import { RegisterForm } from '@/components/register-form.tsx';
import '@testing-library/jest-dom';

describe('RegisterForm', () => {
    const setup = (onFormSubmit = jest.fn()) => {
        render(<RegisterForm onFormSubmit={onFormSubmit} />);
        return {
            emailInput: screen.getByLabelText(/email/i),
            passwordInput: screen.getByLabelText(/password/i),
            nameInput: screen.getByLabelText('Name'),
            surnameInput: screen.getByLabelText('Surname'),
            submitButton: screen.getByRole('button', { name: /register/i }),
            onFormSubmit,
        };
    };

    test('renders all input fields', () => {
        const { emailInput, passwordInput, nameInput, surnameInput } = setup();
        expect(emailInput).toBeInTheDocument();
        expect(passwordInput).toBeInTheDocument();
        expect(nameInput).toBeInTheDocument();
        expect(surnameInput).toBeInTheDocument();
    });

    test('validates required fields and email format', async () => {
        const { emailInput, submitButton } = setup();

        // Try to submit with no inputs filled
        await userEvent.click(submitButton);

        expect(await screen.findAllByText(/required/i)).toHaveLength(2); // Required fields: email, password

        // Filling an invalid email format
        await userEvent.type(emailInput, 'invalid-email');
        await userEvent.click(submitButton);

        expect(await screen.findByText(/invalid email/i)).toBeInTheDocument();
    });

    test('calls onFormSubmit with valid data', async () => {
        const onFormSubmit = jest.fn().mockResolvedValue(undefined);
        const { emailInput, passwordInput, nameInput, surnameInput, submitButton } = setup(onFormSubmit);

        await userEvent.type(emailInput, 'test@example.com');
        await userEvent.type(passwordInput, 'supersecret');
        await userEvent.type(nameInput, 'John');
        await userEvent.type(surnameInput, 'Doe');

        fireEvent.click(submitButton);

        await waitFor(() =>
            expect(onFormSubmit).toHaveBeenCalledWith({
                email: 'test@example.com',
                password: 'supersecret',
                name: 'John',
                surname: 'Doe',
            })
        );
    });

    test('displays server-side validation errors', async () => {
        const error = new ValidationError({
            email: 'Email is already taken',
        });
        error.name = 'ValidationError';

        const onFormSubmit = jest.fn().mockRejectedValue(error);

        const { emailInput, passwordInput, submitButton } = setup(onFormSubmit);

        await userEvent.type(emailInput, 'test@example.com');
        await userEvent.type(passwordInput, 'supersecret');

        fireEvent.click(submitButton);

        expect(await screen.findByText(/email is already taken/i)).toBeInTheDocument();
    });
});
