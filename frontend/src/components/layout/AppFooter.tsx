import { Typography } from 'antd';
import {GithubOutlined, MailOutlined} from "@ant-design/icons";

const { Title } = Typography;

export const AppFooter = () => (
    <div style={{ display: 'flex', justifyContent: 'space-around' }}>
        <div>
            <Title level={5} style={{ color: 'rgba(255,255,255,0.85)' }}>Contact</Title>
            <p><MailOutlined />Email: prokopovich-dan-bsuir@inbox.ru</p>
            <p>
                <GithubOutlined />Github: <a href="https://github.com/Kat0-8" target="_blank" style={{ color: 'grey', textDecoration: 'underline' }}>*touch me*</a>
            </p>
        </div>
        <div>
            <Title level={5} style={{ color: 'rgba(255,255,255,0.85)' }}>About</Title>
            <p>Astronomy catalog system</p>
        </div>
    </div>
);