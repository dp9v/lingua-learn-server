export class ValidationError extends Error {
    validationErrors: Record<string, string>;

    constructor(errors: Record<string, string>) {
        super('Validation failed');
        this.name = 'ValidationError';
        this.validationErrors = errors;
    }
}

export const api = async <T>(input: RequestInfo, init?: RequestInit): Promise<T> => {
    const response = await fetch(input, init);

    const contentType = response.headers.get('Content-Type') || '';

    if (!response.ok) {
        // Валидационные ошибки
        if ((response.status === 422 || response.status === 409) && contentType.includes('application/json')) {
            const errorData = await response.json();
            throw new ValidationError(errorData);
        }

        if (response.status === 401) {
            throw new Error('Invalid credentials');
        }

        // Ошибки с текстом
        const message = contentType.includes('application/json')
            ? ((await response.json()).message ?? 'Unknown error')
            : await response.text();

        throw new Error(message);
    }

    return contentType.includes('application/json') ? await response.json() : ((await response.text()) as T);
};
