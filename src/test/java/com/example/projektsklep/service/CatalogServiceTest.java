package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.*;
import com.example.projektsklep.model.entities.product.*;
import com.example.projektsklep.model.enums.ProductType;
import com.example.projektsklep.model.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.ui.ExtendedModelMap;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CatalogServiceTest {
    private final ProductRepository products = mock(ProductRepository.class);
    private final CategoryRepository categories = mock(CategoryRepository.class);
    private final ProducerRepository producers = mock(ProducerRepository.class);
    private final CategoryService categoryService = new CategoryService(categories);
    private final ProducerService producerService = new ProducerService(producers);
    private final ProductService service = new ProductService(products, producerService, categoryService);
    private final Pageable page = PageRequest.of(0, 10);

    @Test
    void listingIncludesCurrentAvailabilityAndRelations() {
        // DTO pokazuje stan dostepny do sprzedazy, takze przy opoznieniu.
        var product = new Product("Oven", BigDecimal.TEN);
        product.setId(4L);
        product.setStockQuantity(12);
        var author = new Producer("Brand"); author.setId(2L); product.setAuthor(author);
        var category = new Category("Kitchen"); category.setId(3L); product.setCategory(category);
        when(products.findAll(page)).thenReturn(new PageImpl<>(List.of(product)));
        var dto = service.findAllProductDTOs(page).getContent().get(0);
        assertEquals(12, dto.quantity());
        assertEquals(2L, dto.authorId());
        assertEquals(3L, dto.categoryId());
        product.setUnavailableUntil(Instant.now().plusSeconds(300));
        when(products.findById(4L)).thenReturn(Optional.of(product));
        assertEquals(0, service.findProductDTOById(4L).quantity());
        assertNull(service.findProductDTOById(99L));
    }

    @Test
    void searchRoutesEachFilterToRepository() {
        // Wyszukiwanie zachowuje wyniki i paginacje dla kazdego filtra.
        var result = new PageImpl<>(List.of(new Product("Oven", BigDecimal.TEN)));
        when(products.findByTitleContainingIgnoreCase("oven", page)).thenReturn(result);
        when(products.findByCategoryId(2L, page)).thenReturn(result);
        when(products.findByAuthorId(3L, page)).thenReturn(result);
        when(products.findAll(page)).thenReturn(result);
        assertEquals("Oven", service.searchProducts("oven", null, null, page).getContent().get(0).title());
        assertEquals(1, service.searchProducts(null, 2L, null, page).getTotalElements());
        assertEquals(1, service.searchProducts(null, null, 3L, page).getTotalElements());
        assertEquals(1, service.searchProducts(null, null, null, page).getTotalElements());
    }

    @Test
    void editingMetadataPreservesStockAndCreatingUsesNewIdentity() {
        // Edycja opisu nie nadpisuje stanu magazynu.
        var existing = new Product("Old", BigDecimal.ONE);
        existing.setId(1L); existing.setStockQuantity(8);
        when(products.findById(1L)).thenReturn(Optional.of(existing));
        when(products.save(any())).thenAnswer(call -> call.getArgument(0));
        var edited = service.saveProductDTO(ProductDTO.builder().id(1L).title("New").price(BigDecimal.TEN)
                .authorId(3L).categoryId(4L).productType(ProductType.KITCHEN_EQUIPMENT).build());
        assertEquals(8, edited.quantity());
        assertEquals("New", edited.title());
        assertEquals(3L, edited.authorId());
        var created = service.saveProductDTO(ProductDTO.builder().title("New product").price(BigDecimal.ONE).build());
        assertNull(created.id());
        assertEquals(0, created.quantity());
        assertThrows(IllegalArgumentException.class, () -> service.saveProductDTO(ProductDTO.builder().id(99L).build()));
        var form = new ExtendedModelMap();
        service.prepareAddProductFormModel(form);
        assertNull(((ProductDTO) form.get("product")).id());
        assertTrue(((List<?>) form.get("productTypes")).contains(ProductType.KITCHEN_EQUIPMENT));
        assertTrue(form.containsAttribute("categories"));
    }

    @Test
    void categoryCrudMaintainsParentInformation() {
        // Nowa podkategoria zachowuje dane rodzica w DTO.
        var parent = new Category("Parent"); parent.setId(1L);
        when(categories.findById(1L)).thenReturn(Optional.of(parent));
        when(categories.save(any())).thenAnswer(call -> call.getArgument(0));
        var child = new Category("Child"); child.setId(2L); child.setParentCategory(parent);
        when(categories.findById(2L)).thenReturn(Optional.of(child));
        when(categories.findAll()).thenReturn(List.of(parent, child));
        var dto = categoryService.addCategoryWithParent(new CategoryDTO(null, "Child", 1L, null));
        assertEquals("Parent", dto.parentCategoryName());
        assertEquals(1L, dto.parentCategoryId());
        categoryService.addCategoryWithParent(new CategoryDTO(null, "Root", null, null));
        categoryService.updateCategoryDTO(2L, new CategoryDTO(2L, "Renamed", 1L, null));
        assertEquals("Renamed", child.getName());
        assertNull(categoryService.getCategoryDTOById(99L));
        assertEquals("Parent", categoryService.getCategoriesWithParentName().get(1).parentCategoryName());
        categoryService.deleteCategoryById(2L);
        verify(categories).deleteById(2L);
        assertThrows(RuntimeException.class, () -> categoryService.updateCategoryDTO(99L, dto));
        assertThrows(RuntimeException.class, () -> categoryService.addCategoryWithParent(new CategoryDTO(null, "Bad", 99L, null)));
        var form = new ExtendedModelMap(); categoryService.prepareAddCategoryModel(form);
        assertEquals(2, ((List<?>) form.get("allCategories")).size());
        var original = form.get("categoryDTO"); categoryService.prepareAddCategoryModel(form);
        assertSame(original, form.get("categoryDTO"));
    }

    @Test
    void producerCreationDoesNotDuplicateExistingBrand() {
        // Ponowny zapis tej samej marki nie tworzy duplikatu.
        var brand = new Producer("Brand"); brand.setId(1L);
        when(producers.findByName("Brand")).thenReturn(Optional.of(brand));
        producerService.saveAuthor(new ProducerDTO(null, "Brand"));
        verify(producers, never()).save(any());
        when(producers.save(any())).thenAnswer(call -> call.getArgument(0));
        producerService.saveAuthor(new ProducerDTO(null, "Another"));
        verify(producers).save(any());
        when(producers.findAll(page)).thenReturn(new PageImpl<>(List.of(brand)));
        when(producers.findAll()).thenReturn(List.of(brand));
        assertEquals("Brand", producerService.findAllAuthorsPageable(page).getContent().get(0).name());
        assertEquals(1L, producerService.findAll().get(0).id());
    }
}
