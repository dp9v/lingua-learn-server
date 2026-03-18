import { api } from '@/api/utils.ts';
import type { GetRandomWordsRequest, GetRandomWordsResponse } from '@/api/types.ts';

export const getRandomWords = async (request: GetRandomWordsRequest, token: string) => {
    const params = new URLSearchParams();
    request.groups.forEach((groupId) => params.append('groups', groupId.toString()));
    params.append('count', request.count.toString());

    return await api<GetRandomWordsResponse>(`/api/words/random?${params.toString()}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
    });
};
