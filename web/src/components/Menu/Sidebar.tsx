import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';
import axios from '../../axiosConfig.tsx';
import { Settings, LogOut } from 'lucide-react';

const Sidebar: React.FC = () => {
    const navigate = useNavigate();
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedFactory, setSelectedFactory] = useState<string | null>(null);


    useEffect(() => {
        const factoryType = Cookies.get('fabricType');
        setSelectedFactory(factoryType || 'ARRAY');
    }, []);

    const handleLogout = () => {
        Cookies.remove('authToken');
        delete axios.defaults.headers.common['Authorization'];
        navigate('/');
    };

    const handleSettingsClick = () => {
        setIsModalOpen(true);
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
    };

    const handleFactoryChange = (factoryType: string) => {
        Cookies.set('fabricType', factoryType);
        setSelectedFactory(factoryType);
        setIsModalOpen(false);
    };

    return (
        <div className="bg-gray-800 text-white w-64 h-screen flex flex-col backdrop-blur-sm bg-black/30 shadow-lg">
            <div className="w-64 overflow-y-auto custom-scrollbar items-center justify-center flex-col flex">
                <h2 className="font-bold p-4 text-2xl">FunctionOverdose</h2>
                <nav className="flex flex-col p-4 space-y-2">
                    <Link
                        to="/app/create-table-function"
                        className="hover:bg-teal-600 p-2 rounded transition-all duration-300"
                    >
                        Создание табличной функции
                    </Link>
                    <Link
                        to="/app/create-complex-function"
                        className="hover:bg-teal-600 p-2 rounded transition-all duration-300"
                    >
                        Создание сложной функции
                    </Link>
                    <Link
                        to="/app/view-graphs"
                        className="hover:bg-teal-600 p-2 rounded transition-all duration-300"
                    >
                        Исследование табличной функции
                    </Link>
                    <Link
                        to="/app/arithmetic-operations"
                        className="hover:bg-teal-600 p-2 rounded transition-all duration-300"
                    >
                        Арифметические операции
                    </Link>
                    <Link
                        to="/app/differentiation-integration"
                        className="hover:bg-teal-600 p-2 rounded transition-all duration-300"
                    >
                        Дифференцирование & Интегрирование
                    </Link>
                </nav>
            </div>
            <div className="p-4 flex justify-between items-center">
                <button
                    onClick={handleLogout}
                    className="text-red-500 hover:text-red-400 px-4 py-2 rounded transition-all duration-300"
                >
                    <LogOut size={28}/>
                </button>
                <button
                    onClick={handleSettingsClick}
                    className="text-gray-300 hover:text-white transition-all duration-300"
                >
                    <Settings size={28}/>
                </button>
            </div>

            {isModalOpen && (
                <div
                    className="fixed inset-0 flex items-center justify-center bg-black/50 z-50 backdrop-blur-sm motion-preset-fade">
                    <div className="bg-gray-800 p-6 rounded-3xl shadow-lg motion-preset-expand">
                        <h2 className="text-lg font-bold mb-4 text-gray-300">Настройки фабрики</h2>

                        <div className="mb-4">
                            <p className="text-gray-400">Текущая фабрика: <span
                                className="font-bold text-gray-300">{selectedFactory}</span></p>
                        </div>

                        <div className="flex flex-col space-y-2">
                            <button
                                onClick={() => handleFactoryChange('ARRAY')}
                                className={`${selectedFactory === 'ARRAY' ? 'bg-teal-600' : 'bg-blue-600'} text-white px-4 py-2 rounded transition-all duration-300 hover:bg-teal-400`}
                            >
                                Array Factory
                            </button>
                            <button
                                onClick={() => handleFactoryChange('LINKEDLIST')}
                                className={`${selectedFactory === 'LINKEDLIST' ? 'bg-teal-600' : 'bg-blue-600'} text-white px-4 py-2 rounded transition-all duration-300 hover:bg-teal-400`}
                            >
                                LinkedList Factory
                            </button>
                        </div>
                        <button
                            onClick={handleCloseModal}
                            className="mt-4 w-full bg-gray-700 text-gray-300 px-4 py-2 rounded transition-all duration-300 hover:bg-gray-600"
                        >
                            Закрыть
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Sidebar;