package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class OrderRepositoryTest {

    private OrderRepository orderRepository;
    private Order order;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository();
        order = new Order(createProducts("product-1"), "Natasya", OrderStatus.WAITING_PAYMENT.name());
    }

    @Test
    void saveAddsNewOrder() {
        Order savedOrder = orderRepository.save(order);

        assertSame(order, savedOrder);
        assertSame(order, orderRepository.findById(order.getId()));
    }

    @Test
    void saveUpdatesExistingOrder() {
        orderRepository.save(order);
        order.setStatus(OrderStatus.SUCCESS.name());

        Order savedOrder = orderRepository.save(order);

        assertSame(order, savedOrder);
        assertEquals(OrderStatus.SUCCESS.name(), orderRepository.findById(order.getId()).getStatus());
    }

    @Test
    void findByIdReturnsOrderForExistingId() {
        orderRepository.save(order);

        Order foundOrder = orderRepository.findById(order.getId());

        assertNotNull(foundOrder);
        assertSame(order, foundOrder);
    }

    @Test
    void findByIdReturnsNullForUnknownId() {
        assertNull(orderRepository.findById("missing-id"));
    }

    @Test
    void findAllByAuthorReturnsMatchingOrders() {
        Order secondOrder = new Order(createProducts("product-2"), "Natasya", OrderStatus.SUCCESS.name());
        Order thirdOrder = new Order(createProducts("product-3"), "Other", OrderStatus.CANCELLED.name());
        orderRepository.save(order);
        orderRepository.save(secondOrder);
        orderRepository.save(thirdOrder);

        List<Order> orders = orderRepository.findAllByAuthor("Natasya");

        assertEquals(2, orders.size());
        assertSame(order, orders.get(0));
        assertSame(secondOrder, orders.get(1));
    }

    @Test
    void findAllByAuthorIsCaseSensitive() {
        orderRepository.save(order);

        List<Order> orders = orderRepository.findAllByAuthor("natasya");

        assertFalse(orders.iterator().hasNext());
    }

    private List<Product> createProducts(String productId) {
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName("Product " + productId);
        product.setProductQuantity(1);
        return List.of(product);
    }
}
