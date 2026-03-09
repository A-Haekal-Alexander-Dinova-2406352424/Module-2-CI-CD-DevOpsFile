package id.ac.ui.cs.advprog.eshop.service.payment;

import java.util.Map;

public class VoucherCodePaymentValidator implements PaymentDataValidator {
    @Override
    public boolean isValid(Map<String, String> paymentData) {
        String voucherCode = paymentData.get("voucherCode");
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
}
