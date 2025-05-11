import {Form, Input, InputNumber, message, Select, Space, Upload} from 'antd';
import type { UploadFile, RcFile } from 'antd/es/upload/interface';
import type { UploadRequestOption } from 'rc-upload/lib/interface';
import {StarCriteria, StarDto} from '../../types/stars';
import {starsApi} from '../../api/starApi.ts';
import {useEffect, useState} from "react";
import ReactiveButton from "reactive-button";
import Scrollbar from "react-scrollbars-custom";
import '../../styles/custom-scrollbar.css'
import {UploadOutlined} from "@ant-design/icons";

interface StarFormProps {
    initialValues?: StarDto | StarCriteria;
    onSuccess: () => void;
    isFilter?: boolean;
    onFilter?: (values: StarCriteria) => void;
    onReset?: () => void;
}

const greekAlphabet = [
    'Alpha', 'Beta', 'Gamma', 'Delta', 'Epsilon', 'Zeta',
    'Eta', 'Theta', 'Iota', 'Kappa', 'Lambda', 'Mu',
    'Nu', 'Xi', 'Omicron', 'Pi', 'Rho', 'Sigma',
    'Tau', 'Upsilon', 'Phi', 'Chi', 'Psi', 'Omega'
];

export const StarForm = ({
                             initialValues,
                             onSuccess,
                             isFilter=false,
                             onFilter,
                             onReset
                         }: StarFormProps) => {
    const [form] = Form.useForm();
    const [loading, setLoading] = useState(false);
    const [previewImage, setPreviewImage] = useState<string>();

    useEffect(() => {
        return () => {
            form.resetFields();
        };
    }, [form]);

    const beforeUpload = (file: RcFile) => {
        const validTypes = ['image/jpeg', 'image/png', 'image/webp', 'image/svg+xml'];
        const isValidType = validTypes.includes(file.type);

        const isLt5M = file.size / 1024 / 1024 < 5;

        if (!isValidType) {
            message.error('You can only upload JPG/PNG/WEBP files!');
            return Upload.LIST_IGNORE;
        }

        if (!isLt5M) {
            message.error('Image must be smaller than 5MB!');
            return Upload.LIST_IGNORE;
        }

        return true;
    };

    const handleUpload = async (options: UploadRequestOption) => {
        const { file } = options;
        const formData = new FormData();
        formData.append('file', file as Blob);

        try {
            const res = await starsApi.uploadImage(file as File);
            form.setFieldValue('imageUrl', res.data);
            if (file instanceof File) {
                setPreviewImage(URL.createObjectURL(file));
            }
        } catch (error) {
            message.error(error instanceof Error ? `Upload failed: ${error.message}` : 'Upload failed');
        }
    };
    const handlePreviewChange = (info: { file: UploadFile }) => {
        if (info.file.originFileObj) {
            setPreviewImage(URL.createObjectURL(info.file.originFileObj));
        }
    };

    const handleSubmit = async (values: StarDto | StarCriteria) => {
        if(isFilter) {
            onFilter?.(values as StarCriteria);
            return;
        }
        try {
            setLoading(true);
            if (initialValues) {
                await starsApi.put((initialValues as StarDto).id, values as StarDto);
                message.success('Star updated successfully');
            } else {
                await starsApi.create(values as StarDto);
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
        <Scrollbar style={{width: '100%', height: '60vh'}}>
            {/*<div>*/}
            <div style={{maxHeight: 'calc(100vh - 200px)', paddingBottom: 80}}>
                <Form
                    form={form}
                    layout="vertical"
                    initialValues={initialValues || {}}
                    onFinish={handleSubmit}
                    size="small"
                    style={{rowGap: 8}}
                    className="compact-form"
                >
                    <Form.Item name="imageUrl" label="Star Image" hidden={isFilter}>
                        <Upload
                            name="image"
                            listType="picture-card"
                            showUploadList={false}
                            customRequest={handleUpload}
                            beforeUpload={beforeUpload}
                            onChange={handlePreviewChange}
                        >
                            {previewImage ? (
                                <img
                                    src={previewImage}
                                    alt="preview"
                                    style={{ width: '100%' }}
                                />
                            ) : (
                                <div>
                                    <UploadOutlined />
                                    <div style={{ marginTop: 8 }}>Upload</div>
                                </div>
                            )}
                        </Upload>
                    </Form.Item>
                    <Form.Item style={{marginBottom: 8, marginTop: 8}} name="name" label="Name"
                               rules={isFilter ? [] : [{required: true, message: 'Please enter name'}]}>
                        <Input style={{width: '95%'}}/>
                    </Form.Item>

                    <Form.Item style={{marginBottom: 8}} name="type" label="Type"
                               rules={isFilter ? [] : [{required: true, message: 'Please enter type'}]}>
                        <Select

                            style={{width: '95%'}}
                            placeholder="Select star type"
                            options={[
                                {
                                    label: 'Main Sequence',
                                    options: [
                                        {value: 'O', label: 'O-type (lightblue)'},
                                        {value: 'B', label: 'B-type (white-blue)'},
                                        {value: 'A', label: 'A-type (white)'},
                                        {value: 'F', label: 'F-type (white-yellow)'},
                                        {value: 'G', label: 'G-type (yellow)'},
                                        {value: 'K', label: 'K-type (orange)'},
                                        {value: 'M', label: 'M-type (red)'},
                                    ],
                                },
                                {
                                    label: 'Subdivisions',
                                    options: Array.from({length: 10}, (_, i) => ({
                                        value: `${i}`,
                                        label: `Subclass ${i}`,
                                    })),
                                },
                                {
                                    label: 'Other',
                                    options: [
                                        {value: 'MULTI', label: 'Multiple Star System (Binary, Trinary, etc.)'},
                                        {value: 'WR', label: 'Wolf-Rayet'},
                                        {value: 'L', label: 'L-type (Brown Dwarfs)'},
                                        {value: 'T', label: 'T-type (Methane Dwarfs)'},
                                        {value: 'Y', label: 'Y-type (Cool Brown Dwarfs)'},
                                        {value: 'DA', label: 'White Dwarf (DA)'},
                                        {value: 'DB', label: 'White Dwarf (DB)'},
                                        {value: 'DZ', label: 'White Dwarf (DZ)'},
                                        {value: 'NS', label: 'Neutron Star'},
                                        {value: 'BH', label: 'Black Hole'},
                                    ],
                                }
                            ]}
                        />
                    </Form.Item>

                    <Form.Item style={{marginBottom: 8}} name="mass" label="Mass"
                               rules={isFilter ? [] : [{required: true, message: 'Please enter mass'}]}>
                        <InputNumber min={0} style={{width: '95%'}}/>
                    </Form.Item>

                    <Form.Item style={{marginBottom: 8}} name="radius" label="Radius"
                               rules={isFilter ? [] : [{required: true, message: 'Please enter radius'}]}>
                        <InputNumber min={0} style={{width: '95%'}}/>
                    </Form.Item>

                    <Form.Item
                        style={{marginBottom: 8}}
                        name="temperature"
                        label="Temperature"
                        rules={isFilter ? [] : [{required: true, message: 'Please enter temperature'}]}
                    >
                        <InputNumber min={0} style={{width: '95%'}}/>
                    </Form.Item>

                    <Form.Item
                        style={{marginBottom: 8}}
                        name="luminosity"
                        label="Luminosity"
                        rules={isFilter ? [] : [{required: true, message: 'Please enter luminosity'}]}
                    >
                        <InputNumber min={0} style={{width: '95%'}}/>
                    </Form.Item>

                    <Form.Item
                        style={{marginBottom: 8}}
                        name="rightAscension"
                        label="Right Ascension"
                        rules={isFilter ? [] : [{required: true, message: 'Please enter right ascension'}]}
                    >
                        <InputNumber step={0.01} style={{width: '95%'}}/>
                    </Form.Item>

                    <Form.Item
                        style={{marginBottom: 8}}
                        name="declination"
                        label="Declination"
                        rules={isFilter ? [] : [{required: true, message: 'Please enter declination'}]}
                    >
                        <InputNumber step={0.01} style={{width: '95%'}}/>
                    </Form.Item>

                    <Form.Item style={{marginBottom: 8, paddingBottom: 8}} name="positionInConstellation"
                               label="Position in Constellation">
                        <Select
                            style={{width: '95%'}}
                            options={greekAlphabet.map(letter => ({
                                label: letter,
                                value: letter
                            }))}
                        />
                    </Form.Item>
                    <Form.Item style={{marginBottom: 10, paddingBottom: 10}}>
                        <Space>
                            <ReactiveButton
                                rounded
                                color="green"
                                size="medium"
                                disabled={loading}
                                idleText={<span>{isFilter ? 'Apply filters' : (initialValues as StarDto)?.id ? 'Update Star' : 'Create Star'}</span>}
                                type={'submit'}
                            />
                            <ReactiveButton
                                rounded
                                color="red"
                                size="medium"
                                onClick={() => {
                                    form.resetFields()
                                    if (isFilter) {
                                        onReset?.();
                                    }
                                }
                                }
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
