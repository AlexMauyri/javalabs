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

    // Send the data to the Java backend
    sendDataToBackend(xValues, yValues);
}

function sendDataToBackend(xValues, yValues) {
    const data = {
        x: xValues,
        y: yValues
    };

    fetch('http://localhost:8080/createTabulatedFunctionWithTable', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })
        .then(response => response.json())
        .then(data => {
            alert('Tabulated function created successfully!');
            console.log('Response from backend:', data);
        })
        .catch((error) => {
            console.error('Error:', error);
            alert('Error creating tabulated function');
        });
}