package com.example.projektsklep.model.entities.product;

import com.example.projektsklep.model.dto.CategoryTreeDTO;
import com.example.projektsklep.model.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Testy integracyjne dla konwersji encji kategorii produktów do obiektów transferu danych (DTO).
 * Używa Mockito do mockowania repozytorium kategorii.
 */
@ExtendWith(MockitoExtension.class)
public class CategoryIntegrationTest {
    @Mock
    private CategoryRepository categoryRepository; // Mock repozytorium kategorii

    /**
     * Testuje konwersję encji kategorii do DTO.
     * Sprawdza, czy po konwersji, DTO zawiera poprawne id i nazwę kategorii.
     */
    @Test
    public void givenCategory_whenConvertToDTO_thenCorrectDTOIsReturned() {
        // Given
        Category category = new Category("Test Category");
        category.setId(1L); // Ustawienie id kategorii

        // When
        CategoryTreeDTO categoryDTO = Category.toTreeDTO(category); // Konwersja kategorii na DTO

        // Then
        assertEquals(category.getId(), categoryDTO.getId()); // Sprawdzenie, czy id się zgadza
        assertEquals(category.getName(), categoryDTO.getName()); // Sprawdzenie, czy nazwa się zgadza
    }
}