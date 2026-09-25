package model.orderStatus;

import enums.OrderStatus;
import model.Order;

public class PlacedState extends OrderState{

    @Override
    public void accept(Order order) {
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setState(new AcceptState());
    }

    @Override
    public void cancel(Order order){
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setState(new CancelState());
    }


}
