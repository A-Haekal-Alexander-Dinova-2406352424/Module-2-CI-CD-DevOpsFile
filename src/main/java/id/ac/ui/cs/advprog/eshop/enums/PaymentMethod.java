package id.ac.ui.cs.advprog.eshop.enums;

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
                .toUpperCase()
                .replace('-', '_')
                .replace(' ', '_');

        for (PaymentMethod method : values()) {
            if (method.name().equals(normalized) || method.displayName.toUpperCase().replace(' ', '_').equals(normalized)) {
                return method;
            }
        }
        return null;
    }
}
