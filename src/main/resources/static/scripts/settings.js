function saveSettings() {
    let radioButtons = document.getElementsByName("fabric");

    for (let i = 0; i < radioButtons.length; ++i) {
        if (radioButtons[i].checked) {
            let savedFactory = document.cookie.split('=')[1];

            if (savedFactory !== null && savedFactory !== undefined && savedFactory === radioButtons[i].id) {
                alert("Вы уже используете эту фабрику!");
                console.error(savedFactory);
                console.error(radioButtons[i].id);
                return;
            }

            console.log(savedFactory);
            console.log(radioButtons[i].id);

            fetch('http://localhost:8080/settings', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/text',
                },
                body: radioButtons[i].id,
            }).catch((error) => {
                    alert('Ой, что-то сломалось');
                    console.error('Error:', error);
                });

            alert('Фабрика успешно заменена!');
            return;
        }
    }
}