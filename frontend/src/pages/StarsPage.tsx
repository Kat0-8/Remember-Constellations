import { useState, useEffect } from 'react';
import {StarDto} from "../types/stars.ts";
import {starsApi} from "../api/starApi.ts";
import {StarsList} from "../components/lists/StarsList.tsx";
import {message} from "antd";

const StarsPage = () => {
    const [stars, setStars] = useState<StarDto[]>([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');

    const loadStars = async () => {
        try {
            setLoading(true);
            const res = await starsApi.getAll({ name: searchTerm });
            setStars(res.data);
        } catch (error) {
            if (error instanceof Error) {
                console.error("API Error:", error.message); // Log details [[1]]
                message.error("Failed to load stars");
            }
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id: number) => {
        await starsApi.delete(id);
    };

    useEffect(() => {
        loadStars();
    }, [searchTerm]);

    return (
        <div style={{ padding: '24px', width: '99vw' }}>
            <h1>Stars Catalog</h1>
            <StarsList
                stars={stars}
                loading={loading}
                searchTerm={searchTerm}
                onSearch={setSearchTerm}
                onDelete={handleDelete}
                onRefresh={loadStars}
            />
        </div>
    );
};

export default StarsPage;