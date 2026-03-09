package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Order {
    private String id;
    private List<Product> products;
    private Long orderTime;
    private String author;
    private String status;

    public Order(List<Product> products, String author) {
        this.products = products;
        this.author = author;
    }

    public Order(List<Product> products, String author, String status) {
        this.products = products;
        this.author = author;
    }
}
