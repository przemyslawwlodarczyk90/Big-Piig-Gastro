package com.example.projektsklep.model.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class CategoryDTOTest {

    @Test
    public void testConstructor() {
        Long id = 1L;
        String name = "Elektronika";
        Long parentCategoryId = 2L;
        String parentCategoryName = "Sprzęt AGD";

        CategoryDTO categoryDTO = new CategoryDTO(id, name, parentCategoryId, parentCategoryName);

        Assertions.assertEquals(id, categoryDTO.id());
        Assertions.assertEquals(name, categoryDTO.name());
        Assertions.assertEquals(parentCategoryId, categoryDTO.parentCategoryId());
        Assertions.assertEquals(parentCategoryName, categoryDTO.parentCategoryName());
    }
}
