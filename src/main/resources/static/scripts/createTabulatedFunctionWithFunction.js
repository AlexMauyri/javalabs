function createDropdownList() {
    let selector = document.getElementById("selectFunction");

    fetch('http://localhost:8080/getFunctions')
        .then(response => response.json())
        .then(data => {
            data.sort();
            for (let index = 0; index < data.length; ++index) {
                let option = document.createElement("option");
                option.text = data[index];
                option.value = data[index];
                selector.appendChild(option);
            }
        });
}

function submitFunction() {
    let tabulatedFunction = document.getElementById('selectFunction');
    let count = document.getElementById('count');
    let leftBorder = document.getElementById('leftBorder');
    let rightBorder = document.getElementById('rightBorder');

    sendDataToBackend(tabulatedFunction.value, count.value, leftBorder.value, rightBorder.value);
}

function sendDataToBackend(tabulatedFunction, count, leftBorder, rightBorder) {
    const data = {
        functionName : tabulatedFunction,
        from : leftBorder,
        to : rightBorder,
        amountOfPoints : count,
    }
    console.log(JSON.stringify(data));
    fetch('http://localhost:8080/createTabulatedFunctionWithFunction', {
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