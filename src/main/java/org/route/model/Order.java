package org.route.model;

import org.route.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.route.model.Items.MenuItem;
import org.route.model.Items.OrderItem;
import org.route.model.orderStatus.OrderState;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Getter
@Setter
public class Order {
    private final int id;
    private int customerId;
    private int restaurantId;
    private Address deliveryAddress;
    private Map<MenuItem,OrderItem> items;
    private LocalDateTime placedAt ;
    private OrderStatus orderStatus;
    private BigDecimal totalPrice;
    private OrderState state;
    private int orderPriority;


}
