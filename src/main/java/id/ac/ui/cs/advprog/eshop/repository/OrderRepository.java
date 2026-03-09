package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository {
    private final List<Order> orderData = new ArrayList<>();

    public Order save(Order order) {
        Order existingOrder = findById(order.getId());
        if (existingOrder != null) {
            int index = orderData.indexOf(existingOrder);
            orderData.set(index, order);
            return order;
        }

        orderData.add(order);
        return order;
    }

    public Order findById(String id) {
        for (Order order : orderData) {
            if (order.getId().equals(id)) {
                return order;
            }
        }
        return null;
    }

    public List<Order> findAllByAuthor(String author) {
        List<Order> orders = new ArrayList<>();
        for (Order order : orderData) {
            if (order.getAuthor().equals(author)) {
                orders.add(order);
            }
        }
        return orders;
    }
}
