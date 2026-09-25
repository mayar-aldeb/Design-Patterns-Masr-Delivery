package model.orderStatus;

import enums.OrderStatus;
import exception.IllegalOrderTransitionException;
import model.Order;

public class OutForDeliveryState extends OrderState{


    @Override
    public void deliver(Order order) {
        order.setOrderStatus(OrderStatus.DELIVERED);
        order.setState(new DeliveredState());

    }

    @Override
    public void outForDelivery(Order order){
        throw new IllegalOrderTransitionException("order already out for delivery");
    }

}
