import { List, Avatar, Modal, Space } from 'antd';
import {ConstellationCriteria, ConstellationDto} from '../../types/constellations';
import {useEffect, useState} from 'react';
import { ConstellationForm } from '../forms/ConstellationForm';
import ReactiveButton from "reactive-button";
import { DeleteOutlined, EditOutlined, StarOutlined } from "@ant-design/icons";
import Scrollbar from "react-scrollbars-custom";
import '../../styles/custom-scrollbar.css';
import '../../styles/blue-button.css';
import { ConfirmDeleteModal } from "../modals/ConfirmDeleteModal.tsx";
import { useForm } from "antd/es/form/Form";
import ExpandedImageModal from "../modals/ExpandedImageModal.tsx";
import {StarsList} from "./StarsList.tsx";
import {starsApi} from "../../api/starApi.ts";

interface ConstellationsListProps {
    constellations: ConstellationDto[];
    loading?: boolean;
    onSearch?: (value: ConstellationCriteria) => void;
    onDelete?: (id: number) => Promise<void>;
    onRefresh?: () => void;
}

export const ConstellationsList = ({
                                       constellations,
                                       loading,
                                       onSearch,
                                       onDelete,
                                       onRefresh
                                   }: ConstellationsListProps) => {
    const [selectedConstellation, setSelectedConstellation] = useState<ConstellationDto | undefined>(undefined);
    const [isFormVisible, setIsFormVisible] = useState(false);
    const [isConfirmDeleteModalVisible, setConfirmDeleteModalVisible] = useState(false);
    const [selectedToDeleteId, setSelectedToDeleteId] = useState<number>(0);
    const [filterVisible, setFilterVisible] = useState(false);
    const [currentFilters, setCurrentFilters] = useState<ConstellationCriteria>({});
    const [selectedImageUrl, setSelectedImageUrl] = useState<string | null>(null);
    const [isImageModalVisible, setIsImageModalVisible] = useState(false);
    const [previewImage, setPreviewImage] = useState<string>('');
    const [isStarsListVisible, setIsStarsListVisible] = useState(false);
    const [selectedConstellationStarsId, setSelectedConstellationStarsId] = useState<number | null>(null);
    const [editForm] = useForm();
    const [filterForm] = useForm();

    const handleDeleteStar = async (id: number) => {
        await starsApi.delete(id);
    };

    useEffect(() => {
        if (filterVisible) {
            //filterForm.resetFields();
            filterForm.setFieldsValue(currentFilters);
        }
    }, [filterVisible]);

    useEffect(() => {
        if (isFormVisible) {
            editForm.resetFields();
            if (selectedConstellation) {
                editForm.setFieldsValue(selectedConstellation);
            }
        }
    }, [isFormVisible, selectedConstellation]);

    const resetPreviewImage = () => {
        setPreviewImage('');
    };

    const handleFilter = (values: ConstellationCriteria) => {
        onSearch?.(values);
        setFilterVisible(false);
    };

    const handleShowConfirmDeleteModal = (id: number) => {
        setSelectedToDeleteId(id);
        setConfirmDeleteModalVisible(true);
    };

    const handleAvatarClick = (imageUrl: string) => {
        setSelectedImageUrl(imageUrl);
        setIsImageModalVisible(true);
    };

    return (
        <div>
            <Space style={{ marginBottom: 16, width: '100%' }} direction="vertical">
                <Space>
                    <ReactiveButton
                        className="blue-button"
                        rounded
                        idleText="Filters"
                        size="medium"
                        onClick={() => setFilterVisible(true)}
                    />
                    <ReactiveButton
                        className="blue-button"
                        rounded
                        idleText="Add New Constellation"
                        size="medium"
                        onClick={() => {
                            setSelectedConstellation(undefined);
                            setIsFormVisible(true);
                        }}
                    />
                </Space>
            </Space>

            <List
                loading={loading}
                dataSource={constellations}
                renderItem={(constellation) => (
                    <List.Item
                        actions={[
                            <ReactiveButton
                                outline
                                rounded
                                color="primary | blue"
                                size="medium"
                                onClick={() => {
                                    setSelectedConstellation(constellation);
                                    setIsFormVisible(true);
                                }}
                                idleText={
                                    <span>
                                        <EditOutlined />
                                        Edit
                                    </span>}
                            />,
                            <ReactiveButton
                                outline
                                rounded
                                color="primary | red"
                                size="medium"
                                onClick={() => handleShowConfirmDeleteModal(constellation.id)}
                                idleText={
                                    <span>
                                        <DeleteOutlined />
                                        Delete
                                    </span>}
                            />,
                            <ReactiveButton
                                outline
                                rounded
                                color="secondary"
                                size="medium"
                                onClick={() => {
                                    setSelectedConstellationStarsId(constellation.id);
                                    setIsStarsListVisible(true);
                                }}
                                idleText={
                                    <span>
                                        <StarOutlined />
                                        Stars ({constellation.stars?.length || 0})
                                    </span>}
                                disabled={!constellation.stars?.length}
                            />
                        ]}
                    >
                        <List.Item.Meta
                            avatar={<Avatar
                                src={`/api/images/${constellation.imageUrl}`}
                                size={50}
                                onClick={() => handleAvatarClick(constellation.imageUrl)}
                                style={{ cursor: 'pointer'}}
                            />}
                            title={constellation.name}
                            description={`
                                Abbreviation: ${constellation.abbreviation} |
                                Family: ${constellation.family} | 
                                Region: ${constellation.region} |
                                Stars: ${constellation.stars?.length || 0}
                            `}
                        />
                    </List.Item>
                )}
            />
            <Modal
                title={`Stars in ${constellations.find(c => c.id === selectedConstellationStarsId)?.name || 'Constellation'}`}
                open={isStarsListVisible}
                onCancel={() => {
                    setIsStarsListVisible(false);
                    setSelectedConstellationStarsId(null);
                }}
                footer={null}
                width={800}
                destroyOnHidden
            >
                <Scrollbar className="custom-scrollbar" style={{ width: '100%', height: '60vh' }}>
                    <StarsList
                        stars={constellations.find(c => c.id === selectedConstellationStarsId)?.stars || []}
                        onRefresh={onRefresh}
                        onDelete={handleDeleteStar}
                        hideButtons={true}
                    />
                </Scrollbar>
            </Modal>

            <Modal
                title="Filter Constellations"
                open={filterVisible}
                onCancel={() => {
                    setFilterVisible(false);
                    filterForm.resetFields();
                }}
                footer={null}
                width={550}
            >
                <Scrollbar style={{ width: '100%', height: '50vh' }}>
                    <ConstellationForm
                        form={filterForm}
                        isFilter
                        initialValues={currentFilters}
                        onFilter={handleFilter}
                        onReset={() => {
                            onSearch?.({});
                            setCurrentFilters({});
                        }}
                        onSuccess={() => setFilterVisible(false)}
                    />
                </Scrollbar>
            </Modal>

            <Modal
                title={selectedConstellation ? "Edit Constellation" : "New Constellation"}
                open={isFormVisible}
                onCancel={() => {
                    setIsFormVisible(false);
                    setSelectedConstellation(undefined);
                    editForm.resetFields();
                    resetPreviewImage();
                }}
                footer={null}
                width={550}
                styles={{ body: { maxHeight: '60vh' } }}
            >
                <Scrollbar style={{ width: '100%', height: '50vh' }}>
                    <ConstellationForm
                        form={editForm}
                        key={selectedConstellation?.id || 'new'}
                        initialValues={selectedConstellation}
                        onSuccess={() => {
                            setIsFormVisible(false);
                            onRefresh?.();
                            resetPreviewImage();
                        }}
                        previewImage={previewImage}
                        setPreviewImage={setPreviewImage}
                    />
                </Scrollbar>
            </Modal>

            <ExpandedImageModal
                key={selectedImageUrl || 'new'}
                open={isImageModalVisible}
                imageUrl={selectedImageUrl}
                onClose={() => {
                    setIsImageModalVisible(false);
                    setSelectedImageUrl(null);
                }}
            />

            <ConfirmDeleteModal
                open={isConfirmDeleteModalVisible}
                id={selectedToDeleteId}
                type={'Constellation'}
                onDelete={onDelete}
                onRefresh={onRefresh}
                onClose={() => {
                    setConfirmDeleteModalVisible(false);
                    setSelectedToDeleteId(0);
                }}
            />
        </div>
    );
};