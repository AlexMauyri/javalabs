import React, { useEffect, useState } from 'react';
import Cookies from 'js-cookie';
import axios from '../axiosConfig.tsx';
import useTable from '../components/useTable';
import useFileHandler from "../components/useFileHandler.tsx";

const CreateTableFunction: React.FC = () => {
    const [functionType, setFunctionType] = useState<string>('Квадратичная функция');
    const [xRange, setXRange] = useState({ start: '', end: '' });
    const [pointCount, setPointCount] = useState('');
    const [availableFunctions, setAvailableFunctions] = useState<string[]>([]);
    const server = 'http://localhost:8080';

    const { showError, openModal, Modal, Messages } = useFileHandler(server);
    const { points, setPoints, renderTable } = useTable([{ x: '', y: '' }]);

    useEffect(() => {
        const fetchFunctions = async () => {
            console.log(Cookies.get('authToken'));

            try {
                const response = await axios.get(`${server}/getFunctions`, {
                    withCredentials: true,
                    headers: {
                        Authorization: `Bearer ${Cookies.get('authToken')}`,
                    },
                });
                setAvailableFunctions(response.data);
            } catch (error) {
                console.error('Error fetching functions:', error);
                showError('Ошибка при загрузке доступных функций.');
            }
        };

        fetchFunctions();
    }, []);

    const generatePoints = async () => {
        const start = parseFloat(xRange.start);
        const end = parseFloat(xRange.end);
        const count = parseInt(pointCount, 10);

        if (start > end) {
            showError('Начальное значение диапазона не может быть больше конечного!');
            return;
        }
        if (!Number.isInteger(count)) {
            showError('Количество точек должно быть целым числом!');
            return;
        }
        if (count < 2) {
            showError('Количество точек должно быть не менее двух!');
            return;
        }

        try {
            const response = await axios.post(
                `${server}/createTabulatedFunctionWithFunctionJSON`,
                {
                    functionName: functionType,
                    from: start,
                    to: end,
                    amountOfPoints: count,
                },
                {
                    withCredentials: true,
                }
            );

            const { xValues, yValues } = response.data;
            const generatedPoints = xValues.map((x: number, index: number) => ({
                x,
                y: yValues[index],
            }));

            setPoints([...generatedPoints, { x: '', y: '' }]);
        } catch (error) {
            console.error('Error generating points:', error);
            showError('Ошибка при генерации точек.');
        }
    };

    const clearPoints = () => {
        setPoints([{ x: '', y: '' }]);
    };

    return (
        <div className="p-8 h-full flex flex-col backdrop-blur-sm bg-black/30 rounded-3xl shadow-lg">
            <Messages/>
            <h1 className="text-2xl font-bold text-gray-300 mb-4">Создание табличной функции</h1>
            <div className="grid grid-cols-2 gap-4 flex-grow">
                <div className="flex flex-col h-full">
                    <h2 className="text-lg font-semibold text-gray-300 mb-2">Ввод точек вручную</h2>
                    {renderTable()}
                    <button
                        onClick={() => openModal(points)}
                        className="bg-teal-600 text-white rounded px-4 py-2 mt-4 transition-all hover:bg-teal-400 hover:scale-105"
                    >
                        Сохранить таблицу
                    </button>
                    <button
                        onClick={clearPoints}
                        className="mt-2 bg-red-600 text-white rounded px-4 py-2 transition-all hover:bg-red-400 hover:scale-105"
                    >
                        Очистить
                    </button>
                </div>
                <div>
                    <h2 className="text-lg font-semibold text-gray-300 mb-2">Генерация точек</h2>
                    <div className="mb-2">
                        <label className="block text-gray-300">Базовая функция</label>
                        <select
                            value={functionType}
                            onChange={(e) => setFunctionType(e.target.value)}
                            className="w-full bg-gray-800 text-gray-300 border border-gray-700 rounded px-2 py-1 focus:outline-none focus:ring-2 focus:ring-teal-600 focus:ring-offset-2 focus:ring-offset-gray-800 transition-all"
                        >
                            {availableFunctions
                                .sort((a, b) => a.localeCompare(b))
                                .map((func, index) => (
                                    <option key={index} value={func} className="bg-gray-800 text-gray-300">
                                        {func}
                                    </option>
                                ))}
                        </select>
                    </div>
                    <div className="mb-2">
                        <label className="block text-gray-300">Диапазон X</label>
                        <input
                            type="number"
                            step="any"
                            placeholder="Начало"
                            value={xRange.start}
                            onChange={(e) => setXRange({...xRange, start: e.target.value})}
                            className="w-full mb-2 bg-gray-800 text-gray-300 border border-gray-700 rounded px-2 py-1 focus:outline-none focus:ring-2 focus:ring-teal-600 focus:ring-offset-2 focus:ring-offset-gray-800 transition-all"
                            style={{appearance: 'textfield'}}
                        />
                        <input
                            type="number"
                            step="any"
                            placeholder="Конец"
                            value={xRange.end}
                            onChange={(e) => setXRange({...xRange, end: e.target.value})}
                            className="w-full bg-gray-800 text-gray-300 border border-gray-700 rounded px-2 py-1 focus:outline-none focus:ring-2 focus:ring-teal-600 focus:ring-offset-2 focus:ring-offset-gray-800 transition-all"
                            style={{appearance: 'textfield'}}
                        />
                    </div>
                    <div className="mb-2">
                        <label className="block text-gray-300">Количество точек</label>
                        <input
                            type="number"
                            step="any"
                            value={pointCount}
                            onChange={(e) => setPointCount(e.target.value)}
                            className="w-full bg-gray-800 text-gray-300 border border-gray-700 rounded px-2 py-1 focus:outline-none focus:ring-2 focus:ring-teal-600 focus:ring-offset-2 focus:ring-offset-gray-800 transition-all"
                            style={{appearance: 'textfield'}}
                        />
                    </div>
                    <div className="space-x-2">
                        <button
                            onClick={generatePoints}
                            className="bg-green-600 text-white rounded px-4 py-2 transition-all hover:bg-green-400 hover:scale-105"
                        >
                            Сгенерировать
                        </button>
                    </div>
                </div>
            </div>

            <Modal points={points}/>
        </div>
    );
};

export default CreateTableFunction;