package id.ac.ui.cs.advprog.eshop.model;

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
        assertEquals("WAITING_PAYMENT", order.getStatus());
    }

    @Test
    void createOrderWithValidStatusKeepsProvidedStatus() {
        Order order = new Order(products, "Natasya", "SUCCESS");

        assertEquals("SUCCESS", order.getStatus());
    }

    @Test
    void createOrderWithInvalidStatusFallsBackToWaitingPayment() {
        Order order = new Order(products, "Natasya", "PAID");

        assertEquals("WAITING_PAYMENT", order.getStatus());
    }

    @Test
    void setStatusWithValidValueUpdatesOrderStatus() {
        Order order = new Order(products, "Natasya");

        order.setStatus("CANCELLED");

        assertEquals("CANCELLED", order.getStatus());
    }

    @Test
    void setStatusWithInvalidValueKeepsPreviousOrderStatus() {
        Order order = new Order(products, "Natasya");

        order.setStatus("PAID");

        assertEquals("WAITING_PAYMENT", order.getStatus());
    }
}
