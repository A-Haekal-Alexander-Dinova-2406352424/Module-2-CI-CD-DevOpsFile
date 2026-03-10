package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    private List<Product> products;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Laptop");
        product.setProductQuantity(1);
        products = List.of(product);
    }

    @Test
    void createOrderWithEmptyProductsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Order(List.of(), "Natasya"));
    }

    @Test
    void createOrderWithoutStatusUsesWaitingPayment() {
        Order order = new Order(products, "Natasya");

        assertNotNull(order.getId());
        assertNotNull(order.getOrderTime());
        assertEquals(products, order.getProducts());
        assertEquals("Natasya", order.getAuthor());
        assertEquals(OrderStatus.WAITING_PAYMENT.name(), order.getStatus());
    }

    @Test
    void createOrderWithValidStatusKeepsProvidedStatus() {
        Order order = new Order(products, "Natasya", OrderStatus.SUCCESS.name());

        assertEquals(OrderStatus.SUCCESS.name(), order.getStatus());
    }

    @Test
    void createOrderWithInvalidStatusFallsBackToWaitingPayment() {
        Order order = new Order(products, "Natasya", "PAID");

        assertEquals(OrderStatus.WAITING_PAYMENT.name(), order.getStatus());
    }

    @Test
    void setStatusWithValidValueUpdatesOrderStatus() {
        Order order = new Order(products, "Natasya");

        order.setStatus(OrderStatus.CANCELLED.name());

        assertEquals(OrderStatus.CANCELLED.name(), order.getStatus());
    }

    @Test
    void setStatusWithInvalidValueKeepsPreviousOrderStatus() {
        Order order = new Order(products, "Natasya");

        order.setStatus("PAID");

        assertEquals(OrderStatus.WAITING_PAYMENT.name(), order.getStatus());
    }
}
