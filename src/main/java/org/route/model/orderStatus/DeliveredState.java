package org.route.model.orderStatus;

import org.route.exception.IllegalOrderTransitionException;
import org.route.model.Order;

public class DeliveredState extends OrderState {


    @Override
    public  void deliver(Order order){
        throw new IllegalOrderTransitionException("order already delivered");
    }

}
