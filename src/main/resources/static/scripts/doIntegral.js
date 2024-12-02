function doIntegral() {
    const table = document.getElementById('functionTable1');
    const rows = table.getElementsByTagName('tbody')[0].getElementsByTagName('tr');
    const xValues = [];
    const yValues = [];
    const threads = document.getElementById('numberOfThreads').value;

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

    integrate(values, threads).then(
        data => {
            console.log(data);
            document.getElementById('resultOfIntegrate').value = data;
        }
    );
}

function integrate(values, threads) {
    return fetch(`http://localhost:8080/doIntegral/${threads}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(values)
    })
        .then(response => response.json())
}