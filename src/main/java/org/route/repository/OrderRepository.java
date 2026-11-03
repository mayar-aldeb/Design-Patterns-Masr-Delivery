package org.route.repository;

import org.route.model.Order;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class OrderRepository {
   private final Map<Integer, Order> orders=new HashMap<>();

   public List<Order> findAll() {
       return orders.values().stream().toList();
   }

   public Optional<Order> findById(Integer id) {
       return Optional.ofNullable(orders.get(id));
   }

   public void save(Order order) {
       orders.put(order.getId(), order);
   }
}
