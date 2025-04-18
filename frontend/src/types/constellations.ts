import {StarDto} from "./stars.ts";

export interface ConstellationDto {
    id: number;
    name: string;
    abbreviation: string;
    family: string;
    region: string;
    stars?: StarDto[];
}

export type ConstellationCriteria = {
    name?: string;
    abbreviation?: string;
    family?: string;
    region?: string;

    page?: number;
    size?: number;
    sort?: string;
};

export type ConstellationSortField = 'name' | 'abbreviation' | 'family' | 'region';