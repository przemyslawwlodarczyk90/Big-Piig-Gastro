

document.addEventListener("DOMContentLoaded", function() {
});

function changeOrderStatus(orderId) {
    var newStatus = document.getElementById('statusSelect' + orderId).value;
    if(newStatus) {
        fetch('/admin/changeOrderStatus/' + orderId + '?newStatus=' + newStatus, {
            method: 'POST',
            headers: {
            }
        }).then(response => {
            if(response.ok) {
                alert('Status zamówienia został zaktualizowany.');
                location.reload();
            } else {
                alert('Wystąpił problem podczas aktualizacji statusu zamówienia.');
            }
        }).catch(error => {
            console.error('Błąd podczas zmiany statusu zamówienia:', error);
            alert('Wystąpił błąd podczas próby zmiany statusu zamówienia.');
        });
    }
}