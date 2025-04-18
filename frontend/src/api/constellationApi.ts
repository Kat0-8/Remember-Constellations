// src/api/constellationApi.ts
import { AxiosResponse } from 'axios';
import { ConstellationCriteria, ConstellationDto } from '../types/constellations';
import API from './axiosInstance.ts'

export interface PaginatedResponse<T> {
    content: T[];
    pageable: {
        pageNumber: number;
        pageSize: number;
    };
    totalElements: number;
}

export const constellationApi = {

    /* CREATE */

    create: (data: Omit<ConstellationDto, 'id'>): Promise<AxiosResponse<ConstellationDto>> =>
        API.post('/constellations', data),

    attachStars: (id: number, starIds: number[]): Promise<AxiosResponse<ConstellationDto>> =>
        API.post(`/constellations/${id}/attach-stars`, starIds),

    /* READ */

    getAll: (params?: ConstellationCriteria): Promise<AxiosResponse<PaginatedResponse<ConstellationDto>>> =>
        API.get('/constellations', { params }),

    getById: (id: number): Promise<AxiosResponse<ConstellationDto>> =>
        API.get(`/constellations/${id}`),

    getByStarType: (type: string): Promise<AxiosResponse<ConstellationDto[]>> =>
        API.get('/constellations/star-type', { params: { type } }),

    /* UPDATE */

    put: (id: number, data: ConstellationDto): Promise<AxiosResponse<ConstellationDto>> =>
        API.put(`/constellations/${id}`, data),

    patch: (id: number, data: Partial<ConstellationDto>): Promise<AxiosResponse<ConstellationDto>> =>
        API.patch(`/constellations/${id}`, data),

    /* DELETE */

    delete: (id: number): Promise<AxiosResponse<void>> =>
        API.delete(`/constellations/${id}`),
};

export { API };
