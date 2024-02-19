package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.LineOfOrderDTO;
import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.model.entities.order.LineOfOrder;
import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.OrderStatus;

import com.example.projektsklep.model.repository.OrderRepository;
import com.example.projektsklep.model.repository.ProductRepository;
import com.example.projektsklep.utils.Basket;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SessionScope
// Określa, że bean serwisu będzie istniał w zakresie sesji HTTP, co pozwala na zachowanie stanu koszyka dla każdego użytkownika oddzielnie.

public class BasketService {

    // Repozytoria i serwisy wstrzyknięte przez Springa do zarządzania zamówieniami, użytkownikami i produktami.
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductRepository productRepository;

    // Mapa przechowująca produkty dodane do koszyka i ich ilości.
    private final Map<Long, Integer> products = new HashMap<>();
    // Obiekt koszyka, który może być używany do przechowywania dodatkowych informacji o zamówieniu.
    private Basket basket;

    // Konstruktor klasy serwisu, inicjalizujący wstrzyknięte repozytoria i serwisy oraz tworzący nowy koszyk.
    public BasketService(OrderRepository orderRepository, UserService userService, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.productRepository = productRepository;
        this.basket = new Basket();
    }

    // Dodaje produkt do koszyka lub zwiększa jego ilość, jeśli już jest w koszyku.
    public void addProduct(Product product) {
        products.put(product.getId(), products.getOrDefault(product.getId(), 0) + 1);
    }

    // Zmniejsza ilość danego produktu w koszyku o jeden lub usuwa go, jeśli ilość spadnie do zera.
    public void takeOneItemFromQuantity(Long productId) {
        products.computeIfPresent(productId, (k, v) -> v > 1 ? v - 1 : null);
    }

    // Usuwa produkt z koszyka całkowicie, niezależnie od jego ilości.
    public void completelyRemoveProduct(Long productId) {
        products.remove(productId);
    }

    // Zwraca aktualną mapę produktów w koszyku.
    public Map<Long, Integer> getProducts() {
        return new HashMap<>(products);
    }

    // Czyści koszyk, usuwając wszystkie produkty.
    public void clear() {
        products.clear();
    }

    // Pobiera aktualny stan koszyka; tworzy nowy, jeśli jeszcze nie istnieje.
    public Basket getCurrentBasket() {
        if (this.basket == null) {
            this.basket = new Basket();
        }
        return this.basket;
    }

    // Aktualizuje ilość produktu w koszyku; usuwa produkt, jeśli nowa ilość to 0.
    public void updateProductQuantity(Long productId, int quantity) {
        if (products.containsKey(productId)) {
            if (quantity > 0) {
                products.put(productId, quantity);
            } else {
                takeOneItemFromQuantity(productId);
            }
        }
    }

    // Tworzy DTO dla inicjalnego zamówienia z pustą listą linii zamówienia i domyślnym adresem dostawy.
    public OrderDTO createInitialOrderDTO() {
        AddressDTO addressDTO = new AddressDTO(null, "", "", "", "");
        return OrderDTO.builder()
                .lineOfOrders(new ArrayList<>())
                .shippingAddress(addressDTO)
                .build();
    }

    /**
     * Tworzy DTO (Data Transfer Object) zamówienia na podstawie aktualnego stanu koszyka.
     *
     * @param userId Identyfikator użytkownika, dla którego tworzone jest zamówienie.
     * @return DTO zamówienia zawierające wszystkie produkty z koszyka, ich ilość, całkowitą cenę oraz adres dostawy.
     */
    public OrderDTO createOrderDTOFromBasket(Long userId) {
        // Inicjalizacja listy linii zamówień oraz zmiennej na całkowitą cenę zamówienia.
        List<LineOfOrderDTO> lineOfOrdersDTO = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        // Iteracja przez każdy produkt w koszyku (mapa produktów i ich ilości).
        for (Map.Entry<Long, Integer> entry : products.entrySet()) {
            Long productId = entry.getKey(); // Identyfikator produktu.
            Integer quantity = entry.getValue(); // Ilość produktu.

            // Wyszukanie produktu w bazie danych.
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono produktu o ID: " + productId));

            // Obliczenie ceny linii zamówienia (cena produktu * ilość).
            BigDecimal linePrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            // Dodanie ceny linii zamówienia do całkowitej ceny zamówienia.
            totalPrice = totalPrice.add(linePrice);

            // Utworzenie DTO linii zamówienia i dodanie do listy.
            LineOfOrderDTO lineOfOrderDTO = new LineOfOrderDTO(null, productId, quantity, product.getPrice());
            lineOfOrdersDTO.add(lineOfOrderDTO);
        }

        // Utworzenie pustego adresu dostawy (w tym miejscu nie jest określany konkretny adres).
        AddressDTO shippingAddress = new AddressDTO(null, "", "", "", "");

        // Utworzenie i zwrócenie DTO zamówienia z wyliczoną całkowitą ceną, listą linii zamówień oraz adresem dostawy.
        return new OrderDTO(null, userId, "NEW", LocalDate.now(), null, totalPrice, lineOfOrdersDTO, shippingAddress);
    }

    // Dodaje produkt do koszyka, wyszukując go najpierw w bazie danych po ID.
    public void addProductToBasket(Long productId, int quantity) {
        // Wyszukuje produkt w bazie danych po podanym ID.
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono produktu o ID: " + productId));
        // Dodaje produkt do koszyka wraz z określoną ilością.
        basket.addProduct(product, quantity);
    }

    // Metoda placeholder - obecnie zwraca nowy, pusty koszyk i nie jest używana.
    private Basket getCurrentUserBasket() {
        return new Basket();
    }

    // Przyjmuje DTO zamówienia i tworzy na jego podstawie nowe zamówienie w systemie.
    public void placeOrder(OrderDTO orderDTO) {
        Order newOrder = new Order(); // Tworzy nową instancję zamówienia.
        newOrder.setOrderStatus(OrderStatus.NEW_ORDER); // Ustawia status zamówienia na nowe.
        newOrder.setDateCreated(LocalDate.now()); // Ustawia datę utworzenia zamówienia na aktualną datę.

        // Wyszukuje użytkownika na podstawie ID z DTO i przypisuje go jako właściciela zamówienia.
        UserDTO userDTO = userService.findUserById(orderDTO.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        User user = userService.convertToUser(userDTO);
        newOrder.setAccountHolder(user);

        // Konwertuje produkty z koszyka na linie zamówienia i dodaje je do zamówienia.
        List<LineOfOrder> lineOfOrders = convertBasketToLineOfOrders();
        newOrder.setLineOfOrders(lineOfOrders);

        // Oblicza całkowitą cenę zamówienia na podstawie linii zamówienia.
        newOrder.calculateTotalPrice();

        // Zapisuje zamówienie w bazie danych.
        orderRepository.save(newOrder);

        // Czyści koszyk po złożeniu zamówienia.
        clear();
    }

    // Konwertuje produkty z koszyka na linie zamówienia.
    private List<LineOfOrder> convertBasketToLineOfOrders() {
        List<LineOfOrder> lineOfOrders = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : products.entrySet()) {
            // Dla każdego produktu w koszyku tworzy nową linię zamówienia.
            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono produktu o ID: " + entry.getKey()));
            LineOfOrder lineOfOrder = new LineOfOrder(product, entry.getValue());
            lineOfOrders.add(lineOfOrder);
        }
        return lineOfOrders;
    }

    // Przygotowuje DTO zamówienia na podstawie produktów w koszyku dla danego użytkownika.
    public OrderDTO prepareOrderForCheckout(Long userId) {
        return createOrderDTOFromBasket(userId);
    }

    // Przetwarza zamówienie na podstawie danych z formularza i tworzy ostateczne DTO zamówienia.
    public String processCheckout(OrderDTO orderDTO, String differentAddress, HttpServletRequest request) {
        // Decyduje, czy użyć alternatywnego adresu dostawy na podstawie danych z formularza.
        AddressDTO shippingAddress = orderDTO.shippingAddress();
        if ("on".equals(differentAddress)) {
            // Jeśli wybrano alternatywny adres, tworzy nowy AddressDTO na podstawie danych formularza.
            shippingAddress = new AddressDTO(null, request.getParameter("street"), request.getParameter("city"),
                    request.getParameter("postalCode"), request.getParameter("country"));
        }
        // Tworzy ostateczne DTO zamówienia z możliwie zmienionym adresem dostawy.
        OrderDTO finalOrderDTO = OrderDTO.builder().id(orderDTO.id()).userId(orderDTO.userId())
                .orderStatus(orderDTO.orderStatus()).dateCreated(orderDTO.dateCreated()).sentAt(orderDTO.sentAt())
                .totalPrice(orderDTO.totalPrice()).lineOfOrders(orderDTO.lineOfOrders()).shippingAddress(shippingAddress)
                .build();

        // Składa zamówienie w systemie na podstawie ostatecznego DTO.
        placeOrder(finalOrderDTO);
        return "orderSuccess"; // Zwraca informację o sukcesie operacji.
    }

    // Przygotowuje i składa zamówienie na podstawie danych użytkownika i zamówienia.
    public String prepareAndPlaceOrder(OrderDTO orderDTO, String username, HttpServletRequest request) {
        // Wyszukuje dane użytkownika na podstawie jego nazwy (e-maila).
        UserDTO userDTO = userService.findUserByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Ekstrahuje adres dostawy z danych formularza.
        AddressDTO shippingAddress = extractShippingAddress(orderDTO, request);

        // Tworzy ostateczne DTO zamówienia na podstawie przekazanych i wyekstrahowanych danych.
        OrderDTO finalOrderDTO = OrderDTO.builder().userId(userDTO.id()).orderStatus(orderDTO.orderStatus())
                .dateCreated(LocalDate.now()).totalPrice(orderDTO.totalPrice()).lineOfOrders(orderDTO.lineOfOrders())
                .shippingAddress(shippingAddress).build();

        // Składa zamówienie w systemie i zwraca informację o sukcesie.
        placeOrder(finalOrderDTO);
        return "orderSuccess";
    }

    // Ekstrahuje alternatywny adres dostawy z danych formularza, jeśli został wybrany.
    private AddressDTO extractShippingAddress(OrderDTO orderDTO, HttpServletRequest request) {
        if ("on".equals(request.getParameter("differentAddress"))) {
            // Tworzy nowy AddressDTO na podstawie danych formularza, jeśli wybrano alternatywny adres.
            return new AddressDTO(null, request.getParameter("street"), request.getParameter("city"),
                    request.getParameter("postalCode"), request.getParameter("country"));
        }
        // W przeciwnym razie zwraca adres dostawy z DTO zamówienia.
        return orderDTO.shippingAddress();
    }

    // Kończy proces składania zamówienia i przekierowuje do strony potwierdzenia.
    public String finalizeOrderAndRedirect(Long userId, AddressDTO potentialNewShippingAddress, boolean useDifferentAddress) {
        // Wyszukuje dane użytkownika na podstawie jego ID.
        UserDTO userDTO = userService.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Decyduje, czy użyć potencjalnie nowego adresu dostawy.
        AddressDTO finalShippingAddress = useDifferentAddress ? potentialNewShippingAddress : null;

        // Przygotowuje DTO zamówienia na podstawie danych koszyka i wybranego adresu dostawy.
        OrderDTO orderDTO = prepareOrderForCheckout(userId, finalShippingAddress);

        // Składa zamówienie w systemie.
        placeOrder(orderDTO);

        // Czyści koszyk po złożeniu zamówienia.
        clear();

        // Przekierowuje do strony potwierdzenia zamówienia.
        return "order_success";
    }

    // Przygotowuje DTO zamówienia na podstawie danych koszyka i opcjonalnie zmienionego adresu dostawy.
    private OrderDTO prepareOrderForCheckout(Long userId, AddressDTO finalShippingAddress) {
        // Tworzy DTO zamówienia na podstawie produktów w koszyku.
        OrderDTO orderDTO = createOrderDTOFromBasket(userId);
        if (finalShippingAddress != null) {
            // Jeśli podano nowy adres dostawy, aktualizuje DTO zamówienia o ten adres.
            orderDTO = new OrderDTO(orderDTO.id(), orderDTO.userId(), orderDTO.orderStatus(), orderDTO.dateCreated(),
                    orderDTO.sentAt(), orderDTO.totalPrice(), orderDTO.lineOfOrders(), finalShippingAddress);
        }
        return orderDTO; // Zwraca ostateczne DTO zamówienia gotowe do złożenia.
    }}