import React from 'react';
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import Auth from './components/Auth/Auth.tsx';
import Sidebar from './components/Menu/Sidebar.tsx';
import CreateTableFunction from './pages/CreateTableFunction';
import CreateComplexFunction from "./pages/CreateComplexFunction.tsx";
import ViewGraphs from "./pages/ViewGraphs.tsx";
import ArithmeticOperations from "./pages/ArithmeticOperations.tsx";
import DifferentiationIntegration from "./pages/DifferentiationIntegration.tsx";
import ProtectedRoute from './components/ProtectedRoute.tsx';
import AuthProvider from './components/Auth/AuthProvider.tsx';
import MatrixBackground from "./components/MatrixBackground.tsx";

const MainLayout: React.FC = () => {
    return (
        <div className="relative">
            {/* Фон с эффектом размытия */}
            <div className="absolute inset-1">
                <MatrixBackground/>
            </div>

            <div className="relative flex h-screen overflow-hidden">
                <div className="absolute inset-0 backdrop-blur-[2px] bg-black/30"></div>

                <div className="z-10 flex w-full h-full">
                    <Sidebar/>
                    <div className="flex-grow p-8 overflow-y-auto">
                        <Routes>
                            <Route path="/" element={<WelcomePlaceholder/>}/>
                            <Route path="create-table-function" element={<ProtectedRoute component={CreateTableFunction}/>}/>
                            <Route path="create-complex-function" element={<ProtectedRoute component={CreateComplexFunction}/>}/>
                            <Route path="view-graphs" element={<ProtectedRoute component={ViewGraphs}/>}/>
                            <Route path="arithmetic-operations" element={<ProtectedRoute component={ArithmeticOperations}/>}/>
                            <Route path="differentiation-integration" element={<ProtectedRoute component={DifferentiationIntegration}/>}/>
                        </Routes>
                    </div>
                </div>
            </div>
        </div>
    );
};

const WelcomePlaceholder: React.FC = () => {
    return (
        <div className="text-center">
            <h1 className="text-3xl text-white font-bold mb-4">Добро пожаловать в наше приложение!</h1>
            <p className="text-white">Выберите действие на боковой панели, чтобы начать работу.</p>
        </div>
    );
};

const AppRoutes: React.FC = () => {
    return (
        <Router>
            <AuthProvider>
                <Routes>
                    <Route path="/" element={<Auth/>}/>
                    <Route path="app/*" element={<ProtectedRoute component={MainLayout}/>}/>
                </Routes>
            </AuthProvider>
        </Router>
    );
};

export default AppRoutes;