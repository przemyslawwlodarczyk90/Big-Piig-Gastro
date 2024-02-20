package com.example.projektsklep.model.entities.order;

import com.example.projektsklep.model.entities.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Klasa reprezentująca linię zamówienia, czyli pojedynczą pozycję zamówienia zawierającą produkt i jego ilość.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "line_of_orders")
public class LineOfOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unikalny identyfikator linii zamówienia

    @ManyToOne
    @JoinColumn(name = "orderId", referencedColumnName = "id")
    private Order order; // Zamówienie, do którego należy ta linia zamówienia

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // Produkt zawarty w linii zamówienia

    private int quantity; // Ilość produktu

    private BigDecimal unitPrice; // Cena jednostkowa produktu

    /**
     * Konstruktor tworzący linię zamówienia z podanym produktem i jego ilością.
     * Ustawia również cenę jednostkową produktu na podstawie ceny produktu.
     * @param product Produkt do dodania do linii zamówienia
     * @param quantity Ilość produktu
     */
    public LineOfOrder(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice(); // Pobiera cenę produktu jako cenę jednostkową
    }

    /**
     * Oblicza całkowitą cenę dla tej linii zamówienia, mnożąc cenę jednostkową przez ilość.
     * @return Całkowita cena dla linii zamówienia
     */
    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(new BigDecimal(quantity));
    }
}