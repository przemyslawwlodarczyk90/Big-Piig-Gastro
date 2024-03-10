// Nasłuchuje, czy cała zawartość strony została załadowana.
document.addEventListener('DOMContentLoaded', function () {
    updatePanels(); // Wywołuje funkcję aktualizującą panele na stronie.

    // Tablica z wiadomościami promocyjnymi sklepu "u Świni".
    var messages = [
        // Lista promocyjnych hasł reklamowych.
    ];

    var messageDiv = document.getElementById('messageDiv'); // Pobiera element DOM, w którym będą wyświetlane wiadomości.
    var index = 0; // Indeks aktualnej wiadomości do wyświetlenia.

    // Ustawia interwał, który co 5 sekund zmienia wyświetlaną wiadomość.
    setInterval(function () {
        messageDiv.innerHTML = messages[index]; // Aktualizuje treść elementu z wiadomościami.
        index = (index + 1) % messages.length; // Oblicza indeks następnej wiadomości, zapętla się po osiągnięciu końca tablicy.
    }, 5000);
});