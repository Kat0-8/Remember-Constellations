import {List, Avatar, Modal, Space} from 'antd';
import {StarCriteria, StarDto} from '../../types/stars';
import {useState} from 'react';
import {StarForm} from '../forms/StarForm';
import {ConstellationInfoModal} from '../modals/ConstellationInfoModal';
import {ViewConstellationButton} from "../buttons/ViewConstellationInfoButton.tsx";
import ReactiveButton from "reactive-button";
import {DeleteOutlined, EditOutlined} from "@ant-design/icons";
import Scrollbar from "react-scrollbars-custom";
import '../../styles/custom-scrollbar.css'
import '../../styles/blue-button.css'
import {ConfirmDeleteModal} from "../modals/ConfirmDeleteModal.tsx";

interface StarsListProps {
    stars: StarDto[];
    loading?: boolean;
    searchTerm?: string;
    onSearch?: (value: StarCriteria) => void;
    onDelete?: (id: number) => Promise<void>;
    onRefresh?: () => void;
}

export const StarsList = ({
                              stars,
                              loading,
                              onSearch,
                              onDelete,
                              onRefresh
                          }: StarsListProps) => {
    const [selectedStar, setSelectedStar] = useState<StarDto | undefined>(undefined);
    const [isFormVisible, setIsFormVisible] = useState(false);
    const [selectedConstellationId, setSelectedConstellationId] = useState<number | null>(null);
    const [isConstellationModalOpen, setIsConstellationModalOpen] = useState(false);
    const [isConfirmDeleteModalVisible, setConfirmDeleteModalVisible] = useState(false);
    const [selectedToDeleteStarId, setSelectedToDeleteStarId] = useState<number>(0);
    const [filterVisible, setFilterVisible] = useState(false);
    const [currentFilters, setCurrentFilters] = useState<StarCriteria>({});

    const handleFilter = (values: StarCriteria) => {
        onSearch?.(values);
        setFilterVisible(false);
    };

    const handleShowConfirmDeleteModal = (starId: number) => {
        setSelectedToDeleteStarId(starId);
        setConfirmDeleteModalVisible(true);
    };

    const handleViewConstellation = (constellationId: number) => {
        setSelectedConstellationId(constellationId);
        setIsConstellationModalOpen(true);
    };

    return (
        <div>
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
                            star.constellationId !== 0 && star.constellationId !== null ? (
                                <ViewConstellationButton
                                    isDisabled={false}
                                    onOpen={() => handleViewConstellation(star.constellationId!)}
                                />
                            ) : (
                                <ViewConstellationButton onOpen={() => {
                                }} isDisabled={true}/>
                            )
                        ]}
                    >
                        <List.Item.Meta
                            avatar={<Avatar src={star.imageUrl} size="large"/>}
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
                title="Filter Stars"
                open={filterVisible}
                onCancel={() => setFilterVisible(false)}
                footer={null}
                width={550}
            >
                <Scrollbar style={{width: '100%', height: '60vh'}}>
                <StarForm
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
                }
                }
                footer={null}
                width={550}
                styles={{body: {maxHeight: '60vh'}}}
            >
                <Scrollbar style={{width: '100%', height: '60vh'}}>
                    <StarForm
                        key={selectedStar?.id || 'new'}
                        initialValues={selectedStar}
                        onSuccess={() => {
                            setIsFormVisible(false);
                            onRefresh?.();
                        }}
                    />
                </Scrollbar>
            </Modal>

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
                key={selectedToDeleteStarId || 'new'}
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