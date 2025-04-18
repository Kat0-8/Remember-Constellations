import { Table, Button } from 'antd';
import { Link } from 'react-router-dom';
import { ConstellationDto } from '../types/constellations';
import * as React from "react";

interface Props {
    data: ConstellationDto[];
    onDelete: (id: number) => void;
}

const ConstellationList: React.FC<Props> = ({ data, onDelete }) => {
    const columns = [
        { title: 'Name', dataIndex: 'name' },
        { title: 'Abbreviation', dataIndex: 'abbreviation' },
        { title: 'Family', dataIndex: 'family' },
        { title: 'Region', dataIndex: 'region' },
        {
            title: 'Actions',
            render: (_: ConstellationDto, record: ConstellationDto) => (
                <>
                    <Button type="link">
                        <Link to={`/constellations/${record.id}`}>View</Link>
                    </Button>
                    <Button type="link" danger onClick={() => onDelete(record.id)}>
                        Delete
                    </Button>
                </>
            ),
        },
    ];

    return <Table columns={columns} dataSource={data} rowKey="id" />;
};

export default ConstellationList;
