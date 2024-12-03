function doIntegral() {
    const threads = document.getElementById('numberOfThreads').value;
    console.log(threads);
    if (!validateInteger(threads)) return;
    if (parseFloat(threads) < 0) {
        showError('Потоков не может быть отрицательное число');
        return;
    } else if (parseFloat(threads) === 0) {
        showError('Потоков не может быть ноль');
        return;
    }
    const values = fetchDataFromTable(1);
    if (values === undefined || values === null) return;


    integrate(values, threads);
}

function integrate(values, threads) {
    fetch(`http://localhost:8080/doIntegral/${threads}`, {
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
                        document.getElementById('resultOfIntegrate').value = data;
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