import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import MainPage from './pages/HomePage.tsx';
import ConstellationPage from './pages/ConstellationPage.tsx';
import StarPage from './pages/StarPage.tsx';
import {MainLayout} from "./components/MainLayout.tsx";

function App() {
    return (
        <MainLayout>
            <Router>
                <Routes>
                    <Route path="/" element={<MainPage />} />
                    <Route path="/constellations" element={<ConstellationPage />} />
                    <Route path="/stars" element={<StarPage />} />
                </Routes>
            </Router>
        </MainLayout>
    );
}

export default App;
