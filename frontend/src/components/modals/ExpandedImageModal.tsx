import React from 'react';
import { Modal } from 'antd';

interface ImageModalProps {
    open: boolean;
    imageUrl: string | null;
    onClose: () => void;
}

const ExpandedImageModal: React.FC<ImageModalProps> = ({ open, imageUrl, onClose }) => {
    if (!imageUrl) return null;

    return (
        <Modal
            open={open}
        footer={null}
        onCancel={onClose}
        width={450}
        title="Image"
    >
    <img
        src={`http://localhost:8080/api/images/${imageUrl}`}
    alt="Expanded View"
    style={{ width: '100%' }}
    />
    </Modal>
);
};

export default ExpandedImageModal;
