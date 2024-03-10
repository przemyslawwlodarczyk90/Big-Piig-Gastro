package com.example.projektsklep.model.entities.product;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryTreeTest {

    @InjectMocks
    CategoryTree categoryTree;

    @BeforeEach
    void setUp() {

        categoryTree = new CategoryTree(1L, "Sprzęt gastronomiczny", null);
    }

    @Test
    void testSetId() {

        categoryTree.setId(2L);

        assertThat(categoryTree.getId()).isEqualTo(2L);
    }

    @Test
    void testSetName() {

        categoryTree.setName("Urządzenia chłodnicze");

        assertThat(categoryTree.getName()).isEqualTo("Urządzenia chłodnicze");
    }

    @Test
    void testSetParent() {

        CategoryTree parent = new CategoryTree(2L, "Wyposażenie kuchni", null);

        categoryTree.setParent(parent);

        assertThat(categoryTree.getParent()).isSameAs(parent);
    }

    @Test
    void testSetChildren() {

        CategoryTree child1 = new CategoryTree(3L, "Piekarniki", 1L);
        CategoryTree child2 = new CategoryTree(4L, "Zmywarki", 1L);

        categoryTree.setChildren(List.of(child1, child2));

        assertThat(categoryTree.getChildren()).containsExactlyInAnyOrder(child1, child2);
    }

    @Test
     void shouldReturnChildrenList() {

        CategoryTree parentCategory = new CategoryTree(1L, "Sprzęt gastronomiczny", null);
        CategoryTree childCategory1 = new CategoryTree(2L, "Piekarniki", 1L);
        CategoryTree childCategory2 = new CategoryTree(3L, "Zmywarki", 1L);
        parentCategory.setChildren(List.of(childCategory1, childCategory2));

        List<CategoryTree> children = parentCategory.getChildren();

        assertThat(children).containsExactlyInAnyOrder(childCategory1, childCategory2);
    }

    @Test
     void shouldReturnParentCategory() {

        CategoryTree parentCategory = new CategoryTree(1L, "Sprzęt gastronomiczny", null);
        CategoryTree childCategory = new CategoryTree(2L, "Piekarniki", 1L);
        childCategory.setParent(parentCategory);

        CategoryTree parent = childCategory.getParent();

        assertThat(parent).isSameAs(parentCategory);
    }

    @Test
     void shouldSetChildrenList() {

        CategoryTree parentCategory = new CategoryTree(1L, "Sprzęt gastronomiczny", null);
        CategoryTree childCategory1 = new CategoryTree(2L, "Piekarniki", 1L);
        CategoryTree childCategory2 = new CategoryTree(3L, "Zmywarki", 1L);

        parentCategory.setChildren(List.of(childCategory1, childCategory2));

        assertThat(parentCategory.getChildren()).containsExactlyInAnyOrder(childCategory1, childCategory2);
    }
}