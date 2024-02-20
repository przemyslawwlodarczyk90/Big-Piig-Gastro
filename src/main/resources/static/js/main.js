document.addEventListener('DOMContentLoaded', function () {
    updatePanels();

    var messages = [
        "Zakupy u Świni, smak i styl gwarantowany!",
        "Gdzie Świnia tam cena fina, wybór pierwsza klasa!",
        "Świnia wie, co dobre – tutaj zakupy są na bogato!",
        "Nie przepłacaj, wybieraj jak Świnia – mądrze i z gustem!",
        "Kto ma nosa, ten u Świni robi zakupy doskonałe!",
        "Świniowe promocje – skorzystaj, zanim znikną!",
        "Tylko u Świni, produkty, które zmienią Twoje życie!",
        "Świnia to więcej niż sklep, to gwarancja jakości!",
        "Zrób sobie dobrze, wybieraj zakupy u Świni!",
        "Świnia zna się na rzeczy, przekonaj się sam!",
        "Zakupy bez nudy, tylko u Świni – przyjdź i zobacz!",
        "Dobre bo u Świni, kupuj i poczuj różnicę!",
        "U Świni zawsze trafisz w dziesiątkę – zakupy bez ryzyka!",
        "Zakupy u Świni – tam, gdzie ceny i jakość idą w parze!",
        "Świnia radzi, jak wybrać najlepsze – zaufaj ekspertowi!",
        "Nie daj się zwieść, wybierz zakupy prosto od Świni!",
        "Świnia to skarbnica dobrych ofert – nie przegap!",
        "Każda wizyta u Świni to festiwal dobrych cen!",
        "Świnia to synonim udanych zakupów – przekonaj się!",
        "Gdzie Świnia tam dom pełen smaków i stylu!",
        "Kupuj jak profesjonalista – Świnia Ci w tym pomoże!",
        "Świniowe okazje – tak tanio tylko u nas!",
        "Przygoda z zakupami zaczyna się u Świni – dołącz do nas!",
        "Świnia zaprasza – tutaj każdy znajdzie coś dla siebie!",
        "Zakupy z klasą? Tylko u Świni – przekonaj się!",
        "Wybierz Świnię – i ciesz się zakupami bez limitu!",
        "Kupuj u Świni – i spraw, by każdy dzień był lepszy!",
        "Świnia to gwarancja satysfakcji – zakupy, które pokochasz!",
        "Nie tylko łep na karku – każdy mądry wybierze Świnię!",
        "Świnia – tam, gdzie zakupy to czysta przyjemność!"
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