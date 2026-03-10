package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import id.ac.ui.cs.advprog.eshop.service.payment.BankTransferPaymentValidator;
import id.ac.ui.cs.advprog.eshop.service.payment.PaymentDataValidator;
import id.ac.ui.cs.advprog.eshop.service.payment.VoucherCodePaymentValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    private PaymentServiceImpl paymentService;

    private Order order;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Laptop");
        product.setProductQuantity(1);
        order = new Order(List.of(product), "Natasya");

        List<PaymentDataValidator> validators = List.of(
                new VoucherCodePaymentValidator(),
                new BankTransferPaymentValidator()
        );
        paymentService = new PaymentServiceImpl(paymentRepository, validators);
    }

    @Test
    void addPaymentAcceptsValidVoucherCode() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "Voucher Code", voucherData("ESHOP1234ABC5678"));

        assertEquals("SUCCESS", payment.getStatus());
        assertEquals("SUCCESS", order.getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void addPaymentRejectsInvalidVoucherCode() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "Voucher Code", voucherData("INVALID"));

        assertEquals("REJECTED", payment.getStatus());
        assertEquals("FAILED", order.getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void addPaymentAcceptsValidBankTransferData() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "Bank Transfer", bankTransferData("BCA", "REF-123"));

        assertEquals("SUCCESS", payment.getStatus());
        assertEquals("SUCCESS", order.getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void addPaymentRejectsIncompleteBankTransferData() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "Bank Transfer", bankTransferData("", "REF-123"));

        assertEquals("REJECTED", payment.getStatus());
        assertEquals("FAILED", order.getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void setStatusToSuccessUpdatesPaymentAndOrder() {
        Payment payment = new Payment(order, "Voucher Code", "REJECTED", voucherData("ESHOP1234ABC5678"));
        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment updatedPayment = paymentService.setStatus(payment, "SUCCESS");

        assertEquals("SUCCESS", updatedPayment.getStatus());
        assertEquals("SUCCESS", order.getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void setStatusToRejectedUpdatesPaymentAndOrder() {
        Payment payment = new Payment(order, "Voucher Code", "SUCCESS", voucherData("ESHOP1234ABC5678"));
        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment updatedPayment = paymentService.setStatus(payment, "REJECTED");

        assertEquals("REJECTED", updatedPayment.getStatus());
        assertEquals("FAILED", order.getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void getPaymentDelegatesToRepository() {
        Payment payment = new Payment(order, "Voucher Code", "SUCCESS", voucherData("ESHOP1234ABC5678"));
        when(paymentRepository.findById(payment.getId())).thenReturn(payment);

        Payment foundPayment = paymentService.getPayment(payment.getId());

        assertSame(payment, foundPayment);
    }

    @Test
    void getAllPaymentsDelegatesToRepository() {
        Payment payment = new Payment(order, "Voucher Code", "SUCCESS", voucherData("ESHOP1234ABC5678"));
        when(paymentRepository.findAll()).thenReturn(List.of(payment));

        List<Payment> payments = paymentService.getAllPayments();

        assertEquals(1, payments.size());
        assertSame(payment, payments.get(0));
    }

    @Test
    void addPaymentStoresSubmittedMethodAndData() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        Map<String, String> paymentData = bankTransferData("BCA", "REF-123");

        paymentService.addPayment(order, "Bank Transfer", paymentData);

        verify(paymentRepository).save(captor.capture());
        Payment savedPayment = captor.getValue();
        assertEquals("Bank Transfer", savedPayment.getMethod());
        assertEquals(paymentData, savedPayment.getPaymentData());
    }

    private Map<String, String> voucherData(String voucherCode) {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", voucherCode);
        return paymentData;
    }

    private Map<String, String> bankTransferData(String bankName, String referenceCode) {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", bankName);
        paymentData.put("referenceCode", referenceCode);
        return paymentData;
    }
}
