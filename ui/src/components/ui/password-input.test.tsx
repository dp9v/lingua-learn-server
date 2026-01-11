import { render, screen, fireEvent } from '@testing-library/react';
import { PasswordInput } from '@/components/ui/password-input';
import '@testing-library/jest-dom';

describe('PasswordInput', () => {
    test('renders password input with default type password', () => {
        const { container } = render(<PasswordInput name="password" />);
        const input = container.querySelector('input[name="password"]') as HTMLInputElement;

        expect(input).toBeInTheDocument();
        expect(input).toHaveAttribute('type', 'password');
    });

    test('toggles password visibility when eye button is clicked', () => {
        const { container } = render(<PasswordInput name="password" />);
        const input = container.querySelector('input[name="password"]') as HTMLInputElement;
        const toggleButton = screen.getByRole('button');

        // Initially should be password type
        expect(input).toHaveAttribute('type', 'password');

        // Click to show password
        fireEvent.click(toggleButton);
        expect(input).toHaveAttribute('type', 'text');

        // Click to hide password again
        fireEvent.click(toggleButton);
        expect(input).toHaveAttribute('type', 'password');
    });

    test('accepts and applies custom className', () => {
        const { container } = render(<PasswordInput name="password" className="custom-class" />);
        const input = container.querySelector('input[name="password"]') as HTMLInputElement;

        expect(input).toHaveClass('custom-class');
    });

    test('passes through other input props', () => {
        const { container } = render(<PasswordInput name="password" placeholder="Enter password" disabled />);
        const input = container.querySelector('input[name="password"]') as HTMLInputElement;

        expect(input).toHaveAttribute('placeholder', 'Enter password');
        expect(input).toBeDisabled();
    });
});
