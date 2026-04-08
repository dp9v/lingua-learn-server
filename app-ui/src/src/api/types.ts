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
    name: string;
}

export interface GetRandomWordsRequest {
    groups: number[];
    count: number;
}

export interface GetRandomWordsResponse {
    words: WordPair[];
}

export interface RegisterRequestData {
    email: string;
    password: string;
    name?: string;
    surname?: string;
}

export interface LoginForm {
    email: string;
    password: string;
}

export interface LoginResponse {
    token: string;
    expiresAt: string;
}
