import { Table, Button } from 'antd';
import { Link } from 'react-router-dom';
import { StarDto } from '../../types/stars.ts';
import * as React from "react";

interface Props {
    data: StarDto[];
    onDelete: (id: number) => void;
}

const StarList: React.FC<Props> = ({ data, onDelete }) => {
    const columns = [
        { title: 'Name', dataIndex: 'name' },
        { title: 'Type', dataIndex: 'type' },
        { title: 'Mass', dataIndex: 'mass' },
        { title: 'Temperature', dataIndex: 'temperature' },
        {
            title: 'Actions',
            render: (_: StarDto, record: StarDto) => (
                <>
                    <Button type="link">
                        <Link to={`/stars/${record.id}`}>View</Link>
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

export default StarList;
