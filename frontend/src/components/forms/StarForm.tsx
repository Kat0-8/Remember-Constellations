import {Form, Input, InputNumber, message, Select, Space} from 'antd';
import {StarCriteria, StarDto} from '../../types/stars';
import {starsApi} from '../../api/starApi.ts';
import {useEffect, useState} from "react";
import ReactiveButton from "reactive-button";
import Scrollbar from "react-scrollbars-custom";
import '../../styles/custom-scrollbar.css'

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

    useEffect(() => {
        return () => {
            form.resetFields(); // Reset form when component unmounts
        };
    }, [form]);

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
                    size="small" // Makes form elements more compact
                    style={{rowGap: 8}} // Reduces spacing between form items
                    className="compact-form"
                >

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