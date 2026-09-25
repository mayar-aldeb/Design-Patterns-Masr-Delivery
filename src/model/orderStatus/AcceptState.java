package model.orderStatus;

import enums.OrderStatus;
import exception.IllegalOrderTransitionException;
import model.Order;

public class AcceptState extends OrderState{



    @Override
    public void prepare(Order order) {
        order.setOrderStatus(OrderStatus.PREPARING);
        order.setState(new PreparingState());
    }

    @Override
    public void cancel(Order order){
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setState(new CancelState());
    }

    @Override
    public void accept(Order order) {
        throw new IllegalOrderTransitionException("order already accepted");
    }
}
