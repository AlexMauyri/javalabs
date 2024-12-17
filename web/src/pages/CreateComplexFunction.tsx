import React, {useEffect, useState} from 'react';
import axios from 'axios';
import useFileHandler from '../components/useFileHandler';

const CreateComplexFunction: React.FC = () => {
    const [functions, setFunctions] = useState<string[]>(['Тождественная функция', 'Тождественная функция']);
    const [availableFunctions, setAvailableFunctions] = useState<string[]>([]);
    const [selectedFunction, setSelectedFunction] = useState<string>('');
    const [functionName, setFunctionName] = useState<string>('');
    const [deleteFunctionName, setDeleteFunctionName] = useState<string>('');
    const [isAddModalOpen, setIsAddModalOpen] = useState(false);
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
    const server = 'http://localhost:8080';

    const {showError, showSuccess, Messages} = useFileHandler(server);

    useEffect(() => {
        fetchFunctions();
    }, []);

    const fetchFunctions = async () => {
        try {
            const response = await axios.get(`${server}/getFunctions`, {
                withCredentials: true,
            });
            setAvailableFunctions(response.data);
        } catch (error) {
            showError('Ошибка при загрузке функций.');
        }
    };

    const addFunction = () => {
        if (functions.length >= 20) {
            showError('Вы не можете добавить больше 20 функций.');
            return;
        }
        if (!selectedFunction) {
            showError('Выберите функцию для добавления.');
            return;
        }
        setFunctions([...functions, selectedFunction]);
        setSelectedFunction('');
        setIsAddModalOpen(false);
    };

    const removeLastFunction = () => {
        if (functions.length > 2) {
            setFunctions(functions.slice(0, -1));
        }
    };

    const resetFunctions = () => {
        setFunctions(['Тождественная функция', 'Тождественная функция']);
    };

    const changeFunction = (index: number, newFunction: string) => {
        const updatedFunctions = [...functions];
        updatedFunctions[index] = newFunction;
        setFunctions(updatedFunctions);
    };

    const createFunction = async () => {
        if (!functionName.match(/^[а-яА-Я0-9\s]+$/)) {
            showError('Название функции должно содержать только кириллицу, цифры и пробелы.');
            return;
        }
        try {
            await axios.post(
                `${server}/create/${functionName}`,
                functions,
                {
                    withCredentials: true,
                }
            );
            resetFunctions();
            showSuccess('Функция успешно создана.');
            setFunctionName('');
            setIsCreateModalOpen(false);
            fetchFunctions();
        } catch (error) {
            showError('Ошибка при создании функции.');
        }
    };

    const deleteFunction = async () => {
        if (!deleteFunctionName) {
            showError('Выберите функцию для удаления.');
            return;
        }
        try {
            await axios.delete(`${server}/delete/${deleteFunctionName}`, {
                withCredentials: true,
            });
            showSuccess('Функция успешно удалена.');
            setDeleteFunctionName('');
            setIsDeleteModalOpen(false);
            fetchFunctions();
        } catch (error) {
            showError('Ошибка при удалении функции.');
        }
    };

    const handleDragEnd = (result: any) => {
        if (!result.destination) return;
        const items = Array.from(functions);
        const [reorderedItem] = items.splice(result.source.index, 1);
        items.splice(result.destination.index, 0, reorderedItem);
        setFunctions(items);
    };

    return (
        <div className="p-8 h-full flex flex-col backdrop-blur-sm bg-black/30 rounded-3xl shadow-lg">
            <Messages/>
            <h1 className="text-2xl font-bold text-gray-300 mb-4">Создание сложной функции</h1>
            <div className="flex justify-between space-x-2 mb-2">
                <button
                    onClick={() => setIsAddModalOpen(true)}
                    className="flex-1 bg-teal-600 text-white rounded px-4 py-2 transition-all hover:bg-teal-400 hover:scale-105"
                >
                    Добавить функцию
                </button>
                <button
                    onClick={removeLastFunction}
                    className="flex-1 bg-teal-600 text-white rounded px-4 py-2 transition-all hover:bg-teal-400 hover:scale-105"
                >
                    Убрать функцию
                </button>
                <button
                    onClick={resetFunctions}
                    className="flex-1 bg-red-600 text-white rounded px-4 py-2 transition-all hover:bg-red-400 hover:scale-105"
                >
                    Сброс
                </button>
            </div>
            <div className="flex justify-center mb-4">
                <button
                    onClick={() => setIsCreateModalOpen(true)}
                    className="bg-green-600 text-white rounded px-4 py-2 w-full transition-all hover:bg-green-400 hover:scale-105"
                >
                    Создать сложную функцию
                </button>
                <button
                    onClick={() => setIsDeleteModalOpen(true)}
                    className="ml-2 bg-red-600 text-white rounded px-4 py-2 w-full transition-all hover:bg-red-400 hover:scale-105"
                >
                    Удалить сложную функцию из базы данных
                </button>
            </div>
            <div className="mb-8 mt-4 ml-8 font-bold text-lg text-gray-300">
                {functions
                    .map((_func, index) => `f${functions.length - (functions.length - index - 1)}( `)
                    .reverse()
                    .join('') + 'x' + functions.map(() => ' )').join('')}
            </div>

            <div className="flex gap-4 flex-wrap pb-11">
                {functions.map((func, index) => (
                    <div
                        key={index}
                        draggable
                        onDragStart={(e) => {
                            e.dataTransfer.setData('text/plain', index.toString());
                            e.currentTarget.classList.add('dragging');
                        }}
                        onDragEnd={(e) => {
                            e.currentTarget.classList.remove('dragging');
                        }}
                        onDragOver={(e) => e.preventDefault()}
                        onDrop={(e) => {
                            const draggedIndex = parseInt(e.dataTransfer.getData('text/plain'));
                            handleDragEnd({source: {index: draggedIndex}, destination: {index}});
                        }}
                        className="border border-gray-700 rounded-md p-4 min-w-[200px] bg-gray-800 text-gray-300 cursor-move transition-transform duration-300 ease-in-out hover:shadow-lg hover:scale-105"
                    >
                        <select
                            value={func}
                            onChange={(e) => changeFunction(index, e.target.value)}
                            className="min-w-full bg-gray-800 text-gray-300 border border-gray-700 rounded px-2 py-1 mt-2 font-bold text-center focus:outline-none focus:ring-2 focus:ring-teal-600 focus:ring-offset-2 focus:ring-offset-gray-800 transition-all"
                        >
                            <option value="" disabled className="bg-gray-800 text-gray-300">
                                Выберите функцию
                            </option>
                            {availableFunctions.map((optionFunc) => (
                                <option key={optionFunc} value={optionFunc} className="bg-gray-800 text-gray-300">
                                    {optionFunc}
                                </option>
                            ))}
                        </select>
                        <h3 className="text-center text-gray-300">Порядок применения - {index + 1}</h3>
                    </div>
                ))}
            </div>

            {isAddModalOpen && (
                <div
                    className="fixed top-0 left-0 right-0 bottom-0 bg-black/50 flex justify-center items-center motion-preset-fade backdrop-blur">
                    <div className="bg-gray-800 p-8 rounded-3xl shadow-lg w-72 motion-preset-expand">
                        <h2 className="text-lg font-bold text-gray-300 mb-4">Добавить функцию</h2>
                        <select
                            value={selectedFunction}
                            onChange={(e) => setSelectedFunction(e.target.value)}
                            className="w-full mb-4 bg-gray-800 text-gray-300 border border-gray-700 rounded px-2 py-1 focus:outline-none focus:ring-2 focus:ring-teal-600 focus:ring-offset-2 focus:ring-offset-gray-800 transition-all"
                        >
                            <option value="" disabled className="bg-gray-800 text-gray-300">
                                Выберите функцию
                            </option>
                            {availableFunctions
                                .sort((a, b) => a.localeCompare(b))
                                .map((func) => (
                                    <option key={func} value={func} className="bg-gray-800 text-gray-300">
                                        {func}
                                    </option>
                                ))}
                        </select>
                        <div className="flex justify-center">
                            <button
                                onClick={addFunction}
                                className="bg-teal-600 text-white rounded px-4 py-2 transition-all hover:bg-teal-400 hover:scale-105"
                            >
                                Добавить
                            </button>
                            <button
                                onClick={() => setIsAddModalOpen(false)}
                                className="ml-2 bg-gray-500 text-white rounded px-4 py-2 transition-all hover:bg-gray-400 hover:scale-105"
                            >
                                Отмена
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {isDeleteModalOpen && (
                <div
                    className="fixed top-0 left-0 right-0 bottom-0 bg-black/50 flex justify-center items-center motion-preset-fade backdrop-blur">
                    <div className="bg-gray-800 p-8 rounded-3xl shadow-lg min-w-fit w-72 motion-preset-expand">
                        <h2 className="text-lg font-bold text-gray-300 mb-4">Удалить сложную функцию</h2>
                        <select
                            value={deleteFunctionName}
                            onChange={(e) => setDeleteFunctionName(e.target.value)}
                            className="min-w-60 mb-4 bg-gray-800 text-gray-300 border border-gray-700 rounded px-2 py-1 ml-4 mr-4 focus:outline-none focus:ring-2 focus:ring-teal-600 focus:ring-offset-2 focus:ring-offset-gray-800 transition-all"
                        >
                            <option value="" disabled className="bg-gray-800 text-gray-300">
                                Выберите функцию
                            </option>
                            {availableFunctions
                                .filter(func => !['Тождественная функция', 'Нулевая функция', 'Квадратичная функция', 'Единичная функция'].includes(func))
                                .sort((a, b) => a.localeCompare(b))
                                .map((func) => (
                                    <option key={func} value={func} className="bg-gray-800 text-gray-300">
                                        {func}
                                    </option>
                                ))}
                        </select>
                        <div className="flex justify-center">
                            <button
                                onClick={deleteFunction}
                                className="bg-red-600 text-white rounded px-4 py-2 transition-all hover:bg-red-400 hover:scale-105"
                            >
                                Удалить
                            </button>
                            <button
                                onClick={() => setIsDeleteModalOpen(false)}
                                className="ml-2 bg-gray-500 text-white rounded px-4 py-2 transition-all hover:bg-gray-400 hover:scale-105"
                            >
                                Отмена
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {isCreateModalOpen && (
                <div
                    className="fixed top-0 left-0 right-0 bottom-0 bg-black/50 flex justify-center items-center motion-preset-fade backdrop-blur">
                    <div className="bg-gray-800 p-8 rounded-3xl shadow-lg min-w-fit w-72 motion-preset-expand">
                        <h2 className="text-lg font-bold text-gray-300 mb-4">Создать функцию</h2>
                        <input
                            type="text"
                            value={functionName}
                            onChange={(e) => setFunctionName(e.target.value)}
                            className="min-w-60 mb-4 bg-gray-800 text-gray-300 border border-gray-700 rounded px-2 py-1 ml-4 mr-4 focus:outline-none focus:ring-2 focus:ring-teal-600 focus:ring-offset-2 focus:ring-offset-gray-800 transition-all"
                            placeholder="Кириллица, цифры, пробелы"
                        />
                        <div className="flex justify-center">
                            <button
                                onClick={createFunction}
                                className="bg-green-600 text-white rounded px-4 py-2 transition-all hover:bg-green-400 hover:scale-105"
                            >
                                Создать
                            </button>
                            <button
                                onClick={() => setIsCreateModalOpen(false)}
                                className="ml-2 bg-gray-500 text-white rounded px-4 py-2 transition-all hover:bg-gray-400 hover:scale-105"
                            >
                                Отмена
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CreateComplexFunction;