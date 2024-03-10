package com.example.projektsklep.model.entities.product;

;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryTreeTest {
    @Mock
    CategoryTree parent;
    @Mock
    List<CategoryTree> children;
    @InjectMocks
    CategoryTree categoryTree;

    @BeforeEach
    void setUp() {
        // Given: Inicjalizacja struktury kategorii sprzętu gastronomicznego
        categoryTree = new CategoryTree(1L, "Sprzęt gastronomiczny", null);
    }

    @Test
    void testSetId() {
        // When: Ustawiamy identyfikator dla kategorii
        categoryTree.setId(2L);

        // Then: Sprawdzamy, czy identyfikator został prawidłowo ustawiony
        assertThat(categoryTree.getId()).isEqualTo(2L);
    }

    @Test
    void testSetName() {
        // When: Ustawiamy nazwę dla kategorii
        categoryTree.setName("Urządzenia chłodnicze");

        // Then: Sprawdzamy, czy nazwa została prawidłowo ustawiona
        assertThat(categoryTree.getName()).isEqualTo("Urządzenia chłodnicze");
    }

    @Test
    void testSetParent() {
        // Given: Przygotowanie kategorii nadrzędnej
        CategoryTree parent = new CategoryTree(2L, "Wyposażenie kuchni", null);

        // When: Ustawiamy kategorię nadrzędną
        categoryTree.setParent(parent);

        // Then: Sprawdzamy, czy kategoria nadrzędna została prawidłowo ustawiona
        assertThat(categoryTree.getParent()).isSameAs(parent);
    }

    @Test
    void testSetChildren() {
        // Given: Przygotowanie kategorii podrzędnych
        CategoryTree child1 = new CategoryTree(3L, "Piekarniki", 1L);
        CategoryTree child2 = new CategoryTree(4L, "Zmywarki", 1L);

        // When: Ustawiamy kategorie podrzędne
        categoryTree.setChildren(List.of(child1, child2));

        // Then: Sprawdzamy, czy lista kategorii podrzędnych została prawidłowo ustawiona
        assertThat(categoryTree.getChildren()).containsExactlyInAnyOrder(child1, child2);
    }

    @Test
    public void shouldReturnChildrenList() {
        // Given: Przygotowanie struktury kategorii z kategoriami podrzędnymi
        CategoryTree parentCategory = new CategoryTree(1L, "Sprzęt gastronomiczny", null);
        CategoryTree childCategory1 = new CategoryTree(2L, "Piekarniki", 1L);
        CategoryTree childCategory2 = new CategoryTree(3L, "Zmywarki", 1L);
        parentCategory.setChildren(List.of(childCategory1, childCategory2));

        // When: Pobieramy listę kategorii podrzędnych
        List<CategoryTree> children = parentCategory.getChildren();

        // Then: Weryfikujemy, czy lista zawiera prawidłowe kategorie podrzędne
        assertThat(children).containsExactlyInAnyOrder(childCategory1, childCategory2);
    }

    @Test
    public void shouldReturnParentCategory() {
        // Given: Przygotowanie kategorii podrzędnej z nadrzędną
        CategoryTree parentCategory = new CategoryTree(1L, "Sprzęt gastronomiczny", null);
        CategoryTree childCategory = new CategoryTree(2L, "Piekarniki", 1L);
        childCategory.setParent(parentCategory);

        // When: Pobieramy kategorię nadrzędną kategorii podrzędnej
        CategoryTree parent = childCategory.getParent();

        // Then: Weryfikujemy, czy zwrócona kategoria nadrzędna jest prawidłowa
        assertThat(parent).isSameAs(parentCategory);
    }

    @Test
    public void shouldSetChildrenList() {
        // Given: Przygotowanie kategorii nadrzędnej i podrzędnych
        CategoryTree parentCategory = new CategoryTree(1L, "Sprzęt gastronomiczny", null);
        CategoryTree childCategory1 = new CategoryTree(2L, "Piekarniki", 1L);
        CategoryTree childCategory2 = new CategoryTree(3L, "Zmywarki", 1L);

        // When: Ustawiamy listę kategorii podrzędnych dla kategorii nadrzędnej
        parentCategory.setChildren(List.of(childCategory1, childCategory2));

        // Then: Weryfikujemy, czy lista kategorii podrzędnych została prawidłowo ustawiona
        assertThat(parentCategory.getChildren()).containsExactlyInAnyOrder(childCategory1, childCategory2);
    }
}