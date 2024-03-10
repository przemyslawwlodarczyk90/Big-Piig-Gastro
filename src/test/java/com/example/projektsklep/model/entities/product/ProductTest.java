package com.example.projektsklep.model.entities.product;

import com.example.projektsklep.model.enums.ProductType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductTest {
    @Mock
    private Producer mockAuthor; // Symulacja producenta produktu, używana w testach.
    @Mock
    private Category mockCategory; // Symulacja kategorii produktu, używana w testach.
    @InjectMocks
    private Product product; // Instancja produktu testowanego z wstrzykniętymi zależnościami.

    @Test
    public void testConstructorWithAllArgs_ShouldCreateProductCorrectly() {
        // Given: Utworzenie instancji produktu z wszystkimi argumentami konstruktora.
        Product product = new Product(1L, mockAuthor, mockCategory, "Title", "Description",
                "miniature.jpg", BigDecimal.TEN, ProductType.COOKING_EQUIPMENT, true, LocalDate.now());

        // Then: Sprawdzenie, czy produkt został poprawnie utworzony z podanymi argumentami.
        assertEquals(1L, product.getId());
        assertEquals(mockAuthor, product.getAuthor());
        // Tutaj można dodać więcej asercji sprawdzających pozostałe pola.
    }

    @Test
    public void testSetAuthor_ShouldUpdateAuthorCorrectly() {
        // Given: Ustawienie producenta produktu.
        Product product = new Product();
        product.setAuthor(mockAuthor);

        // Then: Weryfikacja, czy producent został poprawnie ustawiony.
        assertSame(mockAuthor, product.getAuthor());
    }

    @Test
    public void testSetDescription_ShouldSetDescription() {
        // Given: Ustawienie opisu produktu.
        Product product = new Product();
        String newDescription = "New description";
        product.setDescription(newDescription);

        // Then: Weryfikacja, czy opis został poprawnie ustawiony.
        assertEquals(newDescription, product.getDescription());
    }

    @Test
    public void testSetDescription_ShouldSetEmptyDescription() {
        // Given: Ustawienie pustego opisu produktu.
        Product product = new Product();
        product.setDescription("");

        // Then: Weryfikacja, czy pusty opis został poprawnie ustawiony.
        assertEquals("", product.getDescription());
    }

    @Test
    public void testIsPublished_ShouldReturnFalseForNewProduct() {
        // Given: Nowy produkt bez ustawienia flagi opublikowania.
        Product product = new Product();

        // Then: Weryfikacja, czy domyślnie produkt nie jest oznaczony jako opublikowany.
        assertFalse(product.isPublished());
    }

    @Test
    public void testSetPublished_ShouldSetTrue() {
        // Given: Ustawienie flagi opublikowania produktu.
        Product product = new Product();
        product.setPublished(true);

        // Then: Weryfikacja, czy produkt został oznaczony jako opublikowany.
        assertTrue(product.isPublished());
    }

}