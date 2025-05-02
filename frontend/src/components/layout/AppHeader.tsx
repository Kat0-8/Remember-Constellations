import { Menu, Typography } from 'antd';
import { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';

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
        <div style={{ display: 'flex', alignItems: 'center' }}>
            <Title level={3} style={{ color: 'white', margin: 0, marginRight: '24px' }}>
                Remember-Constellations
            </Title>
            <Menu
                theme="dark"
                mode="horizontal"
                selectedKeys={selectedKeys}
                items={[
                    {
                        key: 'home',
                        label: <Link to="/">Home</Link>,
                    },
                    {
                        key: 'constellations',
                        label: <Link to="/constellations">Constellations</Link>,
                    },
                    {
                        key: 'stars',
                        label: <Link to="/stars">Stars</Link>,
                    },
                ]}
            />
        </div>
    );
};