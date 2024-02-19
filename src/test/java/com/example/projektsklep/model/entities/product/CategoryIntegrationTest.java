package com.example.projektsklep.model.entities.product;

import com.example.projektsklep.model.dto.CategoryTreeDTO;
import com.example.projektsklep.model.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class CategoryIntegrationTest {
    @Mock
    private CategoryRepository categoryRepository;


    @Test
    public void givenCategory_whenConvertToDTO_thenCorrectDTOIsReturned() {
        // Given
        Category category = new Category("Test Category");
        category.setId(1L);

        // When
        CategoryTreeDTO categoryDTO = Category.toTreeDTO(category);

        // Then
        assertEquals(category.getId(), ((CategoryTreeDTO) categoryDTO).getId());
        assertEquals(category.getName(), categoryDTO.getName());

    }

}
