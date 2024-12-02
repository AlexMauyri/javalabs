function submitFunctionToDiff() {
    const table = document.getElementById('functionTable1');
    const rows = table.getElementsByTagName('tbody')[0].getElementsByTagName('tr');
    const xValues = [];
    const yValues = [];

    for (let row of rows) {
        const cells = row.getElementsByTagName('td');
        const x = cells[0].getElementsByTagName('input')[0].value;
        const y = cells[1].getElementsByTagName('input')[0].value;
        xValues.push(parseFloat(x));
        yValues.push(parseFloat(y));
    }

    const values = {
        x : xValues,
        y : yValues
    };

    differentiate(values).then(
        data => {
            createTableWithContent(data, 0);
        }
    );
}

function differentiate(values) {
    return fetch('http://localhost:8080/doDifferential', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(values)
    })
        .then(response => response.json())
}