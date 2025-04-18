import { Layout, Menu, Typography } from 'antd';
import { ReactNode, useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';

const { Header, Content, Footer } = Layout;
const { Title } = Typography;

export const MainLayout = ({ children }: { children: ReactNode }) => {
    const location = useLocation();
    const [selectedKeys, setSelectedKeys] = useState<string[]>([]);

    useEffect(() => {
        const path = location.pathname;
        if (path === '/') setSelectedKeys(['home']);
        else if (path.startsWith('/constellations')) setSelectedKeys(['constellations']);
        else if (path.startsWith('/stars')) setSelectedKeys(['stars']);
    }, [location]);

    return (
        <Layout style={{ minHeight: '100vh' }}>
            <Header style={{ position: 'sticky', top: 0, zIndex: 1, width: '100%' }}>
                <div style={{ display: 'flex', alignItems: 'center' }}>
                    <Title level={3} style={{ color: 'white', margin: 0, marginRight: '24px' }}>
                        Star Catalog
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
            </Header>

            <Content style={{ padding: '24px 50px', flex: 1 }}>{children}</Content>

            <Footer style={{ textAlign: 'center', background: '#001529', color: 'rgba(255,255,255,0.65)' }}>
                <div style={{ display: 'flex', justifyContent: 'space-around' }}>
                    <div>
                        <Title level={5} style={{ color: 'rgba(255,255,255,0.85)' }}>Contact</Title>
                        <p>Email: prokopovich-dan-bsuir@inbox.ru</p>
                    </div>
                    <div>
                        <Title level={5} style={{ color: 'rgba(255,255,255,0.85)' }}>About</Title>
                        <p>Astronomy catalog system</p>
                    </div>
                </div>
            </Footer>
        </Layout>
    );
};