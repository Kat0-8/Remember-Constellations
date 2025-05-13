import {Form, FormInstance, Input, message, Select, Space, Upload} from 'antd';
import type { UploadFile, RcFile } from 'antd/es/upload/interface';
import type { UploadRequestOption } from 'rc-upload/lib/interface';
import {ConstellationCriteria, ConstellationDto} from '../../types/constellations';
import {constellationApi} from '../../api/constellationApi.ts';
import {useEffect, useState} from "react";
import ReactiveButton from "reactive-button";
import Scrollbar from "react-scrollbars-custom";
import '../../styles/custom-scrollbar.css';
import {UploadOutlined} from "@ant-design/icons";
import {starsApi} from "../../api/starApi.ts";
import {StarDto} from "../../types/stars.ts";

interface ConstellationFormProps {
    form: FormInstance;
    initialValues?: ConstellationDto | ConstellationCriteria;
    onSuccess: () => void;
    isFilter?: boolean;
    onFilter?: (values: ConstellationCriteria) => void;
    onReset?: () => void;
    previewImage?: string;
    setPreviewImage?: React.Dispatch<React.SetStateAction<string>>;
}

const constellationFamilies = [
    'Ursa Major', 'Zodiac', 'Perseus', 'Hercules', 'Orion',
    'Heavenly Waters', 'Bayer Family', 'La Caille'
];

const celestialRegions = [
    'Northern Hemisphere', 'Southern Hemisphere', 'Equatorial'
];

export const ConstellationForm = ({
                                      form,
                                      initialValues,
                                      onSuccess,
                                      isFilter = false,
                                      onFilter,
                                      onReset,
                                      previewImage,
                                      setPreviewImage
                                  }: ConstellationFormProps) => {
    const [loading, setLoading] = useState(false);
    const [allStars, setAllStars] = useState<StarDto[]>([]);
    const [selectedStarIds, setSelectedStarIds] = useState<number[]>([]);

    useEffect(() => {
        const loadStars = async () => {
            try {
                const response = await starsApi.getAll({
                    constellationId: undefined // Only unattached stars
                });
                setAllStars(response.data);
            } catch (error) {
                message.error(error instanceof Error ? `Upload failed: ${error.message}` : 'Upload failed');
            }
        };
        if (!isFilter) loadStars();
    }, [isFilter]);

    useEffect(() => {
        return () => {
            form.resetFields();
            setPreviewImage?.('');
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
        formData.append('file', file as Blob)

        try {
            const res = await constellationApi.uploadImage(file as File);
            form.setFieldValue('imageUrl', res.data);
            if (file instanceof File) {
                setPreviewImage?.(URL.createObjectURL(file));
            }
        } catch (error) {
            message.error(error instanceof Error ? `Upload failed: ${error.message}` : 'Upload failed');
        }
    };

    const handlePreviewChange = (info: { file: UploadFile }) => {
        if (info.file.originFileObj) {
            setPreviewImage?.(URL.createObjectURL(info.file.originFileObj));
        }
    };

    const handleSubmit = async (values: ConstellationDto | ConstellationCriteria) => {
        if (isFilter) {
            onFilter?.(values as ConstellationCriteria);
            return;
        }

        try {
            setLoading(true);
            const constellationData = values as ConstellationDto;

            if (initialValues && 'id' in initialValues) {
                await constellationApi.patch(initialValues.id, constellationData);
                await constellationApi.attachStars(initialValues.id, selectedStarIds);
                message.success('Constellation updated successfully');
            } else {
                const response = await constellationApi.create(constellationData);
                if (selectedStarIds.length > 0) {
                    await constellationApi.attachStars(response.data.id, selectedStarIds);
                }
                message.success('Constellation created successfully');
            }

            onSuccess();
        } catch (error) {
            message.error(error instanceof Error ? `Operation failed: ${error.message}` : 'Operation failed');
        } finally {
            setLoading(false);
            setPreviewImage?.('');
        }
    };

    return (
        <Scrollbar style={{width: '100%', height: '50vh'}}>
            <div style={{maxHeight: 'calc(80vh - 100px)', paddingBottom: 20}}>
                <Form
                    form={form}
                    layout="vertical"
                    initialValues={initialValues || {}}
                    onFinish={handleSubmit}
                    size="small"
                    style={{rowGap: 8, paddingBottom: 0}}
                    className="compact-form"
                >
                    <Form.Item name="imageUrl" label="Constellation Image" hidden={isFilter}>
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

                    <Form.Item
                        style={{marginBottom: 8, marginTop: 8}}
                        name="name"
                        label="Name"
                        rules={isFilter ? [] : [{required: true, message: 'Please enter name'}]}
                    >
                        <Input style={{width: '95%'}}/>
                    </Form.Item>

                    <Form.Item
                        style={{marginBottom: 8}}
                        name="abbreviation"
                        label="Abbreviation"
                        rules={isFilter ? [] : [
                            {required: true, message: 'Please enter abbreviation'},
                            {max: 3, message: 'Max 3 characters allowed'}
                        ]}
                    >
                        <Input style={{width: '95%'}} maxLength={3}/>
                    </Form.Item>

                    <Form.Item
                        style={{marginBottom: 8}}
                        name="family"
                        label="Family"
                        rules={isFilter ? [] : [{required: true, message: 'Please select family'}]}
                    >
                        <Select
                            style={{width: '95%'}}
                            placeholder="Select constellation family"
                            options={constellationFamilies.map(family => ({
                                value: family,
                                label: family
                            }))}
                        />
                    </Form.Item>

                    <Form.Item
                        style={{marginBottom: 8, paddingBottom:8}}
                        name="region"
                        label="Region"
                        rules={isFilter ? [] : [{required: true, message: 'Please select region'}]}
                    >
                        <Select
                            style={{width: '95%'}}
                            placeholder="Select celestial region"
                            options={celestialRegions.map(region => ({
                                value: region,
                                label: region
                            }))}
                        />
                    </Form.Item>

                    <Form.Item
                        label="Attach Stars"
                        hidden={isFilter}
                    >

                            <Select
                                mode="multiple"
                                style={{ width: '95%' }}
                                placeholder="Select available stars"
                                value={selectedStarIds}
                                onChange={setSelectedStarIds}
                                options={allStars.map(star => ({
                                    value: star.id,
                                    label: `${star.name} (${star.type})`,
                                    disabled: star.constellationId !== null // Disable already attached
                                }))}
                                showSearch
                                optionFilterProp="label"
                            />
                            {/*<ReactiveButton*/}
                            {/*    rounded*/}
                            {/*    outline*/}
                            {/*    size="small"*/}
                            {/*    color="blue"*/}
                            {/*    idleText="Create New Star"*/}
                            {/*/>*/}

                    </Form.Item>

                    <Form.Item style={{marginBottom: 0, paddingBottom: 0}}>
                        <Space>
                            <ReactiveButton
                                rounded
                                color="green"
                                size="medium"
                                disabled={loading}
                                idleText={
                                    <span>
                                        {isFilter ? 'Apply filters' :
                                            (initialValues as ConstellationDto)?.id ? 'Update Constellation' : 'Create Constellation'}
                                    </span>}
                                type={'submit'}
                            />
                            <ReactiveButton
                                rounded
                                color="red"
                                size="medium"
                                onClick={() => {
                                    form.resetFields();
                                    setPreviewImage?.('');
                                    if (isFilter) {
                                        onReset?.();
                                    }
                                }}
                                disabled={loading}
                                idleText={<span>Reset</span>}
                            />
                        </Space>
                    </Form.Item>
                </Form>
            </div>
        </Scrollbar>
    );
};