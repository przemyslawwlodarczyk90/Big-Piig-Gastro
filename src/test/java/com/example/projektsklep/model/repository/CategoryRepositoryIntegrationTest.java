package com.example.projektsklep.model.repository;

import com.example.projektsklep.model.entities.product.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryIntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        // Przygotowanie bazy danych przed każdym testem
        categoryRepository.deleteAll();

        Category electronics = new Category("Electronics");
        Category books = new Category("Books");

        categoryRepository.save(electronics);
        categoryRepository.save(books);
    }

    @Test
    void shouldFindCategoryById() {
        // Utworzenie nowej kategorii i zapisanie jej
        Category savedCategory = categoryRepository.save(new Category("Games"));

        // Pobranie kategorii po ID
        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());

        // Weryfikacja
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("Games");
    }

    @Test
    void shouldListAllCategories() {
        // Pobranie wszystkich kategorii
        List<Category> categories = categoryRepository.findAll();

        // Weryfikacja, że lista zawiera przynajmniej dwie kategorie
        assertThat(categories).hasSizeGreaterThanOrEqualTo(2);
    }
}