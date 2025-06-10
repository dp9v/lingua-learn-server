import { api } from '@/api/utils.ts';

export interface Group {
    id: number;
    name: string;
    description: string;
    isPublic: boolean;
    wordsCount: number;
}

export interface Word {
    id: number;
    text: string;
    language: string;
}

export interface WordPair {
    id: number;
    from: Word;
    to: Word;
}

export const getUserGroups = async (token: string) => {
    return await api<Group[]>(`/api/groups`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
    });
};

export const getGroupWords = async (groupId: number, token: string) => {
    return await api<WordPair[]>(`/api/groups/${groupId}/words`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
    });
};
