import {useEffect, useState} from 'react';
import {StarCriteria, StarDto} from "../types/stars.ts";
import {starsApi} from "../api/starApi.ts";
import {StarsList} from "../components/lists/StarsList.tsx";
import {message} from "antd";

const StarsPage = () => {
    const [stars, setStars] = useState<StarDto[]>([]);
    const [loading, setLoading] = useState(true);
    const [filters, setFilters] = useState<StarCriteria>({});

    const loadStars = async (criteria: StarCriteria = {}) => {
        try {
            setLoading(true);
            const res = await starsApi.getAll(criteria);
            setStars(res.data);
        } catch (error) {
            if (error instanceof Error) {
                console.error("API Error:", error.message);
                message.error("Failed to load stars");
            }
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = (values: StarCriteria) => {
        setFilters(values);
        loadStars(values);
    };


    const handleDeleteStar = async (id: number) => {
        await starsApi.delete(id);
    };

    useEffect(() => {
        loadStars();
    }, []);

    return (
        <div style={{ padding: '24px', width: '99vw' }}>
            <h1>Stars Catalogue</h1>
            <StarsList
                stars={stars}
                loading={loading}
                onSearch={handleSearch}
                onDelete={handleDeleteStar}
                onRefresh={() => loadStars(filters)}
            />
        </div>
    );
};

export default StarsPage;