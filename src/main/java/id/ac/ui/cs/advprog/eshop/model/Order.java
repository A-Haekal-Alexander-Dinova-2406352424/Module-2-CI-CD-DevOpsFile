package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Order {
    private String id;
    private List<Product> products;
    private Long orderTime;
    private String author;
    private String status;

    public Order(List<Product> products, String author) {
        this(products, author, "WAITING_PAYMENT", false);
    }

    public Order(List<Product> products, String author, String status) {
        this(products, author, status, true);
    }

    public void setStatus(String status) {
        if (OrderStatus.contains(status)) {
            this.status = status;
        }
    }

    private Order(List<Product> products, String author, String status, boolean useProvidedStatus) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Products must not be empty");
        }

        this.id = UUID.randomUUID().toString();
        this.products = List.copyOf(products);
        this.orderTime = System.currentTimeMillis();
        this.author = Objects.requireNonNull(author, "Author must not be null");
        this.status = OrderStatus.WAITING_PAYMENT.name();

        if (useProvidedStatus) {
            setStatus(status);
        }
    }
}
