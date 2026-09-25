package model.orderStatus;

import exception.IllegalOrderTransitionException;
import model.Order;

public class DeliveredState extends OrderState {


    @Override
    public  void deliver(Order order){
        throw new IllegalOrderTransitionException("order already delivered");
    }

}
