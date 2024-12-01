function createTable() {
    const points = parseFloat(document.getElementById('points').value); //Запятая, точка, е дают пустую строку
    if (isNaN(points) || parseInt(Number(points)) !== points) {
        alert('Число не может быть дробным!');
        return;
    } else if (points < 0) {
        alert('Число не может быть отрицательным! Требуется значение от двух и более');
        return;
    } else if (points === 0) {
        alert('А смысл? Требуется значение от двух и более');
        return;
    } else if (points === 1) {
        alert('Требуется значение от двух и более');
        return;
    }

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

function submitFunction() {
    const table = document.getElementById('functionTable');
    const rows = table.getElementsByTagName('tbody')[0].getElementsByTagName('tr');
    const xValues = [];
    const yValues = [];
    const format = document.getElementById("format").value;

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


    serializeAndDownload(xValues, yValues, format);
}

function serializeAndDownload(xValues, yValues, format) {
    if (format !== 'Byte') {
        serializeFunction(xValues, yValues, format)
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
        serializeFunction(xValues, yValues, format)
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

function serializeFunction(xValues, yValues, format) {
    const data = {
        x: xValues,
        y: yValues
    };
    return fetch('http://localhost:8080/createTabulatedFunctionWithTable' + format, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    });
}