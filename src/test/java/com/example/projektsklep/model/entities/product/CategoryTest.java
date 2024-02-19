package com.example.projektsklep.model.entities.product;

import com.example.projektsklep.model.dto.CategoryTreeDTO;
import com.example.projektsklep.model.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

class CategoryTest {
    @Mock
    Category parentCategory;
    @Mock
    List<Category> children;
    @InjectMocks
    Category category;
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        category = new Category("Książki");
    }

    @Test
    void testSetId() {
        category.setId(2L);
        assertThat(category.getId()).isEqualTo(2L);
    }

    @Test
    void testSetName() {
        category.setName("Fantastyka");
        assertThat(category.getName()).isEqualTo("Fantastyka");
    }

    @Test
    void testSetParentCategory() {
        Category parent = new Category("Literatura");
        category.setParentCategory(parent);
        assertThat(category.getParentCategory()).isSameAs(parent);
    }

    @Test
    void testSetChildren() {
        Category child1 = new Category("Kryminał");
        Category child2 = new Category("Science Fiction");
        category.setChildren(List.of(child1, child2));
        assertThat(category.getChildren()).containsExactlyInAnyOrder(child1, child2);
    }

    @Test
    public void shouldReturnChildrenList() {
        // Given
        Category parentCategory = new Category("Książki");
        Category childCategory1 = new Category("Fantastyka");
        Category childCategory2 = new Category("Kryminał");
        parentCategory.addChild(childCategory1);
        parentCategory.addChild(childCategory2);

        // When
        List<Category> children = parentCategory.getChildren();

        // Then
        assertThat(children).containsExactlyInAnyOrder(childCategory1, childCategory2);
    }

    @Test
    public void shouldConvertCategoryToTreeDTO() {
        // Given
        Category category = new Category("Książki");

        // When
        CategoryTreeDTO treeDTO = Category.toTreeDTO(category);

        // Then
        assertThat(treeDTO.getId()).isEqualTo(category.getId());
        assertThat(treeDTO.getName()).isEqualTo(category.getName());
    }


}
