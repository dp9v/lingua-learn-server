import { api } from '@/api/utils.ts';

export interface Group {
    id: number;
    name: string;
    description: string;
    isPublic: boolean;
    wordsCount: number;
}

export interface Word {
    id?: number;
    text: string;
    language: string;
}

export interface WordPair {
    id?: number;
    from: Word;
    to: Word;
}

export interface GroupWithWords {
    group: Group;
    words: WordPair[];
}

export interface Language {
    code: string;
    fullName: string;
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
    return await api<GroupWithWords>(`/api/groups/${groupId}/words`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
    });
};

export const createGroup = async (name: string, description: string, token: string) => {
    return await api<number>(`/api/groups`, {
        method: 'POST',
        body: JSON.stringify({ name, description }),
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
    });
};

export const getAvailableLanguages = async (token: string) => {
    return await api<Language[]>(`/api/languages`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
    });
};

export const addWords = async (groupId: number, token: string, pairs: WordPair[]) => {
    return await api<void>(`/api/groups/${groupId}/words`, {
        method: 'PUT',
        body: JSON.stringify({ pairs }),
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
    });
};
