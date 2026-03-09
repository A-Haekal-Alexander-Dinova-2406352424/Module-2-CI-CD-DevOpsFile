package id.ac.ui.cs.advprog.eshop.service.payment;

import java.util.Map;

public class BankTransferPaymentValidator implements PaymentDataValidator {
    @Override
    public boolean isValid(Map<String, String> paymentData) {
        return hasValue(paymentData.get("bankName")) && hasValue(paymentData.get("referenceCode"));
    }

    private boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }
}
