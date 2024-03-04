package com.example.projektsklep.utils;


import com.example.projektsklep.model.entities.order.LineOfOrder;
import com.example.projektsklep.model.entities.product.Product;
import lombok.Data;
import org.springframework.web.context.annotation.SessionScope;


import java.util.ArrayList;
import java.util.List;

@Data
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

    public void clear() {
        lineOfOrders.clear();
    }


}