function loadFunction() {
    document.getElementById("fileInput").click();
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

function createTableWithContent(content, id) {
    let lengthOfArray = content["xValues"].length
    createTableForTableId(id, lengthOfArray);
    const tableBody = document.getElementById('functionTable1').getElementsByTagName('tbody')[0];
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
    const points = parseFloat(document.getElementById('points').value); //Запятая, точка, е дают пустую строку

    const tableBody = document.getElementById('functionTable').getElementsByTagName('tbody')[0];

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

function submitFunctionOnTable() {
    const table = document.getElementById('functionTable');
    const rows = table.getElementsByTagName('tbody')[0].getElementsByTagName('tr');
    const xValues = [];
    const yValues = [];

    for (let row of rows) {
        const cells = row.getElementsByTagName('td');
        const x = cells[0].getElementsByTagName('input')[0].value;
        const y = cells[1].getElementsByTagName('input')[0].value;
        if (x === '') {
            alert(`Не заполнено значение x в ${row.rowIndex} строке`);
            return;
        } else if (y === '') {
            alert(`Не заполнено значение y в ${row.rowIndex} строке`);
            return;
        }

        xValues.push(parseFloat(x));
        yValues.push(parseFloat(y));
    }

    let content = {
        xValues : xValues,
        yValues : yValues
    }
    createTableWithContent(content, 1);
    document.getElementById('my-dialog').close();
}

function submitFunctionOnFunction() {
    const tabulatedFunction = document.getElementById('selectFunction').value;
    const count = document.getElementById('count').value;
    const leftBorder = document.getElementById('leftBorder').value;
    const rightBorder = document.getElementById('rightBorder').value;

    const functionData = {
        functionName: tabulatedFunction,
        from: leftBorder,
        to: rightBorder,
        amountOfPoints: count,
    }

    serializeFunction(functionData, 'JSON')
        .then(data => {
            createTableWithContent(data, 1);
            document.getElementById('my-dialog').close();
        });
}

function serializeFunction(functionData, format) {
    console.log(JSON.stringify(functionData));
    return fetch('http://localhost:8080/createTabulatedFunctionWithFunction' + format, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(functionData),
    }).then(response => response.json());
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

async function getData(url) {
    const response = await fetch(url);
    return await response.text();
}

document.getElementById('createTable').addEventListener('click', () => {
    fetch('popup/createTabulatedFunction')
        .then(response => response.text())
        .then(html => {
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
