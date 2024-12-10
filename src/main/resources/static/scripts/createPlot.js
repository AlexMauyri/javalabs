function applyValue() {
    const xValue = document.getElementById('xValue').valueAsNumber;
    if (!isFinite(xValue)) {
        showError('Неправильное значение x');
        return;
    }
    const content = fetchDataFromTable(1);
    if (!tableIsNotEmpty(content)) {
        return;
    }
    applyRequest(xValue, content);
}

function applyRequest(xValue, content) {
    fetch(`http://localhost:8080/apply/${xValue}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(content)
    })
        .then(response => {
            if (response.ok) {
                response.json().then(
                    data => {
                        document.getElementById('yValue').value = data;
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