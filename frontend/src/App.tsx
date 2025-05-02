import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage.tsx';
import ConstellationPage from './pages/ConstellationPage.tsx';
import StarPage from './pages/StarPage.tsx';
import {MainLayout} from "./components/layout/MainLayout.tsx";

function App() {
    return (
        <Router>
            <MainLayout>
                <Routes>
                    <Route path="/" element={<HomePage />} />
                    <Route path="/constellations" element={<ConstellationPage />} />
                    <Route path="/stars" element={<StarPage />} />
                    <Route path="*" element={<div>404 Not Found TRY LATER LUV U</div>} />
                </Routes>
            </MainLayout>
        </Router>
    );
}

export default App;
