function applyValue() {
    const xValue = document.getElementById('xValue').value;
    if (!validateDouble(xValue)) return;
    const content = fetchDataFromTable(1);
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