import { Button, Space } from 'antd';
import { Link } from 'react-router-dom';

const MainPage = () => (
    <Space direction="vertical" size="large" style={{ textAlign: 'center' }}>
        <h1>Welcome to the Constellation & Star Catalogue</h1>
        <Space>
            <Button type="primary">
                <Link to="/constellations">Constellation Catalogue</Link>
            </Button>
            <Button type="primary">
                <Link to="/stars">Star Catalogue</Link>
            </Button>
        </Space>
    </Space>
);

export default MainPage;
