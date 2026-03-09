package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Map<String, String> safePaymentData = paymentData == null ? new HashMap<>() : new HashMap<>(paymentData);
        String paymentStatus = determineStatus(method, safePaymentData);
        Payment payment = new Payment(order, method, paymentStatus, safePaymentData);
        syncOrderStatus(order, paymentStatus);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        syncOrderStatus(payment.getOrder(), status);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    private String determineStatus(String method, Map<String, String> paymentData) {
        if ("Voucher Code".equalsIgnoreCase(method)) {
            return isValidVoucherCode(paymentData.get("voucherCode")) ? "SUCCESS" : "REJECTED";
        }

        if ("Bank Transfer".equalsIgnoreCase(method)) {
            return hasValue(paymentData.get("bankName")) && hasValue(paymentData.get("referenceCode"))
                    ? "SUCCESS"
                    : "REJECTED";
        }

        return "REJECTED";
    }

    private boolean isValidVoucherCode(String voucherCode) {
        if (voucherCode == null || voucherCode.length() != 16 || !voucherCode.startsWith("ESHOP")) {
            return false;
        }

        int numericCharacters = 0;
        for (char character : voucherCode.toCharArray()) {
            if (Character.isDigit(character)) {
                numericCharacters++;
            }
        }
        return numericCharacters == 8;
    }

    private boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }

    private void syncOrderStatus(Order order, String paymentStatus) {
        if ("SUCCESS".equals(paymentStatus)) {
            order.setStatus("SUCCESS");
        } else if ("REJECTED".equals(paymentStatus)) {
            order.setStatus("FAILED");
        }
    }
}
