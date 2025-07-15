import {useEffect, useState} from 'react';
import {Dialog, DialogContent, DialogHeader, DialogTitle} from './ui/dialog';
import {Input} from './ui/input';
import {Label} from './ui/label';
import {Textarea} from './ui/textarea';
import {Button} from './ui/button';
import {createGroup} from '@/api/groups';
import {ValidationError} from '@/api/utils';

interface CreateGroupModalProps {
    open: boolean;
    onClose: () => void;
    onGroupCreated: () => void;
    token: string;
}

export function CreateGroupModal({ open, onClose, onGroupCreated, token }: CreateGroupModalProps) {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [fieldErrors, setFieldErrors] = useState<{ name?: string }>({});

    const resetForm = () => {
        setName('');
        setDescription('');
        setError('');
        setFieldErrors({});
    };

    useEffect(() => {
        if (!open) {
            resetForm();
        }
    }, [open]);

    const onSubmit = async () => {
        setFieldErrors({});
        setError('');
        try {
            setLoading(true);
            await createGroup(name, description, token);
            onGroupCreated();
            onClose();
            setName('');
            setDescription('');
        } catch (error) {
            if (error instanceof ValidationError) {
                setFieldErrors(error.validationErrors);
            } else {
                setError('Ошибка при создании группы');
            }
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <Dialog open={open} onOpenChange={onClose}>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>Создать новую группу</DialogTitle>
                </DialogHeader>
                <div className="space-y-6">
                    <div className="space-y-2">
                        <Label htmlFor="name">Название</Label>
                        <Input
                            id="name"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            className={fieldErrors.name ? 'border-red-500' : ''}
                        />
                        {fieldErrors.name && <p className="text-sm text-red-500 mt-1">{fieldErrors.name}</p>}
                    </div>

                    <div className="space-y-2">
                        <Label htmlFor="description">Описание</Label>
                        <Textarea
                            id="description"
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                        />
                    </div>

                    {error && <p className="text-red-500 text-sm">{error}</p>}

                    <Button onClick={onSubmit} disabled={loading}>
                        {loading ? 'Создание...' : 'Создать'}
                    </Button>
                </div>
            </DialogContent>
        </Dialog>
    );
}
