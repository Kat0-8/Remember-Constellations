import { Menu, Typography } from 'antd';
import { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import {HomeOutlined, StarOutlined} from "@ant-design/icons";
import ConstellationIcon from "../customConstellationIcon.tsx";

const { Title } = Typography;

export const AppHeader = () => {
    const location = useLocation();
    const [selectedKeys, setSelectedKeys] = useState<string[]>([]);

    useEffect(() => {
        const path = location.pathname;
        if (path === '/') setSelectedKeys(['home']);
        else if (path.startsWith('/constellations')) setSelectedKeys(['constellations']);
        else if (path.startsWith('/stars')) setSelectedKeys(['stars']);
    }, [location]);

    return (
        <div style={{ display: 'flex', alignItems: 'center', overflowX: 'auto' }}>
            <Link to="/">
                <Title
                    level={3}
                    style={{
                        color: 'white',
                        margin: 0,
                        marginRight: '24px',
                        cursor: 'pointer',
                        lineHeight: 'inherit'
                    }}
                >
                    Remember-Constellations
                </Title>
            </Link>
            <Menu
                theme="dark"
                mode="horizontal"
                style={{
                    display: 'flex',
                    flexWrap: 'nowrap',
                    overflowX: 'auto',
                    minWidth: 'max-content',
                }}
                selectedKeys={selectedKeys}
                items={[
                    {
                        icon: <HomeOutlined />,
                        key: 'home',
                        label: <Link to="/">Home</Link>,
                    },
                    {
                        icon: <ConstellationIcon/>,
                        key: 'constellations',
                        label: <Link to="/constellations">Constellations</Link>,
                    },
                    {
                        icon: <StarOutlined />,
                        key: 'stars',
                        label: <Link to="/stars">Stars</Link>,
                    },
                ]}
            />
        </div>
    );
};