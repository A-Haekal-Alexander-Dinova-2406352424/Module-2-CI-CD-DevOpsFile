package id.ac.ui.cs.advprog.eshop.service.payment;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BankTransferPaymentValidator implements PaymentDataValidator {
    @Override
    public PaymentMethod supportedMethod() {
        return PaymentMethod.BANK_TRANSFER;
    }

    @Override
    public boolean isValid(Map<String, String> paymentData) {
        return hasValue(paymentData.get("bankName")) && hasValue(paymentData.get("referenceCode"));
    }

    private boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }
}
