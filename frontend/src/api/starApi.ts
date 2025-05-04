// src/api/starsApi.ts
import { AxiosResponse } from 'axios';
import { StarCriteria, StarDto } from '../types/stars';
import API from './axiosInstance';

//import { PaginatedResponse } from './constellationApi'; // Or move this to a shared `types/api.ts` file

export const starsApi = {

    /* CREATE */

    create: (data: Omit<StarDto, 'id'>): Promise<AxiosResponse<StarDto>> =>
        API.post('/stars', data),

    createBulk: (data: Omit<StarDto, 'id'>[]): Promise<AxiosResponse<StarDto[]>> =>
        API.post('/stars/bulk', data),

    /* READ */

    getAll: (params?: StarCriteria): Promise<AxiosResponse< StarDto[] >> =>
        API.get('/stars', { params }),

    getById: (id: number): Promise<AxiosResponse<StarDto>> =>
        API.get(`/stars/${id}`),

    /* UPDATE */

    put: (id: number, data: StarDto): Promise<AxiosResponse<StarDto>> =>
        API.put(`/stars/${id}`, data),

    patch: (id: number, data: Partial<StarDto>): Promise<AxiosResponse<StarDto>> =>
        API.patch(`/stars/${id}`, data),

    /* DELETE */

    delete: (id: number): Promise<AxiosResponse<void>> =>
        API.delete(`/stars/${id}`),
};
