function createTable() {
    const points = document.getElementById('points').value;
    const tableBody = document.getElementById('functionTable').getElementsByTagName('tbody')[0];

    // Clear existing rows
    tableBody.innerHTML = '';

    // Add new rows based on the number of points
    for (let i = 0; i < points; i++) {
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

        if (x === '' || y === '') {
            alert('Please fill all x and y values.');
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

    fetch('http://localhost:8080/createTF', {
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