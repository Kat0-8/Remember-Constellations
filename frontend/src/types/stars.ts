export interface StarDto {
    id: number;
    name: string;
    type: string;
    mass: number;
    radius: number;
    temperature: number;
    luminosity: number;
    rightAscension: number;
    declination: number;
    positionInConstellation?: string;
    imageUrl: string;
    constellationId?: number;
}

export type StarCriteria = {
    name?: string;
    type?: string;
    mass?: number;
    radius?: number;
    temperature?: number;
    luminosity?: number;
    rightAscension?: number;
    declination?: number;
    positionInConstellation?: string;
    constellationId?: number;

    page?: number;
    size?: number;
    sort?: string;
};