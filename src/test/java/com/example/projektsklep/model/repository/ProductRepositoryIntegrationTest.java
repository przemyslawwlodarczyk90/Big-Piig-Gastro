package com.example.projektsklep.model.repository;

import com.example.projektsklep.model.entities.product.Category;
import com.example.projektsklep.model.entities.product.Producer;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.enums.ProductType;
import com.example.projektsklep.model.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductRepositoryIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product sampleProduct;
    private Producer sampleProducer; // Załóżmy, że taki obiekt istnieje w bazie danych
    private Category sampleCategory; // Załóżmy, że taki obiekt istnieje w bazie danych

    @BeforeEach
    public void setup() {
        // Załóżmy, że sampleProducer i sampleCategory zostały już utworzone w bazie danych
        sampleProduct = new Product();
        sampleProduct.setTitle("Testowy produkt");
        sampleProduct.setDescription("Opis testowego produktu");
        sampleProduct.setPrice(BigDecimal.valueOf(99.99));
        sampleProduct.setAuthor(sampleProducer); // Ustaw odpowiedni sampleProducer
        sampleProduct.setCategory(sampleCategory); // Ustaw odpowiedni sampleCategory
        sampleProduct.setProductType(ProductType.KITCHEN_EQUIPMENT);
        sampleProduct.setPublished(true);
        sampleProduct.setDateCreated(LocalDate.now());

        productRepository.save(sampleProduct);
    }

    @AfterEach
    public void cleanup() {
        productRepository.deleteAll();
    }

    @Test
    public void testFindAllPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> products = productRepository.findAll(pageable);

        assertFalse(products.isEmpty(), "Strona produktów nie powinna być pusta.");
    }

    @Test
    public void testFindByTitleContainingIgnoreCase() {
        String searchTitle = "testowy";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> products = productRepository.findByTitleContainingIgnoreCase(searchTitle, pageable);

        assertFalse(products.isEmpty(), "Strona produktów zawierających tytuł nie powinna być pusta.");
    }

    @Test
    public void testFindByCategoryId() {
        // Przygotowanie
        Category category = new Category(); // Utwórz odpowiednią kategorię
        category.setName("Testowa Kategoria");
        // Zakładając, że istnieje metoda save w repozytorium kategorii
        category = categoryRepository.save(category); // Zapisz kategorię, aby miała przydzielone ID

        Product product = new Product("Testowy produkt", BigDecimal.valueOf(99.99));
        product.setCategory(category); // Ustaw kategorię produktu
        productRepository.save(product); // Zapisz produkt

        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productsPage = productRepository.findByCategoryId(category.getId(), pageable);

        // Weryfikacja
        assertFalse(productsPage.isEmpty(), "Strona produktów powinna zawierać produkty z danej kategorii.");
    }
    @Test
    public void testDeleteById() {
        Long productId = productRepository.findAll().get(0).getId(); // Pobierz ID pierwszego zapisanego produktu
        productRepository.deleteById(productId);

        Optional<Product> deletedProduct = productRepository.findById(productId);
        assertTrue(deletedProduct.isEmpty(), "Produkt powinien zostać usunięty.");
    }
}
