package com.example.projektsklep.model.entities.product;

import com.example.projektsklep.model.dto.CategoryTreeDTO;
import com.example.projektsklep.model.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
/**
 * Testy jednostkowe dla klasy Category, reprezentującej kategorie sprzętu gastronomicznego w branży HoReCa.
 * Wykorzystuje Mockito do mockowania zależności i wstrzykiwania ich do testowanej klasy.
 */
@ExtendWith(MockitoExtension.class)
class CategoryTest {
    @Mock
    Category parentCategory; // Mock kategorii nadrzędnej
    @Mock
    List<Category> children; // Mock listy kategorii podrzędnych
    @InjectMocks
    Category category; // Testowana instancja kategorii, do której wstrzykiwane są mocki

    @BeforeEach
    void setUp() {
        // Given: Inicjalizacja mocków i przygotowanie kategorii sprzętu gastronomicznego
        category = new Category("Sprzęt gastronomiczny");
    }

    @Test
    void testSetId() {
        // When: Ustawiamy identyfikator dla kategorii
        category.setId(2L);

        // Then: Sprawdzamy, czy identyfikator został prawidłowo ustawiony
        assertThat(category.getId()).isEqualTo(2L);
    }

    @Test
    void testSetName() {
        // When: Ustawiamy nazwę dla kategorii
        category.setName("Urządzenia chłodnicze");

        // Then: Sprawdzamy, czy nazwa została prawidłowo ustawiona
        assertThat(category.getName()).isEqualTo("Urządzenia chłodnicze");
    }

    @Test
    void testSetParentCategory() {
        // Given: Przygotowanie kategorii nadrzędnej
        Category parent = new Category("Sprzęt kuchenny");

        // When: Ustawiamy kategorię nadrzędną
        category.setParentCategory(parent);

        // Then: Sprawdzamy, czy kategoria nadrzędna została prawidłowo ustawiona
        assertThat(category.getParentCategory()).isSameAs(parent);
    }

    @Test
    void testSetChildren() {
        // Given: Przygotowanie kategorii podrzędnych
        Category child1 = new Category("Piekarniki konwekcyjne");
        Category child2 = new Category("Miksery planetarne");

        // When: Ustawiamy listę kategorii podrzędnych
        category.setChildren(List.of(child1, child2));

        // Then: Sprawdzamy, czy lista kategorii podrzędnych została prawidłowo ustawiona
        assertThat(category.getChildren()).containsExactlyInAnyOrder(child1, child2);
    }

    @Test
    public void shouldReturnChildrenList() {
        // Given: Przygotowanie kategorii nadrzędnej z podrzędnymi
        Category parentCategory = new Category("Sprzęt gastronomiczny");
        parentCategory.addChild(new Category("Grille kontaktowe"));
        parentCategory.addChild(new Category("Zmywarki gastronomiczne"));

        // When: Pobieramy listę kategorii podrzędnych
        List<Category> children = parentCategory.getChildren();

        // Then: Sprawdzamy, czy lista zawiera oczekiwane kategorie
        assertThat(children).hasSize(2).extracting("name").contains("Grille kontaktowe", "Zmywarki gastronomiczne");
    }


}