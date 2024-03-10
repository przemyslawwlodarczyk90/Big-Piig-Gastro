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

public class BasketService {

    public static final String productNotFound = "Nie znaleziono produktu o ID: ";
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductRepository productRepository;

    private final Map<Long, Integer> products = new HashMap<>();
    private Basket basket;


    public BasketService(OrderRepository orderRepository, UserService userService, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.productRepository = productRepository;
        this.basket = new Basket();
    }

    public void takeOneItemFromQuantity(Long productId) {
        products.computeIfPresent(productId, (k, v) -> v > 1 ? v - 1 : null);
    }

    public void clear() {
        products.clear();
    }


    public Basket getCurrentBasket() {
        if (this.basket == null) {
            this.basket = new Basket();
        }
        return this.basket;
    }

    public void updateProductQuantity(Long productId, int quantity) {
        if (products.containsKey(productId)) {
            if (quantity > 0) {
                products.put(productId, quantity);
            } else {
                takeOneItemFromQuantity(productId);
            }
        }
    }


    public OrderDTO createOrderDTOFromBasket(Long userId) {

        List<LineOfOrderDTO> lineOfOrdersDTO = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : products.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();


            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException(productNotFound + productId));
            BigDecimal linePrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            totalPrice = totalPrice.add(linePrice);
            LineOfOrderDTO lineOfOrderDTO = new LineOfOrderDTO(null, productId, quantity, product.getPrice());
            lineOfOrdersDTO.add(lineOfOrderDTO);
        }

        AddressDTO shippingAddress = new AddressDTO(null, "", "", "", "");

        return new OrderDTO(null, userId, "NEW", LocalDate.now(), null, totalPrice, lineOfOrdersDTO, shippingAddress);
    }

    public void addProductToBasket(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(productNotFound+ productId));
        basket.addProduct(product, quantity);
    }


    public void placeOrder(OrderDTO orderDTO) {
        Order newOrder = new Order();
        newOrder.setOrderStatus(OrderStatus.NEW_ORDER);
        newOrder.setDateCreated(LocalDate.now());

        UserDTO userDTO = userService.findUserById(orderDTO.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        User user = userService.convertToUser(userDTO);
        newOrder.setAccountHolder(user);

        List<LineOfOrder> lineOfOrders = convertBasketToLineOfOrders();
        newOrder.setLineOfOrders(lineOfOrders);

        newOrder.calculateTotalPrice();

        orderRepository.save(newOrder);

        clear();
    }

    private List<LineOfOrder> convertBasketToLineOfOrders() {
        List<LineOfOrder> lineOfOrders = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : products.entrySet()) {
            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException(productNotFound + entry.getKey()));
            LineOfOrder lineOfOrder = new LineOfOrder(product, entry.getValue());
            lineOfOrders.add(lineOfOrder);
        }
        return lineOfOrders;
    }

    public OrderDTO prepareOrderForCheckout(Long userId) {
        return createOrderDTOFromBasket(userId);
    }







    // Kończy proces składania zamówienia i przekierowuje do strony potwierdzenia.
    public String finalizeOrderAndRedirect(Long userId, AddressDTO potentialNewShippingAddress, boolean useDifferentAddress) {



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