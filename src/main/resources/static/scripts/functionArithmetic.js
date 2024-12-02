function doOperation() {
    let firstTable = fetchDataFromTable(1);
    let secondTable = fetchDataFromTable(2);
    let operation = document.getElementById('operation').value;
    let content = [firstTable, secondTable];
    console.log(content);
    console.log(JSON.stringify(content));
    doOperationRequest(content, operation)
        .then(data => {
            console.log(data);
            createTableWithContent(data, 0);
        });
}

function doOperationRequest(arrayOfTables, operation) {
    return fetch(`http://localhost:8080/doOperation/${operation}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(arrayOfTables)
    })
        .then(response => response.json())
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

    return {
        x: xValues,
        y: yValues
    };
}