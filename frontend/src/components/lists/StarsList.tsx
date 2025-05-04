import { List, Button, Avatar, Input, Modal, Space, message } from 'antd';
import { StarDto } from '../../types/stars';
import { useState } from 'react';
import { StarForm } from '../forms/StarForm';
import { ConstellationInfoModal } from '../modals/ConstellationInfoModal';
import {ViewConstellationButton} from "../buttons/ViewConstellationInfoButton.tsx";

interface StarsListProps {
    stars: StarDto[];
    loading?: boolean;
    searchTerm?: string;
    onSearch?: (value: string) => void;
    onDelete?: (id: number) => Promise<void>;
    onRefresh?: () => void;
}

export const StarsList = ({
                              stars,
                              loading,
                              searchTerm,
                              onSearch,
                              onDelete,
                              onRefresh
                          }: StarsListProps) => {
    const [selectedStar, setSelectedStar] = useState<StarDto | undefined>(undefined);
    const [isFormVisible, setIsFormVisible] = useState(false);
    const [selectedConstellationId, setSelectedConstellationId] = useState<number | null>(null);
    const [isConstellationModalOpen, setIsConstellationModalOpen] = useState(false);

    const handleDelete = async (id: number) => {
        Modal.confirm({
            title: 'Delete Star',
            content: 'Are you sure you want to delete this star?',
            onOk: async () => {
                try {
                    await onDelete?.(id);
                    message.success('Star deleted successfully');
                    onRefresh?.();
                } catch (error) {
                    if (error instanceof Error) {
                        message.error(`Deletion failed : ${error.message}`);
                    } else {
                        message.error('Deletion failed: unknown error');
                    }

                }
            }
        });
    };

    const handleViewConstellation = (constellationId: number) => {
        setSelectedConstellationId(constellationId);
        setIsConstellationModalOpen(true);
    };

    return (
        <div>
            <Space style={{ marginBottom: 16, width: '100%' }} direction="vertical">
                <Input.Search
                    placeholder="Search stars..."
                    value={searchTerm}
                    onChange={(e: { target: { value: string; }; }) => onSearch?.(e.target.value)}
                    enterButton
                />

                <Button
                    type="primary"
                    onClick={() => {
                        setSelectedStar(undefined);
                        setIsFormVisible(true);
                    }}
                >
                    Add New Star
                </Button>
            </Space>

            <List
                loading={loading}
                dataSource={stars}
                renderItem={(star) => (
                    <List.Item
                        actions={[
                            <Button type="link" onClick={() => {
                                setSelectedStar(star);
                                setIsFormVisible(true);
                            }}>
                                Edit
                            </Button>,
                            <Button type="link" danger onClick={() => handleDelete(star.id)}>
                                Delete
                            </Button>,
                            star.constellationId && (
                                <ViewConstellationButton
                                    onOpen={() => handleViewConstellation(star.constellationId!)}
                                />
                            )
                        ]}
                    >
                        <List.Item.Meta
                            avatar={<Avatar src={star.imageUrl} size="large" />}
                            title={star.name}
                            description={`
                                Type: ${star.type} | 
                                Mass: ${star.mass}M⊙ |
                                Radius: ${star.radius}R⊙ |
                                Temperature: ${star.temperature}K |
                                Luminosity: ${star.luminosity}L⊙ |
                                Right ascension: ${star.rightAscension} |
                                Declination: ${star.declination} |
                                Position: ${star.positionInConstellation ? star.positionInConstellation : 'N/A'} |
                            `}
                        />
                    </List.Item>
                )}
            />

            <Modal
                title={selectedStar ? "Edit Star" : "New Star"}
                open={isFormVisible}
                onCancel={() => setIsFormVisible(false)}
                footer={null}
                width={600}
            >
                <StarForm
                    initialValues={selectedStar}
                    onSuccess={() => {
                        setIsFormVisible(false);
                        onRefresh?.();
                    }}
                />
            </Modal>

            <ConstellationInfoModal
                constellationId={selectedConstellationId}
                open={isConstellationModalOpen}
                onClose={() => {
                    setIsConstellationModalOpen(false);
                    setSelectedConstellationId(null);
                }}
            />
        </div>
    );
};