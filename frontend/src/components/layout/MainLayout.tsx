import { Layout } from 'antd';
import { ReactNode } from 'react';
import { AppHeader } from './AppHeader';
import { AppFooter } from './AppFooter';

const { Header, Content, Footer } = Layout;

export const MainLayout = ({ children }: { children: ReactNode }) => (
    <Layout style={{ minHeight: '100vh' }}>
        <Header style={{ position: 'sticky', top: 0, zIndex: 1000, width: '100%', flex: '0 0 auto' }}>
            <AppHeader />
        </Header>

        <Content style={{
            flex: 1,
            padding: 0,
            display: 'flex',
            flexDirection: 'column',
            overflow: 'auto',
            minHeight: 'calc(100vh - 64px - 70px)',
        }}>
            {children}
        </Content>

        <Footer style={{ textAlign: 'center', background: '#001529', color: 'rgba(255,255,255,0.65)', flex: '0 0 auto' }}>
            <AppFooter />
        </Footer>
    </Layout>
);
