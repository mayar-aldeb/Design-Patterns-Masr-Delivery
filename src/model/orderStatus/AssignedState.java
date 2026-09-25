package model.orderStatus;

import enums.OrderStatus;
import exception.IllegalOrderTransitionException;
import model.Order;

public class AssignedState extends OrderState{


    @Override
    public void outForDelivery(Order order) {
        order.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
        order.setState(new OutForDeliveryState());

    }

    @Override
    public void cancel(Order order){
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setState(new CancelState());

    }

    @Override
    public void assign(Order order){
        throw new IllegalOrderTransitionException("order already assigned");

    }
}
