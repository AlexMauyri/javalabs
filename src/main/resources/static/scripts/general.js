let currentButtonId;

function loadFunction(id) {
    document.getElementById(id).click();
}

function showTable(input, id) {
    let file = input.files[0];
    let extension = file.name.split('.').reverse()[0];
    if (extension === 'json') {
        let reader = new FileReader();
        reader.readAsText(file);
        reader.onload = function () {
            createTableWithContent(JSON.parse(reader.result), id);
        }
    } else if (extension === 'xml') {
        let reader = new FileReader();
        reader.readAsText(file);
        reader.onload = function () {
            convertXMLToJSONFunction(file).then(data => createTableWithContent(data, id));
        }
    } else {
        convertBLOBToJSONFunction(file).then(data => createTableWithContent(data, id));
    }
}

function createTableWithContent(content, id) {
    console.log(content);
    console.log("Table id is " + id);
    let lengthOfArray = content["xValues"].length;
    createTableForTableId(id, lengthOfArray);
    const tableBody = document.getElementById('functionTable' + id).getElementsByTagName('tbody')[0];
    const tableRows = tableBody.getElementsByTagName('tr');
    for (let i = 0; i < lengthOfArray; ++i) {
        tableRows[i].getElementsByTagName('td')[0].getElementsByTagName('input')[0].value = content['xValues'][i];
        tableRows[i].getElementsByTagName('td')[1].getElementsByTagName('input')[0].value = content['yValues'][i];
    }
}

function createTableForTableId(tableId, points) {
    const tableBody = document.getElementById('functionTable' + tableId).getElementsByTagName('tbody')[0];

    tableBody.innerHTML = '';

    for (let i = 0; i < points; ++i) {
        const row = document.createElement('tr');

        const cellX = document.createElement('td');
        const inputX = document.createElement('input');
        inputX.type = 'number';
        inputX.required = true;
        inputX.readOnly = true;
        cellX.appendChild(inputX);

        const cellY = document.createElement('td');
        const inputY = document.createElement('input');
        inputY.type = 'number';
        inputY.required = true;
        inputY.readOnly = (tableId === 0);
        cellY.appendChild(inputY);

        row.appendChild(cellX);
        row.appendChild(cellY);
        tableBody.appendChild(row);
    }
}

function createTable() {
    let points = document.getElementById('points').value; //Запятая, точка, е дают пустую строку

    if (!validateInteger(points)) return;

    points = parseFloat(points);

    if (points < 2) {
        showError('Число точек должно быть больше 2');
        return;
    }

    const tableBody = document.getElementById('functionTableModal').getElementsByTagName('tbody')[0];

    const existingRows = tableBody.getElementsByTagName('tr');
    let i = 0;
    for (; i < existingRows.length && i < points; ++i) {
        let values = existingRows[i].getElementsByTagName('td');
        let x = values[0].getElementsByTagName('input')[0].value;
        let y = values[1].getElementsByTagName('input')[0].value;
        if (x === undefined && y === undefined) {
            break;
        }
    }

    for (let j = existingRows.length - 1; j >= i; --j) {
        tableBody.removeChild(existingRows[j]);
    }

    for (; i < points; ++i) {
        const row = document.createElement('tr');

        const cellX = document.createElement('td');
        const inputX = document.createElement('input');
        inputX.type = 'number';
        inputX.required = true;
        cellX.appendChild(inputX);

        const cellY = document.createElement('td');
        const inputY = document.createElement('input');
        inputY.type = 'number';
        inputY.required = true;
        cellY.appendChild(inputY);

        row.appendChild(cellX);
        row.appendChild(cellY);
        tableBody.appendChild(row);
    }
}

function createDropdownList() {
    let selector = document.getElementById("selectFunction");

    fetch('http://localhost:8080/getFunctions')
        .then(response => response.json())
        .then(data => {
            data.sort();
            for (let index = 0; index < data.length; ++index) {
                let option = document.createElement("option");
                option.text = data[index];
                option.value = data[index];
                selector.appendChild(option);
            }
        });
}

function submitFunctionOnTable() {
    let content = fetchDataFromTable('Modal');
    if (content === undefined || content === null) return;
    createTableWithContent(content, currentButtonId[currentButtonId.length - 1]);
    document.getElementById('my-dialog').close();
}

function submitFunctionOnFunction() {
    const tabulatedFunction = document.getElementById('selectFunction').value;
    const count = document.getElementById('count').value;
    const leftBorder = document.getElementById('leftBorder').value;
    const rightBorder = document.getElementById('rightBorder').value;

    if (!validateInteger(count)) return;

    if (parseFloat(count) < 2) {
        showError('Число точек должно быть больше 2');
        return;
    }

    if (!validateDouble(leftBorder)) return;
    if (!validateDouble(rightBorder)) return;

    const functionData = {
        functionName: tabulatedFunction,
        from: leftBorder,
        to: rightBorder,
        amountOfPoints: count,
    }

    serializeFunctionAndReturn(functionData, 'JSON')
        .then(data => {
            createTableWithContent(data, currentButtonId[currentButtonId.length - 1]);
            document.getElementById('my-dialog').close();
        });
}

function submitFunction() {
    const content = fetchDataFromTable(currentButtonId[currentButtonId.length - 1]);
    const format = document.getElementById("dialog-format").value;

    serializeAndDownload(content, format);
}

function serializeFunctionAndReturn(functionData, format) {
    console.log(JSON.stringify(functionData));
    return fetch('http://localhost:8080/createTabulatedFunctionWithFunction' + format, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(functionData),
    }).then(response => response.json());
}

function serializeAndDownload(content, format) {
    if (format !== 'Byte') {
        serializeFunction(content, format)
            .then(response => response.text())
            .then(data => {
                console.log(data);
                let link = document.createElement('a');
                link.href = window.URL.createObjectURL(
                    new Blob([data],
                        {
                            type: 'application/plain'
                        }
                    )
                );
                link.download = 'newFunction.' + format.toLowerCase();
                link.click();
            })
            .catch((error) => {
                console.error('Error:', error);
                alert('Error creating tabulated function');
            });
    } else {
        serializeFunction(content, format)
            .then(response => response.blob())
            .then(data => {
                console.log(data);
                let link = document.createElement('a');
                link.href = window.URL.createObjectURL(data);
                link.download = 'newFunction.bin'; // Предлагаемое имя файла
                link.click();
            })
            .catch((error) => {
                console.error('Error:', error);
                alert('Error creating tabulated function');
            });
    }
}

function serializeFunction(content, format) {
    console.log(JSON.stringify(content));
    console.log(content);
    return fetch('http://localhost:8080/createTabulatedFunctionWithTable' + format, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(content),
    });
}

function fetchDataFromTable(id) {
    const table = document.getElementById('functionTable' + id);
    const rows = table.getElementsByTagName('tbody')[0].getElementsByTagName('tr');
    const xValues = [];
    const yValues = [];

    for (let row of rows) {
        const cells = row.getElementsByTagName('td');
        const x = cells[0].getElementsByTagName('input')[0].value;
        const y = cells[1].getElementsByTagName('input')[0].value;
        if (x === '' || !validateDouble(x)) {
            showError(`Неправильное значение x в ${row.rowIndex} строке`);
            return;
        } else if (y === '' || !validateDouble(y)) {
            showError(`Неправильное значение y в ${row.rowIndex} строке`);
            return;
        }

        xValues.push(parseFloat(x));
        yValues.push(parseFloat(y));
    }

    return {
        xValues: xValues,
        yValues: yValues
    };
}

function setCurrentIdButton(id) {
    currentButtonId = id;
}

function convertXMLToJSONFunction(content) {
    return fetch('http://localhost:8080/convertFromXML', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/plain',
        },
        body: content})
        .then(response => response.json())
}

function convertBLOBToJSONFunction(content) {
    return fetch('http://localhost:8080/convertFromBLOB', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/octet-stream',
        },
        body: content
    })
        .then(response => response.json())
}

function showError(error) {
    fetch('popup/error')
        .then(response => response.text())
        .then(html => {
            document.getElementById('error-modal-container').innerHTML = html;
            document.getElementById('textError').innerHTML = error;
            document.getElementById('error-modal').showModal();
            document.getElementById('closeError').addEventListener('click', () => {
                document.getElementById('error-modal').close();
                document.getElementById('error-modal-container').innerHTML = '';
            });
        })
}

function validateInteger(value) {
    if (value === null || value === undefined || value === '') {
        showError('Неправильная запись числа!');
        return false;
    }

    if (typeof value === 'string') {
        // Удаляем пробелы
        value = value.trim();

        if (value.includes('.') || value.includes(',')) {
            showError('Число должно быть целым');
            return false;
        }

        if (value.includes('e')) {
            const parts = value.split('e');
            if (parts.length !== 2 || isNaN(parts[0]) || isNaN(parts[1])) {
                showError('Неправильная запись экспоненциальной формы числа');
                return false;
            }
        }

        value = Number(value);
    }

    if (typeof value !== 'number') {
        showError('Вы ввели не число!');
        return false;
    } else if (!isFinite(value)) {
        showError('Число слишком большое!')
        return false;
    }

    return true;
}

function validateDouble(value) {
    if (value === null || value === undefined || value === '') {
        showError('Неправильная запись числа!');
        return false;
    }

    if (typeof value === 'string') {
        // Удаляем пробелы
        value = value.trim();

        if (value.includes('e')) {
            const parts = value.split('e');
            if (parts.length !== 2 || isNaN(parts[0]) || isNaN(parts[1])) {
                showError('Неправильная запись экспоненциальной формы числа');
                return false;
            }
        }

        value = Number(value);
    }

    if (typeof value !== 'number') {
        showError('Вы ввели не число!');
        return false;
    } else if (!isFinite(value)) {
        showError('Число слишком большое!')
        return false;
    }

    return true;
}

async function getData(url) {
    const response = await fetch(url);
    return await response.text();
}

let createButtons = document.getElementsByClassName("createTable");

for (let i = 0; i < createButtons.length; ++i) {
    let id = createButtons[i].getAttribute('id');
    console.log(id);
    document.getElementById(id).addEventListener('click', () => {
        fetch('popup/createTabulatedFunction')
            .then(response => response.text())
            .then(html => {
                console.log(currentButtonId);
                document.getElementById('modal-container').innerHTML = html;
                document.getElementById('my-dialog').showModal();
                document.getElementById('table').addEventListener('click', () => {
                    getData('popup/tableCreation').then(result => {
                        document.getElementById('my-dialog').innerHTML = result;
                        document.getElementById('close').addEventListener('click', () => {
                            document.getElementById('my-dialog').close();
                        });
                    });
                });

                document.getElementById('function').addEventListener('click', () => {
                    getData('popup/functionCreation').then(result => {
                        document.getElementById('my-dialog').innerHTML = result;
                        createDropdownList();
                        document.getElementById('close').addEventListener('click', () => {
                            document.getElementById('my-dialog').close();
                        });
                    });
                });

                document.getElementById('close').addEventListener('click', () => {
                    document.getElementById('my-dialog').close();
                });
            });
    });
}

let saveButtons = document.getElementsByClassName("saveTable");

for (let i = 0; i < saveButtons.length; ++i) {
    let id = saveButtons[i].getAttribute('id');
    console.log(id);
    document.getElementById(id).addEventListener('click', () => {
        fetch('popup/saveFile')
            .then(response => response.text())
            .then(html => {
                console.log(currentButtonId);
                document.getElementById('modal-container').innerHTML = html;
                document.getElementById('save-modal').showModal();
                document.getElementById('close').addEventListener('click', () => {
                    document.getElementById('save-modal').close();
                });
            });
    })
}