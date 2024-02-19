package com.example.projektsklep.utils;


import com.example.projektsklep.model.entities.order.LineOfOrder;
import com.example.projektsklep.model.entities.product.Product;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@SessionScope // Określa, że obiekt tej klasy jest tworzony na potrzeby sesji użytkownika.
public class Basket {

    // Lista linii zamówień przechowująca produkty dodane do koszyka i ich ilości.
    private List<LineOfOrder> lineOfOrders;

    // Konstruktor inicjalizujący listę linii zamówień jako nową, pustą listę.
    public Basket() {
        this.lineOfOrders = new ArrayList<>();
    }

    // Dodaje produkt do koszyka. Jeśli produkt już istnieje, zwiększa jego ilość.
    public void addProduct(Product product, int quantity) {
        LineOfOrder lineOfOrder = findLineByProduct(product); // Wyszukuje, czy produkt jest już w koszyku.
        if (lineOfOrder == null) {
            // Jeśli produktu nie ma w koszyku, tworzy nową linię zamówienia i dodaje ją do listy.
            lineOfOrders.add(new LineOfOrder(product, quantity));
        } else {
            // Jeśli produkt jest już w koszyku, zwiększa jego ilość.
            lineOfOrder.setQuantity(lineOfOrder.getQuantity() + quantity);
        }
    }

    // Usuwa produkt z koszyka. Jeśli ilość jest większa niż 1, zmniejsza ilość. W przeciwnym razie usuwa linię zamówienia.
    public void removeProduct(Product product) {
        LineOfOrder lineOfOrder = findLineByProduct(product); // Wyszukuje linię zamówienia dla danego produktu.
        if (lineOfOrder != null) {
            if (lineOfOrder.getQuantity() > 1) {
                // Jeśli ilość produktu w linii zamówienia jest większa niż 1, zmniejsza ilość.
                lineOfOrder.setQuantity(lineOfOrder.getQuantity() - 1);
            } else {
                // Jeśli ilość produktu jest równa 1, usuwa linię zamówienia z koszyka.
                lineOfOrders.remove(lineOfOrder);
            }
        }
    }

    // Pomocnicza metoda wyszukująca linię zamówienia na podstawie produktu.
    private LineOfOrder findLineByProduct(Product product) {
        return lineOfOrders.stream()
                .filter(line -> line.getProduct().equals(product)) // Filtrowanie linii zamówienia po produkcie.
                .findFirst() // Pobieranie pierwszej pasującej linii zamówienia.
                .orElse(null); // Zwraca null, jeśli nie znaleziono linii dla danego produktu.
    }

    // Czyści koszyk, usuwając wszystkie linie zamówień.
    public void clear() {
        lineOfOrders.clear();
    }

    // Oblicza całkowitą cenę produktów w koszyku.
    public BigDecimal getTotalPrice() {
        BigDecimal total = BigDecimal.ZERO; // Początkowa wartość sumy.
        for (LineOfOrder lineOfOrder : lineOfOrders) {
            // Dodawanie do sumy ceny jednostkowej każdego produktu pomnożonej przez jego ilość.
            total = total.add(lineOfOrder.getUnitPrice().multiply(new BigDecimal(lineOfOrder.getQuantity())));
        }
        return total; // Zwraca całkowitą sumę cen produktów w koszyku.
    }
}