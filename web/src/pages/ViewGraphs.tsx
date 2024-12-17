import React, { useState } from 'react';
import { Chart as ChartJS, LineElement, PointElement, LinearScale, Title, Tooltip, Legend, CategoryScale
} from 'chart.js';
import { Line } from 'react-chartjs-2';
import axios from '../axiosConfig.tsx';
import useFileHandler from "../components/useFileHandler.tsx";
import useTable from "../components/useTable.tsx";

ChartJS.register( LineElement, PointElement, LinearScale, CategoryScale, Title, Tooltip, Legend);

const ViewGraphs: React.FC = () => {

    const [data, setData] = useState<any>({
        labels: [],
        datasets: [
            {
                label: 'График',
                data: [],
                fill: false,
                borderColor: 'rgb(75, 192, 192)',
                tension: 0.1,
            },
        ],
    });
    const [xValue, setXValue] = useState('');
    const [fXValue, setFXValue] = useState<string | null>(null);
    const server = 'http://localhost:8080';

    const { handleFileUpload, showError, openModal, Modal, Messages } = useFileHandler(server);
    const { points, setPoints, renderTable } = useTable([{ x: '', y: '' }]);

    const handleGraphGeneration = () => {
        const labels = points
            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
            .filter(point => point.x !== '' && point.y !== '')
            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
            .map(point => point.x);

        const dataPoints = points
            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
            .filter(point => point.x !== '' && point.y !== '')
            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
            .map(point => ({ x: point.x, y: point.y }));

        const nonEmptyPoints = points.slice(0, -1);
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-expect-error
        if (nonEmptyPoints.some(point => point.x === '' || point.y === '')) {
            showError('Все строки должны быть полностью заполнены!');
        } else if (nonEmptyPoints.length < 2) {
            showError('Должно быть не менее двух полных строк!');
            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
        } else if (!nonEmptyPoints.every((point, i) => i === 0 || point.x > nonEmptyPoints[i - 1].x)) {
            showError('Значения X должны быть в возрастающем порядке!');
        } else {
            setData({
                labels,
                datasets: [
                    {
                        label: 'График',
                        data: dataPoints,
                        fill: false,
                        borderColor: 'rgb(75, 192, 192)',
                        tension: 0.1,
                    },
                ],
            });
        }
    };

    const handleCalculateFX = async () => {
        const nonEmptyPoints = points.slice(0, -1);
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-expect-error
        if (nonEmptyPoints.some(point => point.x === '' || point.y === '')) {
            showError('Все строки должны быть полностью заполнены!');
        } else if (nonEmptyPoints.length < 2) {
            showError('Должно быть не менее двух полных строк!');
            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
        } else if (!nonEmptyPoints.every((point, i) => i === 0 || point.x > nonEmptyPoints[i - 1].x)) {
            showError('Значения X должны быть в возрастающем порядке!');
        } else {
            if (xValue === '') {
                setFXValue(null);
                return;
            }

            const x = parseFloat(xValue);
            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
            const xValues = nonEmptyPoints.map(point => point.x);
            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
            const yValues = nonEmptyPoints.map(point => point.y);

            try {
                const response = await axios.post(`${server}/apply/${x}`, {
                    xValues,
                    yValues,
                });

                const result = JSON.parse(response.data);
                setFXValue(`f(${x}) = ${result}`);
                console.log(response.data)
            } catch (error) {
                console.error('Ошибка при вычислении f(X):', error);
                showError('Ошибка при вычислении f(X).');
            }
        }
    };

    return (
        <div className="p-8 h-full backdrop-blur-sm bg-black/30 rounded-3xl shadow-lg">
            <Messages/>
            <h1 className="text-2xl font-bold text-gray-300 mb-4">Исследование табличной функции</h1>
            <div className="flex flex-col space-y-4">
                <div className="flex justify-between space-x-2 mb-2">
                    <div className="flex flex-1 flex-col space-y-2 mt-[24px]">
                        <button
                            onClick={() => document.getElementById('fileInput')?.click()}
                            className="h-[40px] bg-teal-600 text-white rounded px-4 py-2 transition-all hover:bg-teal-400 hover:scale-105"
                        >
                            Загрузить таблицу
                        </button>
                        <input
                            type="file"
                            id="fileInput"
                            style={{display: 'none'}}
                            accept=".json,.bin,.xml"
                            onChange={(e) => handleFileUpload(e, setPoints)}
                        />
                        <button
                            onClick={() => openModal(points)}
                            className="h-[40px] bg-teal-600 text-white rounded px-4 py-2 transition-all hover:bg-teal-400 hover:scale-105"
                        >
                            Сохранить таблицу
                        </button>
                    </div>
                    <div className="flex-1">
                        <label className="block text-gray-300">Введите X для вычисления f(X):</label>
                        <input
                            type="number"
                            step="any"
                            value={xValue}
                            onChange={(e) => setXValue(e.target.value)}
                            className="h-[40px] w-full bg-gray-800 text-gray-300 border border-gray-700 rounded px-2 py-1 mb-2 focus:outline-none focus:ring-2 focus:ring-teal-600 focus:ring-offset-2 focus:ring-offset-gray-800 transition-all"
                        />
                        <div className="flex items-center space-x-4">
                            <button
                                onClick={handleCalculateFX}
                                className="h-[40px] bg-green-600 text-white rounded px-4 py-2 transition-all hover:bg-green-400 hover:scale-105"
                            >
                                Вычислить f(X)
                            </button>
                            {fXValue !== null && (
                                <div className="text-lg text-gray-300">
                                    f(X) = {fXValue}
                                </div>
                            )}
                        </div>
                    </div>
                </div>
                {renderTable()}
                <button
                    onClick={handleGraphGeneration}
                    className="bg-teal-600 text-white rounded px-4 py-2 transition-all hover:bg-teal-400 hover:scale-105"
                >
                    Построить график
                </button>
                <div className="mb-4 pb-12">
                    <Line data={data} options={{responsive: true, plugins: {legend: {position: 'top'}}}}/>
                </div>
            </div>
            <Modal points={points}/>
        </div>
    );
};

export default ViewGraphs;