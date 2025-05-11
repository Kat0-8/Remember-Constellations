import React, { useState } from 'react';
import { Modal, Space, message } from 'antd';
import ReactiveButton from 'reactive-button';

type Props = {
    open: boolean;
    id: number;
    type: string;
    onDelete?: (id: number) => Promise<void>;
    onRefresh?: () => void;
    onClose: () => void;
};


const ConfirmDeleteModal: React.FC<Props> = ({ open, id, type, onDelete, onRefresh, onClose }) => {
    const [loading, setLoading] = useState(false);

    const handleOk = async () => {
        setLoading(true);
        try {
            await onDelete?.(id);
            message.success(`${type} deleted successfully`);
            onRefresh?.();
            onClose?.();
        } catch (error) {
            message.error(`Deletion failed: ${error instanceof Error ? error.message : 'unknown error'}`);
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <Modal
                open={open}
                title={`Delete ${type}`}
                onCancel={onClose}
                footer={(
                    <Space>
                        <ReactiveButton
                            rounded
                            color="red"
                            idleText="Delete"
                            loadingText="Deleting..."
                            onClick={handleOk}
                            disabled={loading}
                        />
                        <ReactiveButton
                            outline
                            rounded
                            color={"secondary"}
                            idleText="Close"
                            onClick={onClose}
                            disabled={loading}
                        />
                    </Space>
                )}
            >
                Are you sure you want to delete this {type}?
            </Modal>
        </>
    );
};

export { ConfirmDeleteModal };
