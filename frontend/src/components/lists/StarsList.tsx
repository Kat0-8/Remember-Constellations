import {List, Avatar, Modal, Space} from 'antd';
import {StarCriteria, StarDto} from '../../types/stars';
import {useEffect, useState} from 'react';
import {StarForm} from '../forms/StarForm';
import {ConstellationInfoModal} from '../modals/ConstellationInfoModal';
import {ViewConstellationButton} from "../buttons/ViewConstellationInfoButton.tsx";
import ReactiveButton from "reactive-button";
import {DeleteOutlined, EditOutlined} from "@ant-design/icons";
import Scrollbar from "react-scrollbars-custom";
import '../../styles/custom-scrollbar.css'
import '../../styles/blue-button.css'
import {ConfirmDeleteModal} from "../modals/ConfirmDeleteModal.tsx";
import ExpandedImageModal from "../modals/ExpandedImageModal.tsx";
import {useForm} from "antd/es/form/Form";

interface StarsListProps {
    stars: StarDto[];
    loading?: boolean;
    onSearch?: (value: StarCriteria) => void;
    onDelete?: (id: number) => Promise<void>;
    onRefresh?: () => void;
    hideButtons?: boolean;
}

export const StarsList = ({
                              stars,
                              loading,
                              onSearch,
                              onDelete,
                              onRefresh,
                              hideButtons
                          }: StarsListProps) => {
    const [selectedStar, setSelectedStar] = useState<StarDto | undefined>(undefined);
    const [isFormVisible, setIsFormVisible] = useState(false);
    const [selectedConstellationId, setSelectedConstellationId] = useState<number | null>(null);
    const [isConstellationModalOpen, setIsConstellationModalOpen] = useState(false);
    const [isConfirmDeleteModalVisible, setConfirmDeleteModalVisible] = useState(false);
    const [selectedToDeleteStarId, setSelectedToDeleteStarId] = useState<number>(0);
    const [filterVisible, setFilterVisible] = useState(false);
    const [currentFilters, setCurrentFilters] = useState<StarCriteria>({});
    const [selectedImageUrl, setSelectedImageUrl] = useState<string | null>(null);
    const [isImageModalVisible, setIsImageModalVisible] = useState(false);
    const [previewImage, setPreviewImage] = useState<string>('');
    const [editForm] = useForm();
    const [filterForm] = useForm();

    useEffect(() => {
        if (filterVisible) {
            //filterForm.resetFields();
            filterForm.setFieldsValue(currentFilters);
        }
    }, [filterVisible]);

    useEffect(() => {
        if (isFormVisible) {
            editForm.resetFields();
            if (selectedStar) {
                editForm.setFieldsValue(selectedStar);
            }
        }
    }, [isFormVisible, selectedStar]);

    const resetPreviewImage = () => {
        setPreviewImage('');
    };

    const handleFilter = (values: StarCriteria) => {
        onSearch?.(values);
        setFilterVisible(false);
    };

    const handleShowConfirmDeleteModal = (starId: number) => {
        setSelectedToDeleteStarId(starId);
        setConfirmDeleteModalVisible(true);
    };

    const handleAvatarClick = (imageUrl: string) => {
        setSelectedImageUrl(imageUrl);
        setIsImageModalVisible(true);
    };

    const handleViewConstellation = (constellationId: number) => {
        setSelectedConstellationId(constellationId);
        setIsConstellationModalOpen(true);
    };

    return (
        <div>
            {!hideButtons && (
                <Space style={{marginBottom: 16, width: '100%'}} direction="vertical">
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
                            idleText="Add new star"
                            size="medium"
                            onClick={() => {
                                setSelectedStar(undefined);
                                setIsFormVisible(true);
                            }}
                        />
                    </Space>
                </Space>
            )}

            <List
                loading={loading}
                dataSource={stars}
                renderItem={(star) => (
                    <List.Item
                        actions={[
                            <ReactiveButton
                                outline
                                rounded
                                color="primary | blue"
                                size="medium"
                                onClick={() => {
                                    setSelectedStar(star);
                                    setIsFormVisible(true);
                                }}
                                idleText={
                                    <span>
                                    <EditOutlined/>
                                    Edit
                                </span>}
                            />,

                            <ReactiveButton
                                outline
                                rounded
                                color="primary | red"
                                size="medium"
                                onClick={() => handleShowConfirmDeleteModal(star.id)}
                                idleText={
                                    <span>
                                    <DeleteOutlined/>
                                    Delete
                                </span>}
                            />
                            ,
                            !hideButtons && (
                                star.constellationId !== 0 && star.constellationId !== null ? (
                                    <ViewConstellationButton
                                        isDisabled={false}
                                        onOpen={() => handleViewConstellation(star.constellationId!)}
                                    />
                                ) : (
                                    <ViewConstellationButton onOpen={() => {}} isDisabled={true}/>
                                )
                            )
                        ]}
                    >
                        <List.Item.Meta
                            avatar={<Avatar
                                src={`/api/images/${star.imageUrl}`}
                                size={50}
                                onClick={() => handleAvatarClick(star.imageUrl)}
                                style={{ cursor: 'pointer' }}
                            />}
                            title={star.name}
                            description={`
                                Type: ${star.type} | 
                                Mass: ${star.mass}M⊙ |
                                Radius: ${star.radius}R⊙ |
                                Temperature: ${star.temperature}K |
                                Luminosity: ${star.luminosity}L⊙ |
                                Right ascension: ${star.rightAscension} |
                                Declination: ${star.declination} |
                                Position: ${star.positionInConstellation ? star.positionInConstellation : 'N/A'}
                            `}
                        />
                    </List.Item>
                )}
            />

            <Modal
                title="Filter Stars"
                open={filterVisible}
                onCancel={() => {
                    setFilterVisible(false);
                    filterForm.resetFields();
                }
                }
                footer={null}
                width={550}
            >
                <Scrollbar style={{width: '100%', height: '60vh'}}>
                <StarForm
                    form = {filterForm}
                    isFilter
                    initialValues={currentFilters}
                    onFilter={handleFilter}
                    onReset={() => {
                        onSearch?.({});
                        setCurrentFilters({});
                    }}
                    onSuccess={ () => {
                        setFilterVisible(false);
                    }}
                />
                </Scrollbar>
            </Modal>

            <Modal
                title={selectedStar ? "Edit Star" : "New Star"}
                open={isFormVisible}
                onCancel={() => {
                    setIsFormVisible(false);
                    setSelectedStar(undefined);
                    editForm.resetFields();
                    resetPreviewImage();
                }
                }
                footer={null}
                width={550}
                styles={{body: {maxHeight: '60vh'}}}
            >
                <Scrollbar style={{width: '100%', height: '60vh'}}>
                    <StarForm
                        form = {editForm}
                        key={selectedStar?.id || 'new'}
                        initialValues={selectedStar}
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
                onClose={() =>
                {
                    setIsImageModalVisible(false);
                    setSelectedImageUrl(null);
                }
            }
            />

            <ConstellationInfoModal
                key={selectedConstellationId || 'new'}
                constellationId={selectedConstellationId}
                open={isConstellationModalOpen}
                onClose={() => {
                    setIsConstellationModalOpen(false);
                    setSelectedConstellationId(null);
                }}
            />

            <ConfirmDeleteModal
                open={isConfirmDeleteModalVisible}
                id={selectedToDeleteStarId}
                type={'Star'}
                onDelete={onDelete}
                onRefresh={onRefresh}
                onClose={() => {
                    setConfirmDeleteModalVisible(false);
                    setSelectedToDeleteStarId(0);
                }}

            />
        </div>
    );
};