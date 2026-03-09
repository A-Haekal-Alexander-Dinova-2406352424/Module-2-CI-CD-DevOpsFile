package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentTest {

    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Laptop");
        product.setProductQuantity(1);

        order = new Order(List.of(product), "Natasya");
        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void createPaymentStoresAllFields() {
        Payment payment = new Payment(order, "Voucher Code", "SUCCESS", paymentData);

        assertNotNull(payment.getId());
        assertEquals(order, payment.getOrder());
        assertEquals("Voucher Code", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(paymentData, payment.getPaymentData());
    }

    @Test
    void setStatusUpdatesPaymentStatus() {
        Payment payment = new Payment(order, "Voucher Code", "REJECTED", paymentData);

        payment.setStatus("SUCCESS");

        assertEquals("SUCCESS", payment.getStatus());
    }
}
