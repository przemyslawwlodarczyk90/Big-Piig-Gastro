package com.example.projektsklep.service;


import com.example.projektsklep.model.dto.ProductDTO;

import com.example.projektsklep.model.entities.product.Producer;
import com.example.projektsklep.model.entities.product.Category;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.enums.ProductType;
import com.example.projektsklep.model.repository.ProductRepository;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProducerService producerService;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, ProducerService producerService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.producerService = producerService;
        this.categoryService = categoryService;
    }

    // Metoda zwracająca stronicowaną listę produktów jako DTO.
    public Page<ProductDTO> findAllProductDTOs(Pageable pageable) {
        // Pobiera wszystkie produkty, konwertuje je na DTO i zwraca jako stronicowaną listę.
        return productRepository.findAll(pageable)
                .map(this::convertToProductDTO);
    }

    // Metoda wyszukująca produkt po ID i zwracająca go jako DTO, lub null, jeśli nie znaleziono.
    public ProductDTO findProductDTOById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToProductDTO)
                .orElse(null);
    }

    // Zapisuje produkt na podstawie DTO i zwraca zapisany produkt jako DTO.
    public ProductDTO saveProductDTO(ProductDTO productDTO) {
        Product product = convertToProduct(productDTO); // Konwersja z DTO na encję.
        Product savedProduct = productRepository.save(product); // Zapis produktu.
        return convertToProductDTO(savedProduct); // Konwersja zapisanego produktu na DTO i jego zwrócenie.
    }

    // Wyszukuje produkt po ID i zwraca encję, lub null, jeśli nie znaleziono.
    public Product findProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // Usuwa produkt o podanym ID.
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Konwersja encji produktu na DTO.
    private ProductDTO convertToProductDTO(Product product) {
        // Budowanie DTO produktu z danych encji.
        return ProductDTO.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .miniature(product.getMiniature())
                .price(product.getPrice())
                .productType(product.getProductType())
                .authorId(product.getAuthor() != null ? product.getAuthor().getId() : null) // Bezpieczne przypisanie ID autora.
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null) // Bezpieczne przypisanie ID kategorii.
                .build();
    }

    // Konwersja z DTO produktu na encję.
    private Product convertToProduct(ProductDTO productDTO) {
        Product product = new Product();
        // Ustawianie pól produktu na podstawie danych z DTO.
        product.setId(productDTO.id());
        product.setTitle(productDTO.title());
        product.setDescription(productDTO.description());
        product.setMiniature(productDTO.miniature());
        product.setPrice(productDTO.price());
        product.setProductType(productDTO.productType());

        // Obsługa ustawienia autora i kategorii produktu na podstawie ID.
        if (productDTO.authorId() != null) {
            Producer author = new Producer();
            author.setId(productDTO.authorId()); // Ustawienie ID bez wyszukiwania encji w bazie.
            product.setAuthor(author);
        } else {
            product.setAuthor(null);
        }

        if (productDTO.categoryId() != null) {
            Category category = new Category();
            category.setId(productDTO.categoryId()); // Ustawienie ID bez wyszukiwania encji w bazie.
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        return product;
    }

    // Metoda wyszukująca produkty po tytule z uwzględnieniem wielkości liter i stronicowaniem.
    public Page<ProductDTO> findProductsByTitle(String title, Pageable pageable) {
        // Użycie repozytorium do wyszukania produktów zawierających podany tytuł, niezależnie od wielkości liter.
        return productRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(this::convertToProductDTO); // Konwersja wyników na DTO.
    }

    // Metoda wyszukująca produkty po kategorii z stronicowaniem.
    public Page<ProductDTO> findProductsByCategory(Long categoryId, Pageable pageable) {
        // Użycie repozytorium do wyszukania produktów należących do podanej kategorii.
        return productRepository.findByCategoryId(categoryId, pageable)
                .map(this::convertToProductDTO); // Konwersja wyników na DTO.
    }

    // Metoda wyszukująca produkty po autorze z stronicowaniem.
    public Page<ProductDTO> findProductsByAuthor(Long authorId, Pageable pageable) {
        // Użycie repozytorium do wyszukania produktów stworzonych przez podanego autora.
        return productRepository.findByAuthorId(authorId, pageable)
                .map(this::convertToProductDTO); // Konwersja wyników na DTO.
    }

    // Metoda umożliwiająca wyszukiwanie produktów na podstawie kombinacji kryteriów: tytułu, kategorii i autora.
    public Page<ProductDTO> searchProducts(String title, Long categoryId, Long authorId, Pageable pageable) {
        Page<Product> products;

        // Logika warunkowa decydująca o metodzie wyszukiwania na podstawie dostępności kryteriów.
        if (title != null) {
            products = productRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId, pageable);
        } else if (authorId != null) {
            products = productRepository.findByAuthorId(authorId, pageable);
        } else {
            products = productRepository.findAll(pageable); // Domyślne wyszukiwanie wszystkich produktów, jeśli brak kryteriów.
        }

        return products.map(this::convertToProductDTO); // Konwersja wyników na DTO.
    }

    // Tworzenie domyślnego DTO produktu z przykładowymi danymi.
    public ProductDTO createDefaultProductDTO() {
        // Przykładowe wartości dla nowego produktu.
        Long id = 1L;
        String title = "Nazwa";
        String description = "Opis";
        String miniature = "Miniatura";
        BigDecimal price = BigDecimal.valueOf(10.0);
        ProductType productType = ProductType.DEFAULT_TYPE;

        // Budowanie i zwracanie DTO.
        ProductDTO productDTO = ProductDTO.builder()
                .id(id)
                .title(title)
                .description(description)
                .miniature(miniature)
                .price(price)
                .productType(productType)
                .build();

        return productDTO;
    }

    // Metoda zwracająca listę wszystkich dostępnych typów produktów.
    public List<ProductType> findAllProductTypes() {
        return Arrays.asList(ProductType.values()); // Zwraca listę wszystkich wartości enum ProductType.
    }

    // Przygotowanie modelu dla formularza dodawania produktu.
    public Model prepareAddProductFormModel(Model model) {
        ProductDTO productDTO = createDefaultProductDTO(); // Utworzenie domyślnego DTO produktu.
        model.addAttribute("product", productDTO); // Dodanie produktu do modelu.
        model.addAttribute("productTypes", findAllProductTypes()); // Dodanie listy typów produktów do modelu.
        model.addAttribute("categories", categoryService.findAll()); // Dodanie listy kategorii do modelu.
        model.addAttribute("authors", producerService.findAll()); // Dodanie listy autorów (producentów) do modelu.
        return model; // Zwrócenie przygotowanego modelu.
    }
}

