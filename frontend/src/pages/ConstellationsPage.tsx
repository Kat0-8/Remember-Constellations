import {useEffect, useState} from 'react';
import {ConstellationsList} from "../components/lists/ConstellationList.tsx";
import {message} from "antd";
import {ConstellationCriteria, ConstellationDto} from "../types/constellations.ts";
import {constellationApi} from "../api/constellationApi.ts";

const StarsPage = () => {
    const [constellations, setConstellations] = useState<ConstellationDto[]>([]);
    const [loading, setLoading] = useState(true);
    const [filters, setFilters] = useState<ConstellationCriteria>({});

    const loadConstellations = async (criteria: ConstellationCriteria = {}) => {
        try {
            setLoading(true);
            const res = await constellationApi.getAll(criteria);
            setConstellations(res.data);
        } catch (error) {
            if (error instanceof Error) {
                console.error("API Error:", error.message);
                message.error("Failed to load constellations");
            }
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = (values: ConstellationCriteria) => {
        setFilters(values);
        loadConstellations(values);
    };


    const handleDeleteConstellation = async (id: number) => {
        await constellationApi.delete(id);
    };

    useEffect(() => {
        loadConstellations();
    }, []);

    return (
        <div style={{ padding: '24px', width: '99vw' }}>
            <h1>Constellations Catalogue</h1>
            <ConstellationsList
               constellations={constellations}
               loading={loading}
               onSearch={handleSearch}
               onDelete={handleDeleteConstellation}
               onRefresh={()=>loadConstellations(filters)}
            />
        </div>
    );
};

export default StarsPage;