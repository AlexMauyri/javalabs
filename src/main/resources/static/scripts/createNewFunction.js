function addFunction() {
    let select = document.createElement("select");
    select.innerHTML = document.getElementById('selectFunction').innerHTML;
    let label = document.createElement('label');
    label.innerHTML = ' <= ';
    label.appendChild(select);
    let chain = document.getElementById('chainOfFunctions');
    chain.appendChild(label);
}

function deleteFunction() {
    let chain = document.getElementById('chainOfFunctions');
    if (chain.getElementsByTagName('select').length === 2) {
        showError("Для создания сложной функции должно быть минимум две функции в цепочке");
        return;
    }
    chain.removeChild(chain.children[chain.children.length - 1]);
}

function createFunction() {
    let functionName = document.getElementById('functionName').value;

    if (!validateNameOfFunction(functionName)) {
        showError("Имя функции может содержать только русские буквы, цифры и пробелы");
        return;
    }

    let selects = document.getElementById('chainOfFunctions').getElementsByTagName('select');

    let functions = [];

    for (let i = 0; i < selects.length; ++i) {
        functions.push(selects[i].value);
    }

    createFunctionRequest(functions, functionName);
}

function createFunctionRequest(functions, functionName) {
    fetch(`/create/${functionName}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(functions)
    }).then(response => {
        if (response.ok) {
            console.log("Everything is fine!!!");
            updateSelects();
        } else {
            response.text().then(error => {
                showError(error);
            });
        }
    });
}

function updateSelectsRequest() {
    createDropdownList().then(() => {
        updateSelects();
    });
}

function updateSelects() {
    let selects = document.getElementById('chainOfFunctions').getElementsByTagName('select');
    console.log(selects);
    for (let i = 0; i < selects.length; ++i) {
        console.log(document.getElementById('selectFunction').innerHTML);
        selects[i].innerHTML = document.getElementById('selectFunction').innerHTML;
    }
}

function clearAll() {
    document.getElementById('chainOfFunctions').innerHTML = "<label><select></select></label> <= <label><select></select></label>";
    updateSelects();
}

function validateNameOfFunction(data) {
    const regex = /^[а-яА-ЯёЁ0-9\s]+$/;
    return regex.test(data);
}