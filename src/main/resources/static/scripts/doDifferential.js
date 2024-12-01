function loadFunction() {
    document.getElementById("fileInput").click();
}

function showTable(input) {
    let file = input.files[0];
    let extension = file.name.split('.').reverse()[0];

    if (extension === 'json') {
        let reader = new FileReader();
        reader.readAsText(file);
        reader.onload = function () {
            createTableWithContent(JSON.parse(reader.result));
        }
    } else if (extension === 'xml') {
        let reader = new FileReader();
        reader.readAsText(file);
        reader.onload = function () {
            convertXMLToJSONFunction(file).then(data => createTableWithContent(data))
        }
    } else {
        convertBLOBToJSONFunction(file).then(data => createTableWithContent(data));
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

function createTableWithContent(content) {
    let lengthOfArray = content["xValues"].length
    createTable(1, lengthOfArray);
    const tableBody = document.getElementById('functionTable1').getElementsByTagName('tbody')[0];
    const tableRows = tableBody.getElementsByTagName('tr');
    for (let i = 0; i < lengthOfArray; ++i) {
        tableRows[i].getElementsByTagName('td')[0].getElementsByTagName('input')[0].value = content['xValues'][i];
        tableRows[i].getElementsByTagName('td')[1].getElementsByTagName('input')[0].value = content['yValues'][i];
    }
}

function createTable(tableId, points) {
    const tableBody = document.getElementById('functionTable' + tableId).getElementsByTagName('tbody')[0];

    tableBody.innerHTML = '';

    for (let i = 0; i < points; ++i) {
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