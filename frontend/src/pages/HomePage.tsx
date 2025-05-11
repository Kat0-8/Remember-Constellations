import { Space } from 'antd';
import { Link } from 'react-router-dom';
import ReactiveButton from "reactive-button";

const HomePage = () => (
    <div style={{
        backgroundImage: `url(/images/home_page_bg.png)`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        height: '100vh',
        width: '99vw',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        position: 'relative',
    }}>

        <div style={{
            position: 'absolute',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundColor: 'rgba(0, 0, 0, 0.5)'
        }} />

        <div style={{
            backgroundColor: '#000000',
            padding: '20px',
            borderRadius: '24px',
            textAlign: 'center',
            width: '40%',
            maxWidth: '1200px',
            boxShadow: '0 4px 8px rgba(0, 0, 0, 0.3)',
            position: 'relative',
            zIndex: 1,
            margin: '0 auto',
            boxSizing: 'border-box'
        }}>
            <Space
                direction="vertical"
                size="large"
                style={{
                    width: '100%',
                    justifyContent: 'center'
                }}
            >
                <h1 style={{ color: 'white', margin: 0 }}>
                    Welcome!
                </h1>

                <Space
                    size="middle"
                    style={{
                        width: '100%',
                        justifyContent: 'center',
                        flexWrap: 'wrap',
                    }}
                >

                    <div style={{ textAlign: 'center', marginBottom: '20px' }}>
                        <Link to="/constellations">
                            <div style={{ marginBottom: '20px' }}>
                                <img
                                    src="/images/constellation_icon.png"
                                    alt="Constellation Icon"
                                    style={{
                                        width: '150px',
                                        height: '150px',
                                        cursor: 'pointer',
                                        transition: 'transform 0.3s ease-in-out',
                                    }}
                                    onMouseEnter={(e) => {
                                        e.currentTarget.style.transform = 'scale(1.1)';
                                    }}
                                    onMouseLeave={(e) => {
                                        e.currentTarget.style.transform = 'scale(1)';
                                    }}
                                />
                            </div>
                            <ReactiveButton
                                className="blue-button"
                                rounded
                                size="large"
                                style={{
                                    fontWeight:'bold',
                                    minWidth:'150px'
                                }}
                                idleText={"Constellations"}
                            />
                        </Link>
                    </div>

                    <div style={{ textAlign: 'center', marginBottom: '20px' }}>
                        <Link to="/stars">
                            <div style={{ marginBottom: '20px' }}>
                                <img
                                    src="/images/star_icon.png"
                                    alt="Star Icon"
                                    style={{
                                        width: '150px',
                                        height: '150px',
                                        cursor: 'pointer',
                                        transition: 'transform 0.3s ease-in-out',
                                    }}
                                    onMouseEnter={(e) => {
                                        e.currentTarget.style.transform = 'scale(1.3)';
                                    }}
                                    onMouseLeave={(e) => {
                                        e.currentTarget.style.transform = 'scale(1)';
                                    }}
                                />
                            </div>
                            <ReactiveButton
                                className="blue-button"
                                rounded
                                size="large"
                                style={{ minWidth:'150px', fontWeight:'bold'}}
                                idleText={"Stars"}
                            />
                        </Link>
                    </div>
                </Space>
            </Space>
        </div>
    </div>
);

export default HomePage;
