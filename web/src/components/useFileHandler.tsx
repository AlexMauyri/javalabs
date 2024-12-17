import React, {useCallback, useState} from 'react';
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-expect-error
import { saveAs } from 'file-saver';
import axios from "../axiosConfig.tsx";
import {XCircleIcon} from "lucide-react";

const useFileHandler = (server: string) => {
    const [errorMessage, setErrorMessage] = useState<string | null>(null);
    const [successMessage, setSuccessMessage] = useState<string | null>(null);
    const [isCloseButtonVisible, setIsCloseButtonVisible] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const openModal = useCallback((points: Array<{ x: number | ''; y: number | '' }>) => {
        const nonEmptyPoints = points.filter(point => point.x !== '' && point.y !== '');

        if (nonEmptyPoints.some(point => point.x === '' || point.y === '')) {
            showError('Все строки должны быть полностью заполнены!');
        } else if (nonEmptyPoints.length < 2) {
            showError('Должно быть не менее двух полных строк!');
        } else if (!nonEmptyPoints.every((point, i) => i === 0 || point.x > nonEmptyPoints[i - 1].x)) {
            showError('Значения X должны быть в возрастающем порядке!');
        } else {
            setIsModalOpen(true);
        }
    }, []);

    const closeModal = useCallback(() => {
        setIsModalOpen(false);
    }, []);

    const showError = (message: string) => {
        setErrorMessage(message);
        setIsCloseButtonVisible(false);
        setTimeout(() => setIsCloseButtonVisible(true), 1500);
    };

    const closeError = () => {
        setErrorMessage(null);
        setIsCloseButtonVisible(false);
    };

    const showSuccess = (message: string) => {
        setErrorMessage(null);
        setSuccessMessage(message);
        setTimeout(() => setSuccessMessage(null), 1500);
    };

    const handleFileUpload = async (event: React.ChangeEvent<HTMLInputElement>, setPoints: (points: any) => void) => {
        const file = event.target.files?.[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = async () => {
                const result = reader.result;

                if (file.type === 'application/json' || file.name.endsWith('.json')) {
                    try {
                        const loadedPoints = JSON.parse(result as string);

                        if (!validateFunctionData(loadedPoints)) {
                            return;
                        }

                        setPoints(loadedPoints.xValues.map((x: number, i: number) => ({ x, y: loadedPoints.yValues[i] })).concat({ x: '', y: '' }));
                        showSuccess('Файл успешно загружен.');
                    } catch (e) {
                        console.error('Ошибка при обработке JSON:', e);
                        showError('Ошибка при обработке JSON: некорректный формат данных.');
                    }
                } else if (file.type === 'application/xml' || file.name.endsWith('.xml')) {
                    try {
                        const response = await axios.post(`${server}/convertFromXML`, result as string, {
                            headers: {
                                'Content-Type': 'application/xml',
                            },
                        });

                        if (!validateFunctionData(response.data)) {
                            return;
                        }

                        setPoints(response.data.xValues.map((x: number, i: number) => ({ x, y: response.data.yValues[i] })).concat({ x: '', y: '' }));
                        showSuccess('Файл успешно загружен.');
                    } catch (e) {
                        console.error('Ошибка при обработке XML:', e);
                        showError('Ошибка при обработке XML: некорректный формат данных.');
                    }
                } else {
                    try {
                        const response = await axios.post(`${server}/convertFromBLOB`, result as ArrayBuffer, {
                            headers: {
                                'Content-Type': 'application/octet-stream',
                            },
                        });

                        if (!validateFunctionData(response.data)) {
                            return;
                        }

                        setPoints(response.data.xValues.map((x: number, i: number) => ({ x, y: response.data.yValues[i] })).concat({ x: '', y: '' }));
                        showSuccess('Файл успешно загружен.');
                    } catch (e) {
                        console.error('Ошибка при обработке BLOB:', e);
                        showError('Ошибка при обработке BLOB: некорректный формат данных.');
                    }
                }
            };

            if (file.type === 'application/json' || file.name.endsWith('.json')) {
                reader.readAsText(file);
            } else if (file.type === 'application/xml' || file.name.endsWith('.xml')) {
                reader.readAsText(file);
            } else {
                reader.readAsArrayBuffer(file);
            }

            event.target.value = '';
        }
    };

    const handleSave = async (format: string, points: Array<{ x: number | ''; y: number | '' }>) => {
        const xValues = points.map(point => point.x).filter(x => x !== '');
        const yValues = points.map(point => point.y).filter(y => y !== '');

        closeModal()

        try {
            let response;
            let fileName;
            let fileType;

            switch (format) {
                case 'Byte':
                    response = await axios.post(
                        `${server}/createTabulatedFunctionWithTableByte`,
                        { xValues, yValues },
                        {
                            responseType: 'arraybuffer',
                            withCredentials: true
                        }
                    );
                    fileName = 'function.bin';
                    fileType = 'application/octet-stream';
                    break;
                case 'JSON':
                    response = await axios.post(
                        `${server}/createTabulatedFunctionWithTableJSON`,
                        { xValues, yValues },
                        {
                            withCredentials: true
                        }
                    );
                    fileName = 'function.json';
                    fileType = 'application/json';
                    break;
                case 'XML':
                    response = await axios.post(
                        `${server}/createTabulatedFunctionWithTableXML`,
                        { xValues, yValues },
                        {
                            withCredentials: true
                        }
                    );
                    fileName = 'function.xml';
                    fileType = 'application/xml';
                    break;
                default:
                    console.error('Unknown format:', format);
                    return;
            }

            const fileData =
                format === 'JSON' ? JSON.stringify(response.data, null, 2) : response.data;
            const blob = new Blob([fileData], { type: fileType });
            saveAs(blob, fileName);

            showSuccess('Функция успешно сохранена!');
        } catch (error) {
            console.error('Error saving function:', error);
            showError('Ошибка при сохранении функций.');
        }
    };

    const validateFunctionData = (data: any): boolean => {
        const validClasses = [
            'ru.ssau.tk.DoubleA.javalabs.functions.LinkedListTabulatedFunction',
            'ru.ssau.tk.DoubleA.javalabs.functions.ArrayTabulatedFunction',
        ];
        if (!validClasses.includes(data['@class'])) {
            showError('Ошибка: некорректное значение "@class".');
            return false;
        }

        if (data.xValues.length !== data.yValues.length) {
            showError('Ошибка: количество значений в массивах xValues и yValues не совпадает.');
            return false;
        }

        for (let i = 0; i < data.xValues.length - 1; i++) {
            if (data.xValues[i] >= data.xValues[i + 1]) {
                showError('Ошибка: значения в xValues не идут строго по возрастанию.');
                return false;
            }
        }

        if (data.count !== data.xValues.length) {
            showError('Ошибка: значение "count" не совпадает с количеством пар xValues-yValues.');
            return false;
        }

        return true;
    };

    const Modal = ({ points }: { points: Array<{ x: number | ''; y: number | '' }> }) => (
        isModalOpen && (
            <div className="fixed inset-0 flex items-center justify-center bg-black/50 backdrop-blur-sm motion-preset-fade-sm">
                <div className="bg-gray-800 p-6 rounded-3xl shadow-lg motion-preset-expand">
                    <h2 className="text-xl font-bold text-gray-300 mb-4">Выберите тип сохранения</h2>
                    <div className="space-y-2">
                        <button
                            onClick={() => handleSave('Byte', points)}
                            className="bg-teal-600 text-white rounded px-4 py-2 w-full transition-all hover:bg-teal-400 hover:scale-105"
                        >
                            Byte
                        </button>
                        <button
                            onClick={() => handleSave('JSON', points)}
                            className="bg-teal-600 text-white rounded px-4 py-2 w-full transition-all hover:bg-teal-400 hover:scale-105"
                        >
                            JSON
                        </button>
                        <button
                            onClick={() => handleSave('XML', points)}
                            className="bg-teal-600 text-white rounded px-4 py-2 w-full transition-all hover:bg-teal-400 hover:scale-105"
                        >
                            XML
                        </button>
                    </div>
                    <button
                        onClick={closeModal}
                        className="mt-4 text-red-500 hover:underline transition-all hover:scale-105"
                    >
                        Отмена
                    </button>
                </div>
            </div>
        )
    );

    const Messages = () => (
        <div>
            {successMessage && (
                <div className="fixed top-0 left-0 right-0 bottom-0 bg-black/50 z-50 flex justify-center items-start backdrop-blur motion-preset-fade">
                    <div className="bg-green-600 text-white px-4 py-2 mt-16 rounded-3xl motion-preset-bounce flex items-center space-x-3 min-w-[200px]">
                        <span className="text-base select-none">{successMessage}</span>
                    </div>
                </div>
            )}
            {errorMessage && (
                <div className={`fixed top-0 left-0 right-0 bottom-0 bg-black/50 z-50 flex justify-center items-start backdrop-blur ${isCloseButtonVisible ? '' : 'motion-preset-fade-sm'}`}>
                    <div className={`bg-red-600 text-white px-4 py-2 mt-16 rounded-3xl flex items-center space-x-3 min-w-[200px] ${isCloseButtonVisible ? '' : 'motion-preset-bounce'}`}>
                        <span className="text-base select-none">{errorMessage}</span>
                        <button
                            onClick={closeError}
                            className={`text-white select-none text-lg font-bold ${isCloseButtonVisible ? 'motion-preset-fade' : 'invisible'}`}
                        >
                            <XCircleIcon className="w-5 h-5" />
                        </button>
                    </div>
                </div>
            )}
        </div>
    );


    return {
        handleFileUpload,
        handleSave,
        errorMessage,
        successMessage,
        isCloseButtonVisible,
        showError,
        closeError,
        showSuccess,
        openModal,
        closeModal,
        Modal,
        Messages,
    };
};

export default useFileHandler;