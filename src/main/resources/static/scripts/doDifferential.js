function submitFunctionToDiff() {
    const values = fetchDataFromTable(1);
    if (values === undefined || values === null) return;
    differentiate(values);
}

function differentiate(values) {
    fetch('http://localhost:8080/doDifferential', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(values)
    })
        .then(response => {
            if (response.ok) {
                response.json().then(
                    data => {
                        createTableWithContent(data, 0);
                    }
                );
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