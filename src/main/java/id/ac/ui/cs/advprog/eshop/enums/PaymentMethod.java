package id.ac.ui.cs.advprog.eshop.enums;

import java.util.Locale;

public enum PaymentMethod {
    VOUCHER_CODE("Voucher Code"),
    BANK_TRANSFER("Bank Transfer"),
    CASH_ON_DELIVERY("Cash on Delivery");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PaymentMethod from(String value) {
        if (value == null) {
            return null;
        }

        String normalized = value
                .trim()
                .toUpperCase(Locale.ROOT)
                .replace('-', '_')
                .replace(' ', '_');

        for (PaymentMethod method : values()) {
            if (method.name().equals(normalized) || method.displayName.toUpperCase(Locale.ROOT).replace(' ', '_').equals(normalized)) {
                return method;
            }
        }
        return null;
    }
}
