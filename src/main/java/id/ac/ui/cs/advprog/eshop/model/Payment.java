package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class Payment {
    private String id;
    private Order order;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public Payment(Order order, String method, String status, Map<String, String> paymentData) {
        this.id = UUID.randomUUID().toString();
        this.order = Objects.requireNonNull(order, "Order must not be null");
        this.method = Objects.requireNonNull(method, "Method must not be null");
        this.status = status;
        this.paymentData = Collections.unmodifiableMap(new HashMap<>(paymentData));
    }
}
