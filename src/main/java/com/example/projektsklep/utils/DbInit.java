package com.example.projektsklep.utils;

import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.entities.product.Producer;
import com.example.projektsklep.model.entities.product.Category;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.entities.role.Role;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.AdminOrUser;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.model.enums.ProductType;
import com.example.projektsklep.model.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Profile("dev") // Oznacza, że ta klasa konfiguracyjna zostanie użyta tylko w profilu deweloperskim.
@Component
public class DbInit {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final ProducerRepository authorRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public DbInit(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                  CategoryRepository categoryRepository, ProducerRepository authorRepository,
                  ProductRepository productRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @PostConstruct // Metoda wywołana bezpośrednio po utworzeniu beana i wstrzyknięciu zależności.
    private void postConstruct() {
        // Tworzenie i zapisywanie ról w bazie danych.
        Role adminRole = Role.fromAdminOrUser(AdminOrUser.ADMIN);
        Role userRole = Role.fromAdminOrUser(AdminOrUser.USER);
        Role savedAdminRole = roleRepository.save(adminRole);
        Role savedUserRole = roleRepository.save(userRole);

        // Tworzenie i zapisywanie użytkowników w bazie danych.
        User user = User.builder()
                .firstName("user")
                .lastName("user")
                .email("user@example.com")
                .passwordHash(passwordEncoder.encode("user")) // Kodowanie hasła użytkownika.
                .roles(Set.of(savedUserRole)) // Przypisanie roli użytkownika.
                .build();
        User admin = User.builder()
                .firstName("admin")
                .lastName("admin")
                .email("admin@example.com")
                .passwordHash(passwordEncoder.encode("admin")) // Kodowanie hasła admina.
                .roles(Set.of(savedAdminRole)) // Przypisanie roli admina.
                .build();
        userRepository.save(user);
        userRepository.save(admin);

        // Tworzenie kategorii i producentów (autorów).
        List<Category> categories = createCategories();
        List<Producer> authors = createAuthors();

        // Tworzenie produktów.
        createProducts(categories, authors);

        // Przypisywanie zamówień do użytkowników.
        assignOrdersToUsers();
    }

    // Tworzenie kategorii produktów.
    private List<Category> createCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Piece konwekcyjne"));
        categories.add(new Category("Zmywarki przemysłowe"));
        categories.forEach(categoryRepository::save); // Zapis kategorii do bazy danych.
        return categories;
    }

    // Tworzenie producentów (autorów) produktów.
    private List<Producer> createAuthors() {
        List<Producer> authors = new ArrayList<>();
        authors.add(new Producer("Rational"));
        authors.add(new Producer("Electrolux"));
        authors.forEach(authorRepository::save); // Zapis producentów do bazy danych.
        return authors;
    }

    // Tworzenie produktów i przypisywanie ich do kategorii i producentów.
    private void createProducts(List<Category> categories, List<Producer> authors) {
        Random rand = new Random();
        List<String> productNames = List.of("Piec Rational", "Zmywarka Electrolux", "Chłodziarka Coldex", "Mikser Bosh", "Grill Weber");
        for (int i = 0; i < 100; i++) {
            Category category = categories.get(rand.nextInt(categories.size()));
            Producer author = authors.get(rand.nextInt(authors.size()));
            String name = productNames.get(rand.nextInt(productNames.size()));

            Product product = new Product();
            product.setCategory(category);
            product.setAuthor(author);
            product.setTitle(name);
            product.setDescription("Szczegółowy opis " + name);
            product.setMiniature("URL do miniatury " + name);
            product.setPrice(BigDecimal.valueOf(500 + rand.nextDouble() * 1500));
            product.setProductType(ProductType.KITCHEN_EQUIPMENT);
            product.setPublished(true);
            product.setDateCreated(LocalDate.now());
            productRepository.save(product); // Zapis produktu do bazy danych.
        }
    }

    // Przypisywanie zamówień do losowo wybranych użytkowników.
    private void assignOrdersToUsers() {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            for (int i = 1; i <= 20; i++) {
                User user = users.get(i % users.size());
                Order order = new Order();
                order.setAccountHolder(user); // Przypisanie użytkownika do zamówienia.
                order.setOrderStatus(OrderStatus.NEW_ORDER); // Ustawienie statusu zamówienia.
                order.setDateCreated(LocalDate.now()); // Ustawienie daty utworzenia zamówienia.
                order.setTotalPrice(BigDecimal.valueOf(500 + (i * 30))); // Ustawienie ceny zamówienia.
                orderRepository.save(order); // Zapis zamówienia do bazy danych.
            }
        }
    }
}