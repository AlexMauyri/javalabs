function doOperation() {
    let firstTable = fetchDataFromTable(1);

    if (!tableIsNotEmpty(firstTable)) {
        return;
    }

    let secondTable = fetchDataFromTable(2);

    if (!tableIsNotEmpty(secondTable)) {
        return;
    }

    let operation = document.getElementById('operation').value;
    let content = [firstTable, secondTable];
    console.log(content);
    console.log(JSON.stringify(content));
    doOperationRequest(content, operation);

}

function doOperationRequest(arrayOfTables, operation) {
    fetch(`http://localhost:8080/doOperation/${operation}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(arrayOfTables)
    })
        .then(response => {
            if (response.ok) {
                response.json().then(data => {
                    console.log(data);
                    createTableWithContent(data, 0);
                })
            } else {
                response.text().then(
                    error => {
                        console.log(error);
                        showError(error);
                    }
                );
            }
        });
}