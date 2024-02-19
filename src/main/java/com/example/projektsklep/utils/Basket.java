package com.example.projektsklep.utils;


import com.example.projektsklep.model.entities.order.LineOfOrder;
import com.example.projektsklep.model.entities.product.Product;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data

@Component
@SessionScope
public class Basket {

    private List<LineOfOrder> lineOfOrders;


    public Basket() {
        this.lineOfOrders = new ArrayList<>();
    }

    public void addProduct(Product product, int quantity) {
        LineOfOrder lineOfOrder = findLineByProduct(product);
        if (lineOfOrder == null) {
            lineOfOrders.add(new LineOfOrder(product, quantity));
        } else {
            lineOfOrder.setQuantity(lineOfOrder.getQuantity() + quantity);
        }
    }

    public void removeProduct(Product product) {
        LineOfOrder lineOfOrder = findLineByProduct(product);
        if (lineOfOrder != null) {
            if (lineOfOrder.getQuantity() > 1) {
                lineOfOrder.setQuantity(lineOfOrder.getQuantity() - 1);
            } else {
                lineOfOrders.remove(lineOfOrder);
            }
        }
    }

    private LineOfOrder findLineByProduct(Product product) {
        return lineOfOrders.stream()
                .filter(line -> line.getProduct().equals(product))
                .findFirst()
                .orElse(null);
    }

    public List<LineOfOrder> getLineOfOrders() {
        return lineOfOrders;
    }


    public void clear() {
        lineOfOrders.clear();
    }


    public BigDecimal getTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (LineOfOrder lineOfOrder : lineOfOrders) {
            total = total.add(lineOfOrder.getUnitPrice().multiply(new BigDecimal(lineOfOrder.getQuantity())));
        }
        return total;
    }
}
