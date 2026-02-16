package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Repository
public class ProductRepository {
    private final List<Product> productData = new ArrayList<>();

    public Product create(Product product) {
        productData.add(product);
        return product;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public Product findById(String id) {
        for (Product product : productData) {
            if (Objects.equals(product.getProductId(), id)) {
                return product;
            }
        }
        return null;
    }

    public Product update(Product updatedProduct) {
        if (updatedProduct == null || updatedProduct.getProductId() == null) {
            return null;
        }

        Product product = findById(updatedProduct.getProductId());
        if (product == null) {
            return null;
        }

        product.setProductName(updatedProduct.getProductName());
        product.setProductQuantity(updatedProduct.getProductQuantity());
        return product;
    }

    public boolean delete(String id) {
        Iterator<Product> iterator = productData.iterator();
        while (iterator.hasNext()) {
            if (Objects.equals(iterator.next().getProductId(), id)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
}

