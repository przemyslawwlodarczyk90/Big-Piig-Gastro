package com.example.projektsklep.model.repository;

import com.example.projektsklep.model.entities.product.Category;
import com.example.projektsklep.model.entities.product.Producer;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.enums.ProductType;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
 class ProductRepositoryIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product sampleProduct;
    private Producer sampleProducer;
    private Category sampleCategory;

    @BeforeEach
    public void setup() {

        sampleProduct = new Product();
        sampleProduct.setTitle("Testowy produkt");
        sampleProduct.setDescription("Opis testowego produktu");
        sampleProduct.setPrice(BigDecimal.valueOf(99.99));
        sampleProduct.setAuthor(sampleProducer);
        sampleProduct.setCategory(sampleCategory);
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
     void testFindAllPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> products = productRepository.findAll(pageable);

        assertFalse(products.isEmpty(), "The products page should not be empty.");
    }

    @Test
     void testFindByTitleContainingIgnoreCase() {
        String searchTitle = "testowy";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> products = productRepository.findByTitleContainingIgnoreCase(searchTitle, pageable);

        assertFalse(products.isEmpty(), "The products page containing the title should not be blank.");
    }

    @Test
    public void testFindByCategoryId() {

        Category category = new Category();
        category.setName("Testowa Kategoria");

        category = categoryRepository.save(category);

        Product product = new Product("Testowy produkt", BigDecimal.valueOf(99.99));
        product.setCategory(category);
        productRepository.save(product);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productsPage = productRepository.findByCategoryId(category.getId(), pageable);

        assertFalse(productsPage.isEmpty(), "The products page should contain products from the category.");
    }
    @Test
    public void testDeleteById() {
        Long productId = productRepository.findAll().get(0).getId();
        productRepository.deleteById(productId);

        Optional<Product> deletedProduct = productRepository.findById(productId);
        assertTrue(deletedProduct.isEmpty(), "The product should be removed.");
    }
}
