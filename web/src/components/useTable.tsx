import { useState, useCallback } from 'react';

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-expect-error
const useTable = (initialPoints) => {
    const [points, setPoints] = useState(initialPoints);

    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    const handlePointChange = useCallback((index, axis, value) => {
        const newValue = value === '' ? '' : parseFloat(value);
        const updatedPoints = [...points];
        updatedPoints[index] = {
            ...updatedPoints[index],
            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
            [axis]: isNaN(newValue) ? '' : newValue,
        };

        if (index === points.length - 1 && (updatedPoints[index].x !== '' || updatedPoints[index].y !== '')) {
            updatedPoints.push({ x: '', y: '' });
        }

        const filteredPoints = updatedPoints.filter(
            (point, i) => i === updatedPoints.length - 1 || point.x !== '' || point.y !== ''
        );

        setPoints(filteredPoints);
    }, [points]);

    const sortColumnX = useCallback(() => {
        const sortedPoints = [...points].sort((a, b) => {
            if (a.x === '' || isNaN(a.x)) return 1;
            if (b.x === '' || isNaN(b.x)) return -1;
            return a.x - b.x;
        });
        setPoints(sortedPoints);
    }, [points]);

    const removeDuplicates = useCallback(() => {
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-expect-error
        const uniquePoints = points.reduce((res, point) => {
            if (point.x === '') {
                res.push(point);
            } else {
                // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                // @ts-expect-error
                const isDuplicate = res.some(p => p.x === point.x);
                if (!isDuplicate) {
                    res.push(point);
                }
            }
            return res;
        }, [] as Array<{ x: number | ''; y: number | '' }>);

        setPoints(uniquePoints);
    }, [points]);

    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    const isSorted = useCallback((arr) => {
        for (let i = 1; i < arr.length; i++) {
            if (arr[i - 1] !== '' && arr[i] !== '' && parseFloat(arr[i - 1]) > parseFloat(arr[i])) {
                return false;
            }
        }
        return true;
    }, []);

    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    const hasDuplicates = useCallback((arr) => {
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-expect-error
        const nonEmptyValues = arr.filter(value => value !== '');
        const uniqueValues = new Set(nonEmptyValues);
        return uniqueValues.size !== nonEmptyValues.length;
    }, []);

    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    const xValues = points.map(point => point.x);
    const showSortButton = !isSorted(xValues);
    const showRemoveDuplicatesButton = isSorted(xValues) && hasDuplicates(xValues);

    const renderTable = () => (
        <div className="mt-2 flex-grow overflow-hidden relative" style={{ maxHeight: '450px' }}>
            <div
                className="overflow-y-auto custom-scrollbar backdrop-blur-sm bg-black/30 rounded-3xl shadow-lg"
                style={{
                    maxHeight: points.length > 10 ? '400px' : 'auto',
                }}
            >
                <table className="min-w-full border-collapse border-gray-700 border-2 rounded-3xl">
                    <thead className="sticky top-0 bg-gray-800/80 backdrop-blur-sm">
                    <tr>
                        <th className="border border-gray-700 bg-gray-800/80 text-gray-300 px-4 py-2 relative w-8 pt-3 pb-3">#</th>
                        <th className="border border-gray-700 bg-gray-800/80 text-gray-300 px-4 py-2 relative">
                            X
                            {showSortButton && (
                                <button
                                    className="ml-2 px-2 py-1 bg-teal-600 text-white rounded transition-all hover:bg-teal-400 hover:scale-105"
                                    onClick={sortColumnX}
                                >
                                    Сортировать
                                </button>
                            )}
                            {showRemoveDuplicatesButton && (
                                <button
                                    className="ml-2 px-2 py-1 bg-red-600 text-white rounded transition-all hover:bg-red-400 hover:scale-105"
                                    onClick={removeDuplicates}
                                >
                                    Удалить дубликаты
                                </button>
                            )}
                        </th>
                        <th className="border border-gray-700 bg-gray-800/80 text-gray-300 px-4 py-2">Y</th>
                    </tr>
                    </thead>
                    <tbody>

                    {// eslint-disable-next-line @typescript-eslint/ban-ts-comment
                        // @ts-expect-error
                        points.map((point, index) => (
                        <tr
                            key={index}
                            className={`transition-all duration-300 ${
                                index % 2 === 0 ? 'bg-gray-800/50' : 'bg-gray-700/50'
                            } hover:bg-teal-600/50`}
                        >
                            <td className="border border-gray-700 text-gray-300 px-4 py-2 text-center">
                                {index === points.length - 1 ? '#' : index + 1}
                            </td>
                            <td className="border border-gray-700 text-gray-300 px-4 py-2">
                                <input
                                    type="number"
                                    step="any"
                                    value={point.x}
                                    onChange={(e) => handlePointChange(index, 'x', e.target.value)}
                                    onWheel={(e) => e.currentTarget.blur()}
                                    className="w-full bg-gray-800 text-gray-300 rounded px-2 py-1 focus:outline-none focus:ring-2 focus:ring-teal-600 focus:ring-offset-2 focus:ring-offset-gray-800 transition-all"
                                    style={{ appearance: 'textfield' }}
                                />
                            </td>
                            <td className="border border-gray-700 text-gray-300 px-4 py-2">
                                <input
                                    type="number"
                                    step="any"
                                    value={point.y}
                                    onChange={(e) => handlePointChange(index, 'y', e.target.value)}
                                    onWheel={(e) => e.currentTarget.blur()}
                                    className="w-full bg-gray-800 text-gray-300 rounded px-2 py-1 focus:outline-none focus:ring-2 focus:ring-teal-600 focus:ring-offset-2 focus:ring-offset-gray-800 transition-all"
                                    style={{ appearance: 'textfield' }}
                                />
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );

    return {
        points,
        setPoints,
        renderTable,
    };
};

export default useTable;