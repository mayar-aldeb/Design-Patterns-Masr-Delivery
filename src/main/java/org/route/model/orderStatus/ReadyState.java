package org.route.model.orderStatus;

import org.route.enums.OrderStatus;
import org.route.exception.IllegalOrderTransitionException;
import org.route.model.Order;

public class ReadyState extends OrderState{

    @Override
    public void assign(Order order) {
        order.setOrderStatus(OrderStatus.ASSIGNED);
        order.setState(new AssignedState());
    }

    @Override
    public void cancel(Order order){
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setState(new CancelState());
    }

    public  void ready(Order order){
        throw new IllegalOrderTransitionException("order is already ready");
    }

}
