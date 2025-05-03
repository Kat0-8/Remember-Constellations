import React from 'react';
import { Form, Input, InputNumber, Button, Space } from 'antd';
import { StarDto } from '../../types/stars.ts';

interface Props {
    initialValues?: Partial<StarDto>; // allows use for both create (no ID) and update
    onSubmit: (values: Omit<StarDto, 'id'>) => void;
    isSubmitting?: boolean;
}

const StarForm: React.FC<Props> = ({ initialValues, onSubmit, isSubmitting }) => {
    const [form] = Form.useForm<Omit<StarDto, 'id'>>();

    const handleFinish = (values: Omit<StarDto, 'id'>) => {
        onSubmit(values);
    };

    return (
        <Form
            form={form}
            initialValues={initialValues}
            onFinish={handleFinish}
            layout="vertical"
            disabled={isSubmitting}
        >
            <Form.Item name="name" label="Name" rules={[{ required: true, message: 'Please enter name' }]}>
                <Input />
            </Form.Item>

            <Form.Item name="type" label="Type" rules={[{ required: true, message: 'Please enter type' }]}>
                <Input />
            </Form.Item>

            <Form.Item name="mass" label="Mass" rules={[{ required: true, message: 'Please enter mass' }]}>
                <InputNumber min={0} style={{ width: '100%' }} />
            </Form.Item>

            <Form.Item name="radius" label="Radius" rules={[{ required: true, message: 'Please enter radius' }]}>
                <InputNumber min={0} style={{ width: '100%' }} />
            </Form.Item>

            <Form.Item
                name="temperature"
                label="Temperature"
                rules={[{ required: true, message: 'Please enter temperature' }]}
            >
                <InputNumber min={0} style={{ width: '100%' }} />
            </Form.Item>

            <Form.Item
                name="luminosity"
                label="Luminosity"
                rules={[{ required: true, message: 'Please enter luminosity' }]}
            >
                <InputNumber min={0} style={{ width: '100%' }} />
            </Form.Item>

            <Form.Item
                name="rightAscension"
                label="Right Ascension"
                rules={[{ required: true, message: 'Please enter right ascension' }]}
            >
                <InputNumber min={0} max={24} step={0.01} style={{ width: '100%' }} />
            </Form.Item>

            <Form.Item
                name="declination"
                label="Declination"
                rules={[{ required: true, message: 'Please enter declination' }]}
            >
                <InputNumber min={-90} max={90} step={0.01} style={{ width: '100%' }} />
            </Form.Item>

            <Form.Item name="positionInConstellation" label="Position in Constellation">
                <Input />
            </Form.Item>

            <Space>
                <Button type="primary" htmlType="submit" loading={isSubmitting}>
                    {initialValues?.id ? 'Update Star' : 'Create Star'}
                </Button>
                <Button htmlType="button" onClick={() => form.resetFields()} disabled={isSubmitting}>
                    Reset
                </Button>
            </Space>
        </Form>
    );
};

export default StarForm;
