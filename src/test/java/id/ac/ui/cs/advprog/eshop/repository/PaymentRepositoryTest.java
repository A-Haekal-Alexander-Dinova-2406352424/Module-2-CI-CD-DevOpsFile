package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class PaymentRepositoryTest {

    private PaymentRepository paymentRepository;
    private Payment payment;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
        payment = new Payment(createOrder(), "Voucher Code", "SUCCESS", createPaymentData("ESHOP1234ABC5678"));
    }

    @Test
    void saveAddsNewPayment() {
        Payment savedPayment = paymentRepository.save(payment);

        assertSame(payment, savedPayment);
        assertSame(payment, paymentRepository.findById(payment.getId()));
    }

    @Test
    void saveUpdatesExistingPayment() {
        paymentRepository.save(payment);
        payment.setStatus("REJECTED");

        Payment savedPayment = paymentRepository.save(payment);

        assertSame(payment, savedPayment);
        assertEquals("REJECTED", paymentRepository.findById(payment.getId()).getStatus());
    }

    @Test
    void findByIdReturnsPaymentForExistingId() {
        paymentRepository.save(payment);

        Payment foundPayment = paymentRepository.findById(payment.getId());

        assertNotNull(foundPayment);
        assertSame(payment, foundPayment);
    }

    @Test
    void findByIdReturnsNullForUnknownId() {
        assertNull(paymentRepository.findById("missing-id"));
    }

    @Test
    void findAllReturnsAllSavedPayments() {
        Payment secondPayment = new Payment(createOrder(), "Bank Transfer", "SUCCESS", createPaymentData("reference-123"));
        paymentRepository.save(payment);
        paymentRepository.save(secondPayment);

        List<Payment> payments = paymentRepository.findAll();

        assertEquals(2, payments.size());
        assertSame(payment, payments.get(0));
        assertSame(secondPayment, payments.get(1));
    }

    @Test
    void findAllReturnsEmptyListWhenNoPaymentsExist() {
        assertFalse(paymentRepository.findAll().iterator().hasNext());
    }

    private Order createOrder() {
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Laptop");
        product.setProductQuantity(1);
        return new Order(List.of(product), "Natasya");
    }

    private Map<String, String> createPaymentData(String value) {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("reference", value);
        return paymentData;
    }
}
