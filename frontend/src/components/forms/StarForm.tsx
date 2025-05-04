import {Form, Input, InputNumber, message, Select, Space} from 'antd';
import { StarDto } from '../../types/stars';
import { starsApi } from '../../api/starApi.ts';
import {useEffect, useState} from "react";
import ReactiveButton from "reactive-button";
import Scrollbar from "react-scrollbars-custom";
import '../../styles/custom-scrollbar.css'

interface StarFormProps {
    initialValues?: StarDto;
    onSuccess: () => void;
}

const greekAlphabet = [
    'Alpha', 'Beta', 'Gamma', 'Delta', 'Epsilon', 'Zeta',
    'Eta', 'Theta', 'Iota', 'Kappa', 'Lambda', 'Mu',
    'Nu', 'Xi', 'Omicron', 'Pi', 'Rho', 'Sigma',
    'Tau', 'Upsilon', 'Phi', 'Chi', 'Psi', 'Omega'
];

export const StarForm = ({ initialValues, onSuccess }: StarFormProps) => {
    const [form] = Form.useForm();
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        return () => {
            form.resetFields(); // Reset form when component unmounts
        };
    }, [form]);

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
        <Scrollbar style={{ width: '100%', height: '60vh'}}>
            {/*<div>*/}
            <div style={{ maxHeight: 'calc(100vh - 200px)', paddingBottom: 80}}>
            <Form
                form={form}
                layout="vertical"
                initialValues={initialValues || {}}
                onFinish={handleSubmit}
                size="small" // Makes form elements more compact
                style={{ rowGap: 8 }} // Reduces spacing between form items
                className="compact-form"
            >
                <Form.Item style={{ marginBottom: 8, marginTop: 8 }} name="name" label="Name" rules={[{ required: true, message: 'Please enter name' }]}>
                    <Input style={{ width: '95%' }}/>
                </Form.Item>

                <Form.Item style={{ marginBottom: 8 }} name="type" label="Type" rules={[{ required: true, message: 'Please enter type' }]}>
                    <Select
                        style={{ width: '95%' }}
                        placeholder="Select star type"
                        options={[
                            {
                                label: 'Main Sequence',
                                options: [
                                    { value: 'O', label: 'O-type (Blue Supergiants)' },
                                    { value: 'B', label: 'B-type (Blue-White Stars)' },
                                    { value: 'A', label: 'A-type (White Stars)' },
                                    { value: 'F', label: 'F-type (Yellow-White Stars)' },
                                    { value: 'G', label: 'G-type (Yellow Stars, like our Sun)' },
                                    { value: 'K', label: 'K-type (Orange Dwarfs)' },
                                    { value: 'M', label: 'M-type (Red Dwarfs)' },
                                ],
                            },
                            {
                                label: 'Subdivisions',
                                options: Array.from({ length: 10 }, (_, i) => ({
                                    value: `${i}`,
                                    label: `Subclass ${i}`,
                                })),
                            },
                            {
                                label: 'Other',
                                options: [
                                    { value: 'MULTI', label: 'Multiple Star System (Binary, Trinary, etc.)' },
                                    { value: 'WR', label: 'Wolf-Rayet' },
                                    { value: 'L', label: 'L-type (Brown Dwarfs)' },
                                    { value: 'T', label: 'T-type (Methane Dwarfs)' },
                                    { value: 'Y', label: 'Y-type (Cool Brown Dwarfs)' },
                                    { value: 'DA', label: 'White Dwarf (DA)' },
                                    { value: 'DB', label: 'White Dwarf (DB)' },
                                    { value: 'DZ', label: 'White Dwarf (DZ)' },
                                    { value: 'NS', label: 'Neutron Star' },
                                    { value: 'BH', label: 'Black Hole' },
                                ],
                            },
                        ]}
                    />
                </Form.Item>

                <Form.Item style={{ marginBottom: 8 }} name="mass" label="Mass" rules={[{ required: true, message: 'Please enter mass' }]}>
                    <InputNumber min={0} style={{ width: '95%' }} />
                </Form.Item>

                <Form.Item style={{ marginBottom: 8 }} name="radius" label="Radius" rules={[{ required: true, message: 'Please enter radius' }]}>
                    <InputNumber min={0} style={{ width: '95%' }} />
                </Form.Item>

                <Form.Item
                    style={{ marginBottom: 8 }}
                    name="temperature"
                    label="Temperature"
                    rules={[{ required: true, message: 'Please enter temperature' }]}
                >
                    <InputNumber min={0} style={{ width: '95%' }} />
                </Form.Item>

                <Form.Item
                    style={{ marginBottom: 8 }}
                    name="luminosity"
                    label="Luminosity"
                    rules={[{ required: true, message: 'Please enter luminosity' }]}
                >
                    <InputNumber min={0} style={{ width: '95%' }} />
                </Form.Item>

                <Form.Item
                    style={{ marginBottom: 8 }}
                    name="rightAscension"
                    label="Right Ascension"
                    rules={[{ required: true, message: 'Please enter right ascension' }]}
                >
                    <InputNumber step={0.01} style={{ width: '95%' }} />
                </Form.Item>

                <Form.Item
                    style={{ marginBottom: 8 }}
                    name="declination"
                    label="Declination"
                    rules={[{ required: true, message: 'Please enter declination' }]}
                >
                    <InputNumber step={0.01} style={{ width: '95%' }} />
                </Form.Item>

                <Form.Item style={{ marginBottom: 8, paddingBottom: 8 }} name="positionInConstellation" label="Position in Constellation">
                    <Select
                        style={{ width: '95%' }}
                        options={greekAlphabet.map(letter => ({
                            label: letter,
                            value: letter
                        }))}
                    />
                </Form.Item>
                <Form.Item style={{marginBottom: 10, paddingBottom:10}}>
                    <Space>
                        <ReactiveButton
                            rounded
                            color="primary | green"
                            size="medium"
                            disabled={loading}
                            idleText={<span>{initialValues?.id ? 'Update Star' : 'Create Star'}</span>}
                            type={'submit'}
                        />
                        <ReactiveButton
                            rounded
                            color="secondary | red"
                            size="medium"
                            onClick={() => form.resetFields()}
                            disabled={loading}
                            idleText={<span>Reset</span>}
                        />
                    </Space>
                </Form.Item>
            </Form>
            </div>

            {/*</div>*/}
        </Scrollbar>
    );
};