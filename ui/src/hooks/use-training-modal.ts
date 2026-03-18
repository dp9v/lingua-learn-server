import { useState } from 'react';

/**
 * Hook for managing training modal state
 * Can be used on any page of the application
 */
export function useTrainingModal() {
    const [isOpen, setIsOpen] = useState(false);

    const openTraining = () => {
        setIsOpen(true);
    };

    const closeTraining = () => {
        setIsOpen(false);
    };

    return { isOpen, openTraining, closeTraining };
}
