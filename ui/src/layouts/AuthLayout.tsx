import { Outlet } from 'react-router-dom';

export const AuthLayout = () => {
    return (
        <div className="flex min-h-[100svh] flex-col items-center justify-center gap-4 sm:gap-6 bg-muted p-4 sm:p-6 md:p-10">
            <div className="flex w-full max-w-sm flex-col gap-4 sm:gap-6">
                <a href="#" className="flex items-center gap-2 self-center font-medium text-lg sm:text-xl">
                    LinguaLearn
                </a>
                <Outlet />
            </div>
        </div>
    );
};
