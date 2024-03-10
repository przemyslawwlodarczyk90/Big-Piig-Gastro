package com.example.projektsklep.model.entities.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class CategoryTest {

    @InjectMocks
    Category category;

    @BeforeEach
    void setUp() {

        category = new Category("Sprzęt gastronomiczny");
    }

    @Test
    void testSetId() {

        category.setId(2L);

        assertThat(category.getId()).isEqualTo(2L);
    }

    @Test
    void testSetName() {

        category.setName("Urządzenia chłodnicze");

        assertThat(category.getName()).isEqualTo("Urządzenia chłodnicze");
    }

    @Test
    void testSetParentCategory() {

        Category parent = new Category("Sprzęt kuchenny");

        category.setParentCategory(parent);

        assertThat(category.getParentCategory()).isSameAs(parent);
    }

    @Test
    void testSetChildren() {

        Category child1 = new Category("Piekarniki konwekcyjne");
        Category child2 = new Category("Miksery planetarne");

        category.setChildren(List.of(child1, child2));

        assertThat(category.getChildren()).containsExactlyInAnyOrder(child1, child2);
    }

    @Test
     void shouldReturnChildrenList() {
        Category parentCategory = new Category("Sprzęt gastronomiczny");
        parentCategory.addChild(new Category("Grille kontaktowe"));
        parentCategory.addChild(new Category("Zmywarki gastronomiczne"));

        List<Category> children = parentCategory.getChildren();

        assertThat(children).hasSize(2).extracting("name").contains("Grille kontaktowe", "Zmywarki gastronomiczne");
    }


}