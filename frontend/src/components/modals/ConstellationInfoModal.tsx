import {Modal, Descriptions, Skeleton, Button} from 'antd';
import { ConstellationDto } from '../../types/constellations.ts';
import {useEffect, useState} from "react";
import {constellationApi} from "../../api/constellationApi.ts";

export const ConstellationInfoModal = ({
                                           constellationId,
                                           open,
                                           onClose,
                                       }: {
    constellationId: number | null;
    open: boolean;
    onClose: () => void;
}) => {
    const [constellation, setConstellation] = useState<ConstellationDto | null>(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (open && constellationId) {
            const loadData = async () => {
                try {
                    setLoading(true);
                    const response = await constellationApi.getById(constellationId);
                    setConstellation(response.data);
                } finally {
                    setLoading(false);
                }
            };
            loadData();
        }
    }, [open, constellationId]);

    return (
        <Modal
            title="Constellation Quick Info"
            open={open}
            onCancel={onClose}
            footer={[
                <Button key="close" onClick={onClose}>
                    Close
                </Button>,
            ]}
        >
            {loading ? (
                <Skeleton active />
            ) : (
                <Descriptions column={1} bordered>
                    <Descriptions.Item label="Name">{constellation?.name}</Descriptions.Item>
                    <Descriptions.Item label="Abbreviation">
                        {constellation?.abbreviation}
                    </Descriptions.Item>
                    <Descriptions.Item label="Family">{constellation?.family}</Descriptions.Item>
                    <Descriptions.Item label="Region">{constellation?.region}</Descriptions.Item>
                    <Descriptions.Item label="Number of Stars">
                        {constellation?.stars?.length || 0}
                    </Descriptions.Item>
                </Descriptions>
            )}
        </Modal>
    );
};