let built_in_functions=['Квадратичная функция', 'Нулевая функция', 'Тождественная функция', 'Единичная функция'];

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
    console.log(functionName);
    fetch(`/create/${functionName}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(functions)
    }).then(response => {
        if (response.ok) {
            console.log("Everything is fine!!!");
            updateSelectsRequest();
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
    let cachedSelect = document.getElementById('selectFunction');
    for (let i = 0; i < selects.length; ++i) {
        selects[i].innerHTML = document.getElementById('selectFunction').innerHTML;
    }

    let options = cachedSelect.getElementsByTagName('option');
    let deleteSelect = document.getElementById('selectForDeleting');
    deleteSelect.innerHTML = '';
    for (let option of options) {
        if (!built_in_functions.includes(option.value)) {
            deleteSelect.appendChild(option);
        }
    }
}

function deleteCustomFunction() {
    let select = document.getElementById('selectForDeleting');
    let functionToDelete = select.value;
    deleteCustomFunctionRequest(functionToDelete);
}

function deleteCustomFunctionRequest(functionToDelete) {
    fetch(`/delete/${functionToDelete}`, {
        method: 'DELETE'
    }).then(response => {
        if (response.ok) {
            console.log("Everything is fine!!!");
            updateSelectsRequest();
        } else {
            response.text().then(error => {
                showError(error);
            });
        }
    });
}

function clearAll() {
    document.getElementById('chainOfFunctions').innerHTML = "<label><select></select></label> <= <label><select></select></label>";
    updateSelects();
}

function validateNameOfFunction(data) {
    const regex = /^[а-яА-ЯёЁ0-9\s]+$/;
    return regex.test(data);
}