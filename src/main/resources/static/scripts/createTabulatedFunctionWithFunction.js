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