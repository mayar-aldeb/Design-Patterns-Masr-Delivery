package org.route.repository;

import org.route.model.Order;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

@Repository
public class ReadyOrdersRepository {
    private final Queue<Order> readyOrders=new PriorityQueue<>(Comparator.comparing(Order::getOrderPriority).
            thenComparing(Order::getPlacedAt));

    public Queue<Order> findReadyOrders() {
        return readyOrders;
    }

    public Order peekReadyOrder() {
        return readyOrders.peek();
    }
    public void removeReadyOrder() {
        readyOrders.poll();
    }


    public void addOrder(Order order) {
        readyOrders.add(order);
    }


}
