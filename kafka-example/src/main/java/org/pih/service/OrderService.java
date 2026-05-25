package org.pih.service;

import org.pih.entity.Order;
import org.pih.entity.OutboxEvent;
import org.pih.repository.OrderRepository;
import org.pih.repository.OutboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OutboxRepository outboxRepository;

    @Transactional
    public Order createOrder(Double amount) {

        // Step 1: Save Order
        Order order = new Order();
        order.setAmount(amount);
        order.setStatus("CREATED");

        order = orderRepository.save(order);

        // Step 2: Create Outbox Event
        String payload = "{ \"orderId\": " + order.getId() + ", \"amount\": " + amount + " }";

        OutboxEvent event = new OutboxEvent();
        event.setEventType("ORDER_CREATED");
        event.setPayload(payload);
        event.setStatus("NEW");

        outboxRepository.save(event);

        return order;
    }
}