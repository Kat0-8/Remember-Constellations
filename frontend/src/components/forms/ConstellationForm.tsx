import { Form, Input, Button, Space } from 'antd';
import { ConstellationDto } from '../../types/constellations.ts';
import * as React from "react";

interface Props {
    initialValues?: ConstellationDto;
    onSubmit: (values: ConstellationDto) => void;
    isSubmitting?: boolean;
}

const ConstellationForm: React.FC<Props> = ({ initialValues, onSubmit }) => {
    const [form] = Form.useForm();

    const handleFinish = (values: ConstellationDto) => {
        onSubmit(values);
    };

    return (
        <Form
            form={form}
            initialValues={initialValues}
            onFinish={handleFinish}
            layout="vertical"
        >
            <Form.Item label="Name" name="name" rules={[{ required: true }]}>
                <Input />
            </Form.Item>
            <Form.Item label="Abbreviation" name="abbreviation" rules={[{ required: true }]}>
                <Input />
            </Form.Item>
            <Form.Item label="Family" name="family" rules={[{ required: true }]}>
                <Input />
            </Form.Item>
            <Form.Item label="Region" name="region" rules={[{ required: true }]}>
                <Input />
            </Form.Item>
            <Space>
                <Button type="primary" htmlType="submit">
                    {initialValues ? 'Update' : 'Create'}
                </Button>
                <Button onClick={() => form.resetFields()}>Reset</Button>
            </Space>
        </Form>
    );
};

export default ConstellationForm;
