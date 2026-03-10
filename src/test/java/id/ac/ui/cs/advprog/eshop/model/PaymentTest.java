package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
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
        Payment payment = new Payment(order, PaymentMethod.VOUCHER_CODE.getDisplayName(), PaymentStatus.SUCCESS.name(), paymentData);

        assertNotNull(payment.getId());
        assertEquals(order, payment.getOrder());
        assertEquals(PaymentMethod.VOUCHER_CODE.getDisplayName(), payment.getMethod());
        assertEquals(PaymentStatus.SUCCESS.name(), payment.getStatus());
        assertEquals(paymentData, payment.getPaymentData());
    }

    @Test
    void setStatusUpdatesPaymentStatus() {
        Payment payment = new Payment(order, PaymentMethod.VOUCHER_CODE.getDisplayName(), PaymentStatus.REJECTED.name(), paymentData);

        payment.setStatus(PaymentStatus.SUCCESS.name());

        assertEquals(PaymentStatus.SUCCESS.name(), payment.getStatus());
    }
}
