package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Laptop");
        product.setProductQuantity(1);
        order = new Order(List.of(product), "Natasya", OrderStatus.WAITING_PAYMENT.name());
    }

    @Test
    void createOrderAddsNewOrder() {
        when(orderRepository.findById(order.getId())).thenReturn(null);
        when(orderRepository.save(order)).thenReturn(order);

        Order createdOrder = orderService.createOrder(order);

        assertSame(order, createdOrder);
        verify(orderRepository).save(order);
    }

    @Test
    void createOrderRejectsExistingOrder() {
        when(orderRepository.findById(order.getId())).thenReturn(order);

        Order createdOrder = orderService.createOrder(order);

        assertSame(order, createdOrder);
        verify(orderRepository, never()).save(order);
    }

    @Test
    void updateStatusUpdatesExistingOrder() {
        when(orderRepository.findById(order.getId())).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);

        Order updatedOrder = orderService.updateStatus(order.getId(), OrderStatus.SUCCESS.name());

        assertEquals(OrderStatus.SUCCESS.name(), updatedOrder.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void updateStatusWithInvalidStatusKeepsPreviousValue() {
        when(orderRepository.findById(order.getId())).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);

        Order updatedOrder = orderService.updateStatus(order.getId(), "PAID");

        assertEquals(OrderStatus.WAITING_PAYMENT.name(), updatedOrder.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void updateStatusThrowsExceptionForUnknownOrderId() {
        when(orderRepository.findById("missing-id")).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> orderService.updateStatus("missing-id", OrderStatus.SUCCESS.name()));
        verify(orderRepository, never()).save(order);
    }

    @Test
    void findByIdReturnsOrderForExistingId() {
        when(orderRepository.findById(order.getId())).thenReturn(order);

        Order foundOrder = orderService.findById(order.getId());

        assertSame(order, foundOrder);
    }

    @Test
    void findByIdReturnsNullForUnknownId() {
        when(orderRepository.findById("missing-id")).thenReturn(null);

        assertNull(orderService.findById("missing-id"));
    }

    @Test
    void findAllByAuthorReturnsMatchingOrders() {
        when(orderRepository.findAllByAuthor("Natasya")).thenReturn(List.of(order));

        List<Order> orders = orderService.findAllByAuthor("Natasya");

        assertEquals(1, orders.size());
        assertSame(order, orders.get(0));
    }

    @Test
    void findAllByAuthorReturnsEmptyListForCaseMismatch() {
        when(orderRepository.findAllByAuthor("natasya")).thenReturn(List.of());

        assertEquals(List.of(), orderService.findAllByAuthor("natasya"));
    }
}
