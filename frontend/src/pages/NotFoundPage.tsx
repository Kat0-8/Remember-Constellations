import React from 'react';
import { Result } from 'antd';
import { useNavigate } from 'react-router-dom';
import ReactiveButton from "reactive-button";

const NotFoundPage: React.FC = () => {
    const navigate = useNavigate();

    return (
        <div style={{ padding: '50px', textAlign: 'center', height: '100vh', width: '99vw'}}>
            <Result
                status="404"
                title="404"
                subTitle="Sorry, the page you tried to visit does not exist."
                extra={
                    <ReactiveButton
                        className="blue-button"
                        rounded
                        size="large"
                        type="primary"
                        idleText={"Return Home"}
                        onClick={() => navigate('/')}
                    />
                }
            />
        </div>
    );
};

export default NotFoundPage;
