package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void createGeneratesIdWhenMissing() {
        Product product = new Product();
        product.setProductId(null);
        product.setProductName("Sample Product");
        product.setProductQuantity(10);

        Product createdProduct = productService.create(product);

        assertNotNull(createdProduct.getProductId());
        assertFalse(createdProduct.getProductId().isBlank());
        assertDoesNotThrow(() -> UUID.fromString(createdProduct.getProductId()));
        verify(productRepository).create(product);
    }

    @Test
    void createKeepsExistingId() {
        Product product = new Product();
        product.setProductId("fixed-id");
        product.setProductName("Sample Product");
        product.setProductQuantity(10);

        Product createdProduct = productService.create(product);

        assertEquals("fixed-id", createdProduct.getProductId());
        verify(productRepository).create(product);
    }

    @Test
    void createGeneratesIdWhenBlank() {
        Product product = new Product();
        product.setProductId(" ");
        product.setProductName("Sample Product");
        product.setProductQuantity(10);

        Product createdProduct = productService.create(product);

        assertNotNull(createdProduct.getProductId());
        assertFalse(createdProduct.getProductId().isBlank());
        assertDoesNotThrow(() -> UUID.fromString(createdProduct.getProductId()));
        verify(productRepository).create(product);
    }

    @Test
    void findAllCollectsAllProducts() {
        Product product1 = new Product();
        product1.setProductId("id-1");
        Product product2 = new Product();
        product2.setProductId("id-2");
        when(productRepository.findAll()).thenReturn(List.of(product1, product2).iterator());

        List<Product> products = productService.findAll();

        assertEquals(2, products.size());
        assertSame(product1, products.get(0));
        assertSame(product2, products.get(1));
    }

    @Test
    void findByIdDelegatesToRepository() {
        Product product = new Product();
        product.setProductId("id-1");
        when(productRepository.findById("id-1")).thenReturn(product);

        Product foundProduct = productService.findById("id-1");

        assertSame(product, foundProduct);
    }

    @Test
    void updateDelegatesToRepository() {
        Product product = new Product();
        product.setProductId("id-1");
        when(productRepository.update(product)).thenReturn(product);

        Product updatedProduct = productService.update(product);

        assertSame(product, updatedProduct);
    }

    @Test
    void deleteDelegatesToRepository() {
        when(productRepository.delete("id-1")).thenReturn(true);

        assertTrue(productService.delete("id-1"));
    }
}

