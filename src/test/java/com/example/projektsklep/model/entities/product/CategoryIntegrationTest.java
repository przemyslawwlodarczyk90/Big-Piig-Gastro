package com.example.projektsklep.model.entities.product;

import com.example.projektsklep.model.dto.CategoryTreeDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
 class CategoryIntegrationTest {


    @Test
     void givenCategory_whenConvertToDTO_thenCorrectDTOIsReturned() {

        Category category = new Category("Test Category");
        category.setId(1L);

        CategoryTreeDTO categoryDTO = Category.toTreeDTO(category);

        assertEquals(category.getId(), categoryDTO.getId());
        assertEquals(category.getName(), categoryDTO.getName());
    }
}