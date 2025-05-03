import { Button, Space } from 'antd';
import { Link } from 'react-router-dom';

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

        {/* Dark overlay */}
        <div style={{
            position: 'absolute',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundColor: 'rgba(0, 0, 0, 0.5)'
        }} />

        {/* Content container */}
        <div style={{
            backgroundColor: '#000000',
            padding: '20px',
            borderRadius: '24px',
            textAlign: 'center',
            width: '40%', // Responsive width
            maxWidth: '1200px', // Maximum width
            boxShadow: '0 4px 8px rgba(0, 0, 0, 0.3)',
            position: 'relative',
            zIndex: 1,
            margin: '0 auto', // Center horizontally
            boxSizing: 'border-box' // Include padding in width
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
                        flexWrap: 'wrap', // Allow wrapping on small screens
                    }}
                >
                    {/* First Image Link - Constellation Catalogue */}
                    <div style={{ textAlign: 'center', marginBottom: '20px' }}>
                        <Link to="/constellations">
                            <div style={{ marginBottom: '20px' }}>
                                <img
                                    src="/images/constellation_icon.png" // Replace with actual path
                                    alt="Constellation Icon"
                                    style={{
                                        width: '150px', // Increased image size
                                        height: '150px', // Increased image size
                                        cursor: 'pointer', // Shows pointer on hover (like a button)
                                        transition: 'transform 0.3s ease-in-out', // Smooth scale transition on hover
                                    }}
                                    onMouseEnter={(e) => {
                                        e.currentTarget.style.transform = 'scale(1.1)'; // Zoom in on hover
                                    }}
                                    onMouseLeave={(e) => {
                                        e.currentTarget.style.transform = 'scale(1)'; // Zoom out after hover
                                    }}
                                />
                            </div>
                            <Button
                                type="primary"
                                size="large"
                                style={{
                                    minWidth: '150px',
                                    margin: '8px',
                                    fontWeight: 'bold'
                                }}
                            >
                            <p>Constellations</p>
                            </Button>
                        </Link>
                    </div>

                    {/* Second Image Link - Star Catalogue */}
                    <div style={{ textAlign: 'center', marginBottom: '20px' }}>
                        <Link to="/stars">
                            <div style={{ marginBottom: '20px' }}>
                                <img
                                    src="/images/star_icon.png" // Replace with actual path
                                    alt="Star Icon"
                                    style={{
                                        width: '150px', // Increased image size
                                        height: '150px', // Increased image size
                                        cursor: 'pointer', // Shows pointer on hover (like a button)
                                        transition: 'transform 0.3s ease-in-out', // Smooth scale transition on hover
                                    }}
                                    onMouseEnter={(e) => {
                                        e.currentTarget.style.transform = 'scale(1.3)'; // Zoom in on hover
                                    }}
                                    onMouseLeave={(e) => {
                                        e.currentTarget.style.transform = 'scale(1)'; // Zoom out after hover
                                    }}
                                />
                            </div>
                            <Button
                                type="primary"
                                size="large"
                                style={{
                                    minWidth: '150px',
                                    margin: '8px',
                                    fontWeight: 'bold'
                                }}
                            >
                                <p>Stars</p>
                            </Button>
                        </Link>
                    </div>
                </Space>
            </Space>
        </div>
    </div>
);

export default HomePage;
