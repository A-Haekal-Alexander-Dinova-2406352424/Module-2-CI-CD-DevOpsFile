package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import id.ac.ui.cs.advprog.eshop.service.payment.PaymentDataValidator;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final Map<PaymentMethod, PaymentDataValidator> validators;
    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, List<PaymentDataValidator> validatorList) {
        this.paymentRepository = paymentRepository;
        this.validators = buildValidatorMap(validatorList);
    }

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Map<String, String> safePaymentData = paymentData == null ? new HashMap<>() : new HashMap<>(paymentData);
        String paymentStatus = determineStatus(method, safePaymentData);
        Payment payment = new Payment(order, method, paymentStatus, safePaymentData);
        syncOrderStatus(order, paymentStatus);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        syncOrderStatus(payment.getOrder(), status);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    private String determineStatus(String method, Map<String, String> paymentData) {
        PaymentMethod paymentMethod = PaymentMethod.from(method);
        PaymentDataValidator validator = validators.get(paymentMethod);
        if (validator == null) {
            return PaymentStatus.REJECTED.name();
        }
        return validator.isValid(paymentData)
                ? PaymentStatus.SUCCESS.name()
                : PaymentStatus.REJECTED.name();
    }

    private void syncOrderStatus(Order order, String paymentStatus) {
        if (PaymentStatus.SUCCESS.name().equals(paymentStatus)) {
            order.setStatus(PaymentStatus.SUCCESS.name());
        } else if (PaymentStatus.REJECTED.name().equals(paymentStatus)) {
            order.setStatus(OrderStatus.FAILED.name());
        }
    }

    private Map<PaymentMethod, PaymentDataValidator> buildValidatorMap(List<PaymentDataValidator> validatorList) {
        Map<PaymentMethod, PaymentDataValidator> validatorMap = new EnumMap<>(PaymentMethod.class);
        for (PaymentDataValidator validator : validatorList) {
            validatorMap.put(validator.supportedMethod(), validator);
        }
        return Map.copyOf(validatorMap);
    }
}
