package org.route.model.orderStatus;

import org.route.enums.OrderStatus;
import org.route.exception.IllegalOrderTransitionException;
import org.route.model.Order;

public class PreparingState extends OrderState{

    @Override
    public void ready(Order order){
        order.setOrderStatus(OrderStatus.READY);
        order.setState(new ReadyState());
    }

    @Override
    public void cancel(Order order){
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setState(new CancelState());
    }

    @Override
    public  void prepare(Order order){
        throw new IllegalOrderTransitionException("order already preparing");
    }


}
