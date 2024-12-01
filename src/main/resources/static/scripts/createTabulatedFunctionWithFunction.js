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
    const tabulatedFunction = document.getElementById('selectFunction').value;
    const count = document.getElementById('count').value;
    const leftBorder = document.getElementById('leftBorder').value;
    const rightBorder = document.getElementById('rightBorder').value;
    const format = document.getElementById("format").value;

    const functionData = {
        functionName : tabulatedFunction,
        from : leftBorder,
        to : rightBorder,
        amountOfPoints : count,
    }

    serializeAndDownload(functionData, format);
}

function serializeAndDownload(functionData, format) {
    if (format !== 'Byte') {
        serializeFunction(functionData, format)
            .then(response => response.text())
            .then(data => {
                console.log(data);
                let link = document.createElement('a');
                link.href = window.URL.createObjectURL(
                    new Blob([data],
                        {
                            type: 'application/plain'
                        }
                    )
                );
                link.download = 'newFunction.' + format.toLowerCase();
                link.click();
            })
            .catch((error) => {
                console.error('Error:', error);
                alert('Error creating tabulated function');
            });
    } else {
        serializeFunction(functionData, format)
            .then(response => response.blob())
            .then(data => {
                console.log(data);
                let link = document.createElement('a');
                link.href = window.URL.createObjectURL(data);
                link.download = 'newFunction.bin'; // Предлагаемое имя файла
                link.click();
            })
            .catch((error) => {
                console.error('Error:', error);
                alert('Error creating tabulated function');
            });
    }
}

function serializeFunction(functionData, format) {
    console.log(JSON.stringify(functionData));
    return fetch('http://localhost:8080/createTabulatedFunctionWithFunction' + format, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(functionData),
    });
}

