import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage.tsx';
import ConstellationsPage from './pages/ConstellationsPage.tsx';
import {MainLayout} from "./components/layout/MainLayout.tsx";
import StarsPage from "./pages/StarsPage.tsx";
import '@ant-design/v5-patch-for-react-19';

function App() {
    return (
            <Router>
                <MainLayout>
                    <Routes>
                        <Route path="/" element={<HomePage />} />
                        <Route path="/constellations" element={<ConstellationsPage />} />
                        <Route path="/stars" element={<StarsPage />} />
                        <Route path="*" element={<div>404 Not Found TRY LATER LUV U</div>} />
                    </Routes>
                </MainLayout>
            </Router>
    );
}

export default App;
