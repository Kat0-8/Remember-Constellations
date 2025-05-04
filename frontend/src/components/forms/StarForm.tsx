import {Form, Input, InputNumber, Button, message, Space} from 'antd';
import { StarDto } from '../../types/stars';
import { starsApi } from '../../api/starApi.ts';
import {useState} from "react";
import '../../styles/custom-scrollbar.css'

interface StarFormProps {
    initialValues?: StarDto;
    onSuccess: () => void;
}

export const StarForm = ({ initialValues, onSuccess }: StarFormProps) => {
    const [form] = Form.useForm();
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (values: StarDto) => {
        try {
            setLoading(true);
            if (initialValues) {
                await starsApi.put(initialValues.id, values);
                message.success('Star updated successfully');
            } else {
                await starsApi.create(values);
                message.success('Star created successfully');
            }
            onSuccess();
        } catch (error) {
            if (error instanceof Error) {
                message.error(`Deletion failed : ${error.message}`);
            } else {
                message.error('Deletion failed: unknown error');
            }
        } finally {
            setLoading(false);
        }
    };

    return (

        <div className="custom-scrollbar" style={{ maxHeight: 'calc(100vh - 200px)', overflowY: 'auto'}}>
        <Form
            form={form}
            layout="vertical"
            initialValues={initialValues || {}}
            onFinish={handleSubmit}
        >
            <Form.Item name="name" label="Name" rules={[{ required: true, message: 'Please enter name' }]}>
                <Input style={{ width: '90%' }}/>
            </Form.Item>

            <Form.Item name="type" label="Type" rules={[{ required: true, message: 'Please enter type' }]}>
                <Input style={{ width: '90%' }}/>
            </Form.Item>

            <Form.Item name="mass" label="Mass" rules={[{ required: true, message: 'Please enter mass' }]}>
                <InputNumber min={0} style={{ width: '90%' }} />
            </Form.Item>

            <Form.Item name="radius" label="Radius" rules={[{ required: true, message: 'Please enter radius' }]}>
                <InputNumber min={0} style={{ width: '90%' }} />
            </Form.Item>

            <Form.Item
                name="temperature"
                label="Temperature"
                rules={[{ required: true, message: 'Please enter temperature' }]}
            >
                <InputNumber min={0} style={{ width: '90%' }} />
            </Form.Item>

            <Form.Item
                name="luminosity"
                label="Luminosity"
                rules={[{ required: true, message: 'Please enter luminosity' }]}
            >
                <InputNumber min={0} style={{ width: '90%' }} />
            </Form.Item>

            <Form.Item
                name="rightAscension"
                label="Right Ascension"
                rules={[{ required: true, message: 'Please enter right ascension' }]}
            >
                <InputNumber min={0} max={24} step={0.01} style={{ width: '90%' }} />
            </Form.Item>

            <Form.Item
                name="declination"
                label="Declination"
                rules={[{ required: true, message: 'Please enter declination' }]}
            >
                <InputNumber min={-90} max={90} step={0.01} style={{ width: '90%' }} />
            </Form.Item>

            <Form.Item name="positionInConstellation" label="Position in Constellation">
                <Input style={{ width: '90%' }}/>
            </Form.Item>

            <Space>
                <Button type="primary" htmlType="submit" loading={loading}>
                    {initialValues?.id ? 'Update Star' : 'Create Star'}
                </Button>
                <Button htmlType="button" onClick={() => form.resetFields()} disabled={loading}>
                    Reset
                </Button>
            </Space>

        </Form>
        </div>
    );
};