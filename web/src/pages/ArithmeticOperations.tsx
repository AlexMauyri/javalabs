import React, {useState} from 'react';
import useFileHandler from "../components/useFileHandler.tsx";
import useTable from "../components/useTable.tsx";

const ArithmeticOperations: React.FC = () => {
    const [operation, setOperation] = useState<string>('+');
    const [selectedPoints, setSelectedPoints] = useState<number>();
    const server = 'http://localhost:8080';

    const {handleFileUpload, showError, openModal, Modal, Messages} = useFileHandler(server);
    const {points: points1, setPoints: setPoints1, renderTable: renderTable1} = useTable([{x: '', y: ''}]);
    const {points: points2, setPoints: setPoints2, renderTable: renderTable2} = useTable([{x: '', y: ''}]);
    const {points: result, setPoints: setResult} = useTable([{x: '', y: ''}]);

    const performOperation = () => {
        const nonEmptyPoints1 = points1.slice(0, -1);
        const nonEmptyPoints2 = points2.slice(0, -1);

        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-expect-error
        if (nonEmptyPoints1.some(point => point.x === '' || point.y === '') || nonEmptyPoints2.some(point => point.x === '' || point.y === '')) {
            showError('Все строки должны быть полностью заполнены!');
        } else if (nonEmptyPoints1.length < 2 || nonEmptyPoints2.length < 2) {
            showError('Должно быть не менее двух полных строк!');
            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
        } else if (!nonEmptyPoints1.every((point, i) => i === 0 || point.x > nonEmptyPoints1[i - 1].x) || !nonEmptyPoints2.every((point, i) => i === 0 || point.x > nonEmptyPoints2[i - 1].x)) {
            showError('Значения X должны быть в возрастающем порядке!');
        } else if (nonEmptyPoints1.length !== nonEmptyPoints2.length) {
            showError("Таблицы должны иметь одинаковое количество точек!");
            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
        } else if (!nonEmptyPoints1.every((point, index) => point.x === nonEmptyPoints2[index].x)) {
            showError("Значения X в обеих таблицах должны совпадать!");
        } else {

            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
            const newResult = points1.map((point1, index) => {
                const point2 = points2[index];
                let y: number;
                switch (operation) {
                    case '+':
                        y = (point1.y as number) + (point2.y as number);
                        break;
                    case '-':
                        y = (point1.y as number) - (point2.y as number);
                        break;
                    case '*':
                        y = (point1.y as number) * (point2.y as number);
                        break;
                    case '/':
                        y = (point2.y as number) !== 0 ? (point1.y as number) / (point2.y as number) : 0;
                        break;
                    default:
                        y = 0;
                }
                return {x: point1.x, y};
            });

            if (newResult.length > 1 && (newResult[newResult.length - 1].x === '' || newResult[newResult.length - 1].y === '')) {
                newResult.pop();
            }

            setResult(newResult);
        }
    };

    return (
        <div className="p-8 h-full flex flex-col backdrop-blur-sm bg-black/30 rounded-3xl shadow-lg">
            <Messages/>
            <h1 className="text-2xl font-bold text-gray-300 mb-4">Арифметические операции</h1>
            <div className="flex space-x-4">
                <div className="flex flex-col items-center w-1/3">
                    <div className="flex-row flex space-x-4">
                        <button
                            onClick={() => document.getElementById('fileInputFirst')?.click()}
                            className="mb-2 bg-teal-600 text-white rounded px-4 py-2 h-16 transition-all hover:bg-teal-400 hover:scale-105"
                        >
                            Загрузить таблицу
                        </button>
                        <input
                            type="file"
                            id="fileInputFirst"
                            style={{display: 'none'}}
                            accept=".json,.bin,.xml"
                            onChange={(e) => handleFileUpload(e, setPoints1)}
                        />
                        <button
                            onClick={() => {
                                setSelectedPoints(1);
                                openModal(points1);
                            }}
                            className="bg-teal-600 text-white rounded px-4 py-2 h-16 transition-all hover:bg-teal-400 hover:scale-105"
                        >
                            Сохранить таблицу
                        </button>
                    </div>
                    {renderTable1()}
                </div>

                <div className="flex flex-col justify-center">
                    <button
                        className=" outline outline-2 outline-teal-800 text-white scale-100 font-extrabold rounded px-4 py-2 min-w-11 transition-all hover:outline-teal-400 hover:bg-teal-600 hover:scale-110"
                        onClick={() =>
                            setOperation((prev) =>
                                prev === '+' ? '-' : prev === '-' ? '*' : prev === '*' ? '/' : '+'
                            )
                        }
                    >
                        {operation}
                    </button>
                </div>

                <div className="flex flex-col items-center w-1/3">
                    <div className="flex-row flex space-x-4">
                        <button
                            onClick={() => document.getElementById('fileInputSecond')?.click()}
                            className="mb-2 bg-teal-600 text-white rounded px-4 py-2 h-16 transition-all hover:bg-teal-400 hover:scale-105"
                        >
                            Загрузить таблицу
                        </button>
                        <input
                            type="file"
                            id="fileInputSecond"
                            style={{display: 'none'}}
                            accept=".json,.bin,.xml"
                            onChange={(e) => handleFileUpload(e, setPoints2)}
                        />
                        <button
                            onClick={() => {
                                setSelectedPoints(2);
                                openModal(points2);
                            }}
                            className="bg-teal-600 text-white rounded px-4 py-2 h-16 transition-all hover:bg-teal-400 hover:scale-105"
                        >
                            Сохранить таблицу
                        </button>
                    </div>
                    {renderTable2()}
                </div>

                <div className="flex flex-col justify-center">
                    <span className="text-3xl text-gray-300 mb-4">=</span>
                </div>

                <div className="flex flex-col items-center w-1/3">
                    <div className="flex-row flex space-x-4">
                        <button
                            onClick={performOperation}
                            className="mb-2 bg-green-600 text-white rounded px-4 py-2 h-16 transition-all hover:bg-green-400 hover:scale-105"
                        >
                            Выполнить
                        </button>
                        <button
                            onClick={() => {
                                setSelectedPoints(3);
                                openModal(result);
                            }}
                            className="bg-teal-600 text-white rounded px-4 py-2 h-16 transition-all hover:bg-teal-400 hover:scale-105"
                        >
                            Сохранить таблицу
                        </button>
                    </div>

                    <div className="mt-2 flex-grow overflow-hidden relative min-w-full max-w-full" style={{maxHeight: '450px'}}>
                        <div
                            className="overflow-y-auto custom-scrollbar backdrop-blur-sm bg-black/30 rounded-3xl shadow-lg"
                            style={{maxHeight: '400px'}}>
                            <table className="min-w-full border-collapse border-gray-700 border-2 rounded-3xl">
                                <thead className="sticky top-0 bg-gray-800/80 backdrop-blur-sm">
                                <tr>
                                    <th className="border border-gray-700 bg-gray-800/80 text-gray-300 px-4 py-2 relative w-8 pt-3 pb-3">#</th>
                                    <th className="border border-gray-700 bg-gray-800/80 text-gray-300 px-4 py-2 relative">X</th>
                                    <th className="border border-gray-700 bg-gray-800/80 text-gray-300 px-4 py-2">Y</th>
                                </tr>
                                </thead>
                                <tbody>
                                {// eslint-disable-next-line @typescript-eslint/ban-ts-comment
                                    // @ts-expect-error
                                    result.map((point, index) => (
                                    <tr
                                        key={index}
                                        className={`transition-all duration-300 ${
                                            index % 2 === 0 ? 'bg-gray-800/50' : 'bg-gray-700/50'
                                        } hover:bg-teal-600/50`}
                                    >
                                        <td className="border border-gray-700 text-gray-300 px-4 py-2 text-center">{index + 1}</td>
                                        <td className="border border-gray-700 text-gray-300 px-4 py-2">{point.x}</td>
                                        <td className="border border-gray-700 text-gray-300 px-4 py-2">{point.y}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <Modal points={(selectedPoints === 1 ? points1 : selectedPoints === 2 ? points2 : result)}/>
        </div>
    );
};

export default ArithmeticOperations;