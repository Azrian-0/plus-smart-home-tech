package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.repository.PaymentRepository;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.exception.NoPaymentFoundException;
import ru.yandex.practicum.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.order.OrderClient;
import ru.yandex.practicum.shoppingstore.ShoppingStoreClient;
import ru.yandex.practicum.types.PaymentStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderClient orderClient;
    private final ShoppingStoreClient storeClient;

    @Override
    public PaymentDto addPayment(OrderDto orderDto) {
        checkOrder(orderDto);
        Payment payment = Payment.builder()
                .orderId(orderDto.getOrderId())
                .totalPayment(orderDto.getTotalPrice())
                .deliveryTotal(orderDto.getDeliveryPrice())
                .feeTotal(orderDto.getTotalPrice() * 0.1)
                .status(PaymentStatus.PENDING)
                .build();
        return paymentMapper.toPaymentDto(paymentRepository.save(payment));
    }

    @Transactional(readOnly = true)
    @Override
    public double getTotalCost(OrderDto orderDto) {
        checkOrder(orderDto);
        return orderDto.getProductPrice() + (orderDto.getProductPrice() * 0.1) + orderDto.getDeliveryPrice();
    }

    @Override
    public void refundPayment(String paymentId) {
        Payment payment = getPayment(paymentId);
        orderClient.paymentOrder(payment.getOrderId());
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    @Override
    public double getProductsCost(OrderDto orderDto) {
        Map<String, Long> products = orderDto.getProducts();
        if (products == null)
            throw new NotEnoughInfoInOrderToCalculateException("Нет товаров в заказе");

        List<String> productIds = new ArrayList<>(products.keySet());

        List<ProductDto> productDtos = storeClient.getProductsInfo(productIds);

        Map<String, ProductDto> productInfoMap = productDtos.stream()
                .collect(Collectors.toMap(ProductDto::getProductId, productDto -> productDto));

        return products.entrySet().stream()
                .mapToDouble(product -> {
                    ProductDto productDto = productInfoMap.get(product.getKey());
                    if (productDto == null) {
                        throw new ProductNotFoundException("Продукт с ID " + product.getKey() + " не найден");
                    }
                    return productDto.getPrice() * product.getValue();
                })
                .sum();
    }

    @Override
    public void failedPayment(String paymentId) {
        Payment payment = getPayment(paymentId);
        orderClient.paymentOrderFailed(payment.getOrderId());
        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);

    }

    private void checkOrder(OrderDto orderDto) {
        if (orderDto.getDeliveryPrice() == null)
            throw new NotEnoughInfoInOrderToCalculateException("Нет суммы за доставку");
        if (orderDto.getTotalPrice() == null)
            throw new NotEnoughInfoInOrderToCalculateException("Нет общей суммы по заказу");
        if (orderDto.getProductPrice() == null)
            throw new NotEnoughInfoInOrderToCalculateException("Нет суммы по товарам");
    }

    private Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoPaymentFoundException("Платежа с id = " + paymentId + " не существует"));
    }
}
