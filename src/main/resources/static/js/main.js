document.addEventListener('DOMContentLoaded', function () {
    updatePanels();

    var messages = [
        // ... Twoje wiadomości ...
    ];
    var messageDiv = document.getElementById('messageDiv');
    var index = 0;
    setInterval(function () {
        messageDiv.innerHTML = messages[index];
        index = (index + 1) % messages.length;
    }, 5000);
});

function updatePanels() {
    fetch('/api/user/status')
        .then(response => response.json())
        .then(data => {
            console.log(data); // Wyświetla otrzymane dane w konsoli

            var userLoggedIn = data.loggedIn;
            var userRoles = data.roles;

            var adminPanel = document.getElementById('adminPanel');
            var userPanel = document.getElementById('userPanel');

            if (userLoggedIn) {
                adminPanel.style.display = userRoles.includes('ROLE_ADMIN') ? 'block' : 'none';
                userPanel.style.display = userRoles.includes('ROLE_USER') ? 'block' : 'none';
            } else {
                adminPanel.style.display = 'none';
                userPanel.style.display = 'none';
            }
        })
        .catch(error => console.error('Error:', error));
}