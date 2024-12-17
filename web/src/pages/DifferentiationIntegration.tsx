import React, {useState} from 'react';
import useFileHandler from "../components/useFileHandler.tsx";
import axios from "../axiosConfig.tsx";
import useTable from "../components/useTable.tsx";

const DifferentiationIntegration: React.FC = () => {
    const [operation, setOperation] = useState<string>('derivative');
    const [result, setResult] = useState<string | null>(null);
    const [threads, setThreads] = useState<number | ''>('');
    const [selectedPoints, setSelectedPoints] = useState<number>();
    const server = 'http://localhost:8080';

    const {handleFileUpload, showError, openModal, Modal, Messages} = useFileHandler(server);
    const {points: points1, setPoints: setPoints1, renderTable: renderTable1} = useTable([{x: '', y: ''}]);
    const {points: points2, setPoints: setPoints2, renderTable: renderTable2} = useTable([{x: '', y: ''}]);

    const performOperation = async () => {
        if (!points1 || points1.length < 2) {
            showError('Пожалуйста, загрузите функцию или добавьте хотя бы два значения.');
            return;
        }

        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-expect-error
        const xValues = points1.map(point => point.x).filter(x => x !== '');
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-expect-error
        const yValues = points1.map(point => point.y).filter(y => y !== '');

        try {
            if (operation === 'derivative') {
                const response = await axios.post(`${server}/doDifferential`, {xValues, yValues});
                setPoints2(response.data.xValues.map((x: number, i: number) => ({x, y: response.data.yValues[i]})));
                setResult(' ');
            } else if (operation === 'integral') {
                if (!threads) {
                    showError('Пожалуйста, укажите количество потоков.');
                    return;
                }
                const response = await axios.post(`${server}/doIntegral/${threads}`, {xValues, yValues});
                const integralResult = JSON.parse(response.data);
                setResult(`${integralResult}`);
            }
        } catch (error) {
            console.error('Ошибка при выполнении операции:', error);
            showError('Ошибка при выполнении операции.');
        }
    };

    return (
        <div className="p-8 h-full flex flex-col backdrop-blur-sm bg-black/30 rounded-3xl shadow-lg">
            <Messages/>
            <h1 className="text-2xl font-bold text-gray-300 mb-4">Дифференцирование & Интегрирование</h1>
            <div className="p-8 h-full grid grid-cols-2 gap-4 backdrop-blur-sm bg-black/30 rounded-3xl shadow-lg">
                <div className="flex flex-col items-center">
                    <div className="flex justify-between space-x-2 mb-2 w-full">
                        <button
                            onClick={() => document.getElementById('fileInput')?.click()}
                            className="flex-1 bg-teal-600 text-white rounded px-4 py-2 transition-all hover:bg-teal-400 hover:scale-105"
                        >
                            Загрузить таблицу
                        </button>
                        <input
                            type="file"
                            id="fileInput"
                            style={{display: 'none'}}
                            accept=".json,.bin,.xml"
                            onChange={(e) => handleFileUpload(e, setPoints1)}
                        />
                        <button
                            onClick={() => {
                                setSelectedPoints(1);
                                openModal(points1);
                            }}
                            className="flex-1 bg-teal-600 text-white rounded px-4 py-2 transition-all hover:bg-teal-400 hover:scale-105"
                        >
                            Сохранить таблицу
                        </button>
                    </div>
                    {renderTable1()}
                </div>
                <div className="flex flex-col items-center space-y-4">
                    <div>
                        <label className="block text-gray-300">Выберите операцию:</label>
                        <select
                            value={operation}
                            onChange={(e) => setOperation(e.target.value)}
                            className="w-full bg-gray-800 text-gray-300 border border-gray-700 rounded px-2 py-1 focus:outline-none focus:ring-2 focus:ring-teal-600 focus:ring-offset-2 focus:ring-offset-gray-800 transition-all"
                        >
                            <option value="derivative" className="bg-gray-800 text-gray-300">
                                Производная
                            </option>
                            <option value="integral" className="bg-gray-800 text-gray-300">
                                Определённый интеграл
                            </option>
                        </select>
                    </div>
                    {operation === 'integral' && (
                        <div className="w-1/2">
                            <label className="block text-gray-300">Количество потоков для исполнения:</label>
                            <input
                                type="number"
                                value={threads}
                                onChange={(e) => setThreads(parseInt(e.target.value) || '')}
                                className="w-full bg-gray-800 text-gray-300 border border-gray-700 rounded px-2 py-1 focus:outline-none focus:ring-2 focus:ring-teal-600 focus:ring-offset-2 focus:ring-offset-gray-800 transition-all"
                                min="1"
                            />
                        </div>
                    )}
                    <button
                        onClick={performOperation}
                        className="bg-green-600 text-white rounded px-4 py-2 transition-all hover:bg-green-400 hover:scale-105"
                    >
                        Выполнить
                    </button>
                    {result && (
                        <div className="mt-4 w-full items-center">
                            {operation === 'integral' ? (
                                <div className="text-center text-lg text-gray-300">
                                    <strong>Результат интеграла:</strong> {result}
                                </div>
                            ) : (
                                operation === 'derivative' && points2.length > 0 && (
                                    <div className="w-full flex flex-col items-center">
                                        {renderTable2()}
                                        <button
                                            onClick={() => {
                                                setSelectedPoints(2);
                                                openModal(points2);
                                            }}
                                            className="mt-2 bg-teal-600 text-white rounded px-4 py-2 transition-all hover:bg-teal-400 hover:scale-105"
                                        >
                                            Сохранить таблицу производной
                                        </button>
                                    </div>
                                )
                            )}
                        </div>
                    )}
                </div>

                <Modal points={(selectedPoints === 1 ? points1 : points2)}/>
            </div>
        </div>
    );
};

export default DifferentiationIntegration;