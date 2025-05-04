import { useParams } from 'react-router-dom';
import { Descriptions, Spin, Alert } from 'antd';
import { useEffect, useState } from 'react';
import { StarDto } from '../types/stars';
import { starsApi } from '../api/starApi';

const StarPage = () => {
    const { id } = useParams();
    const [star, setStar] = useState<StarDto | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchStar = async () => {
            try {
                const res = await starsApi.getById(Number(id));
                setStar(res.data);
            } catch (error) {
                const errorMessage = error instanceof Error ? error.message : 'Unknown error';
                setError(`Failed to load star details: ${errorMessage}`);
            } finally {
                setLoading(false);
            }
        };

        fetchStar();
    }, [id]);

    if (loading) return <Spin />;

    if (error) return <Alert message={error} type="error" />;

    if (!star) return <Alert message="Star not found." type="warning" />;

    return (
        <div>

            <Descriptions title="Star Details" bordered style={{ marginTop: 24 }}>
                <Descriptions.Item label="Name">{star.name}</Descriptions.Item>
                <Descriptions.Item label="Type">{star.type}</Descriptions.Item>
                <Descriptions.Item label="Mass">{star.mass} Solar Masses</Descriptions.Item>
                <Descriptions.Item label="Radius">{star.radius} Solar Radii</Descriptions.Item>
                <Descriptions.Item label="Temperature">{star.temperature} K</Descriptions.Item>
                <Descriptions.Item label="Luminosity">{star.luminosity} Solar Luminosity</Descriptions.Item>
                <Descriptions.Item label="Right Ascension">{star.rightAscension}</Descriptions.Item>
                <Descriptions.Item label="Declination">{star.declination}</Descriptions.Item>
                <Descriptions.Item label="Position in Constellation">
                    {star.positionInConstellation || 'N/A'}
                </Descriptions.Item>
            </Descriptions>
        </div>
    );
};

export default StarPage;