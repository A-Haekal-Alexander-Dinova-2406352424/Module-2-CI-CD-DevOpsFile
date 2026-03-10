package id.ac.ui.cs.advprog.eshop.enums;

public enum PaymentStatus {
    SUCCESS,
    REJECTED;

    public static boolean contains(String value) {
        if (value == null) {
            return false;
        }

        for (PaymentStatus status : values()) {
            if (status.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
