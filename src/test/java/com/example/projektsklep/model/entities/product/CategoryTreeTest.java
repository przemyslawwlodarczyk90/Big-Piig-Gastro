package com.example.projektsklep.model.entities.product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CategoryTreeTest {
    @Mock
    CategoryTree parent;
    @Mock
    List<CategoryTree> children;
    @InjectMocks
    CategoryTree categoryTree;

    @BeforeEach
    void setUp() {
        categoryTree = new CategoryTree(1L, "Książki", null);
    }

    @Test
    void testSetId() {
        categoryTree.setId(2L);
        assertThat(categoryTree.getId()).isEqualTo(2L);
    }

    @Test
    void testSetName() {
        categoryTree.setName("Fantastyka");
        assertThat(categoryTree.getName()).isEqualTo("Fantastyka");
    }

    @Test
    void testSetParent() {
        CategoryTree parent = new CategoryTree(2L, "Literatura", null);
        categoryTree.setParent(parent);
        assertThat(categoryTree.getParent()).isSameAs(parent);
    }

    @Test
    void testSetChildren() {
        CategoryTree child1 = new CategoryTree(3L, "Kryminał", 1L);
        CategoryTree child2 = new CategoryTree(4L, "Science Fiction", 1L);
        categoryTree.setChildren(List.of(child1, child2));
        assertThat(categoryTree.getChildren()).containsExactlyInAnyOrder(child1, child2);
    }

    @Test
    public void shouldReturnChildrenList() {
        // Given
        CategoryTree parentCategory = new CategoryTree(1L, "Książki", null);
        CategoryTree childCategory1 = new CategoryTree(2L, "Fantastyka", 1L);
        CategoryTree childCategory2 = new CategoryTree(3L, "Kryminał", 1L);
        parentCategory.setChildren(List.of(childCategory1, childCategory2));

        // When
        List<CategoryTree> children = parentCategory.getChildren();

        // Then
        assertThat(children).containsExactlyInAnyOrder(childCategory1, childCategory2);
    }
    @Test
    public void shouldReturnParentCategory() {
        // Given
        CategoryTree parentCategory = new CategoryTree(1L, "Książki", null);
        CategoryTree childCategory = new CategoryTree(2L, "Fantastyka", 1L);
        childCategory.setParent(parentCategory);

        // When
        CategoryTree parent = childCategory.getParent();

        // Then
        assertThat(parent).isSameAs(parentCategory);
    }
    @Test
    public void shouldSetChildrenList() {
        // Given
        CategoryTree parentCategory = new CategoryTree(1L, "Książki", null);
        CategoryTree childCategory1 = new CategoryTree(2L, "Fantastyka", 1L);
        CategoryTree childCategory2 = new CategoryTree(3L, "Kryminał", 1L);

        // When
        parentCategory.setChildren(List.of(childCategory1, childCategory2));

        // Then
        assertThat(parentCategory.getChildren()).containsExactlyInAnyOrder(childCategory1, childCategory2);
    }


}

