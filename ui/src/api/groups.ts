import { api } from '@/api/utils.ts';

export interface Group {
    id: number;
    name: string;
    description: string;
    isPublic: boolean;
    wordsCount: number;
}

export const getUserGroups = async (token: string) => {
    return await api<Group[]>(`api/groups`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
    });
};
