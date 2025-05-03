import React, { useEffect, useState } from 'react';
import { message } from 'antd';
import { starsApi } from '../api/starApi.ts';
import { StarDto } from '../types/stars';
import StarList from '../components/lists/StarList.tsx';
import StarForm from '../components/forms/StarForm.tsx';
import {ViewConstellationButton} from "../components/buttons/ViewConstellationInfoButton.tsx";
import {ConstellationInfoModal} from "../components/modals/ConstellationInfoModal.tsx";

const StarsPage: React.FC = () => {
    const [stars, setStars] = useState<StarDto[]>([]);
    const [editing, setEditing] = useState<StarDto | null>(null);
    const [loading, setLoading] = useState(false);
    const [isConstellationInfoModalOpen, setIsConstellationInfoModalOpen] = useState(false);
    const [selectedConstellationId, setSelectedConstellationId] = useState<number | null>(null);

    const loadData = async () => {
        try {
            const res = await starsApi.getAll({ page: 0, size: 100 });
            setStars(res.data.content);
        } catch {
            message.error('Failed to load stars');
        }
    };

    useEffect(() => {
        loadData();
    }, []);

    const handleOpenConstellationInfoModal = (constellationId: number) => {
        setSelectedConstellationId(constellationId);
        setIsConstellationInfoModalOpen(true);
    }

    const handleSubmit = async (values: Omit<StarDto, 'id'>) => {
        try {
            setLoading(true);
            if (editing) {
                await starsApi.put(editing.id, { ...editing, ...values });
                message.success('Star updated');
            } else {
                await starsApi.create(values);
                message.success('Star created');
            }
            setEditing(null);
            await loadData();
        } catch {
            message.error('Error saving star');
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id: number) => {
        try {
            await starsApi.delete(id);
            message.success('Deleted');
            await loadData();
        } catch {
            message.error('Failed to delete');
        }
    };

    return (
       // <div style = {{ width: '100vw'}}>
        <div>
            <h2>Stars</h2>
            <ViewConstellationButton onOpen={() => handleOpenConstellationInfoModal(1)}/>
            <ConstellationInfoModal
                constellationId={selectedConstellationId}
                open={isConstellationInfoModalOpen}
                onClose={() => setIsConstellationInfoModalOpen(false)} />
            <StarForm
                initialValues={editing || undefined}
                onSubmit={handleSubmit}
                isSubmitting={loading}
            />
            <StarList data={stars} onDelete={handleDelete} />
        </div>
    );
};

export default StarsPage;
