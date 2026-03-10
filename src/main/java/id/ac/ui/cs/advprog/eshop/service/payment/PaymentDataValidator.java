package id.ac.ui.cs.advprog.eshop.service.payment;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;

import java.util.Map;

public interface PaymentDataValidator {
    PaymentMethod supportedMethod();

    boolean isValid(Map<String, String> paymentData);
}
