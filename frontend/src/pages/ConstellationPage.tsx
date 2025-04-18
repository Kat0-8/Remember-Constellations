import React, { useEffect, useState } from 'react';
import { message } from 'antd';
import { constellationApi } from '../api/constellationApi.ts';
import { ConstellationDto } from '../types/constellations';
import ConstellationList from '../components/ConstellationList.tsx';
import ConstellationForm from '../components/ConstellationForm.tsx';

const ConstellationPage: React.FC = () => {
    const [constellations, setConstellations] = useState<ConstellationDto[]>([]);
    const [editing, setEditing] = useState<ConstellationDto | null>(null);
    const [loading, setLoading] = useState(false);

    const loadData = async () => {
        try {
            const res = await constellationApi.getAll({ page: 0, size: 100 });
            setConstellations(res.data.content);
        } catch {
            message.error('Failed to load constellations');
        }
    };

    useEffect(() => {
        loadData();
    }, []);

    const handleSubmit = async (values: Omit<ConstellationDto, 'id'>) => {
        try {
            setLoading(true);
            if (editing) {
                await constellationApi.put(editing.id, { ...editing, ...values });
                message.success('Constellation updated');
            } else {
                await constellationApi.create(values);
                message.success('Constellation created');
            }
            setEditing(null);
            await loadData();
        } catch {
            message.error('Error saving constellation');
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id: number) => {
        try {
            await constellationApi.delete(id);
            message.success('Deleted');
            await loadData();
        } catch {
            message.error('Failed to delete');
        }
    };

    return (
        <div>
            <h2>Constellations</h2>
            <ConstellationForm
                initialValues={editing || undefined}
                onSubmit={handleSubmit}
                isSubmitting={loading}
            />
            <ConstellationList data={constellations} onDelete={handleDelete} />
        </div>
    );
};

export default ConstellationPage;
