package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService service;

    @Mock
    private Model model;

    @InjectMocks
    private ProductController controller;

    @Test
    void createProductPageAddsEmptyProductToModel() {
        String viewName = controller.createProductPage(model);

        verify(model).addAttribute(eq("product"), any(Product.class));
        assertEquals("createProduct", viewName);
    }

    @Test
    void createProductPostCreatesProductAndRedirectsToList() {
        Product product = new Product();

        String viewName = controller.createProductPost(product, model);

        verify(service).create(product);
        assertEquals("redirect:/product/list", viewName);
    }

    @Test
    void productListPageAddsProductsToModel() {
        List<Product> products = List.of(new Product());
        when(service.findAll()).thenReturn(products);

        String viewName = controller.productListPage(model);

        verify(model).addAttribute("products", products);
        assertEquals("productList", viewName);
    }

    @Test
    void editProductPageRedirectsWhenProductNotFound() {
        when(service.findById("missing-id")).thenReturn(null);

        String viewName = controller.editProductPage("missing-id", model);

        verifyNoInteractions(model);
        assertEquals("redirect:/product/list", viewName);
    }

    @Test
    void editProductPageAddsProductToModelWhenFound() {
        Product product = new Product();
        product.setProductId("id-1");
        when(service.findById("id-1")).thenReturn(product);

        String viewName = controller.editProductPage("id-1", model);

        verify(model).addAttribute("product", product);
        assertEquals("editProduct", viewName);
    }

    @Test
    void editProductPostUpdatesProductAndRedirectsToList() {
        Product product = new Product();

        String viewName = controller.editProductPost(product, model);

        verify(service).update(product);
        assertEquals("redirect:/product/list", viewName);
    }

    @Test
    void deleteProductPostDeletesProductAndRedirectsToList() {
        String viewName = controller.deleteProductPost("id-1", model);

        verify(service).delete("id-1");
        assertEquals("redirect:/product/list", viewName);
    }
}

