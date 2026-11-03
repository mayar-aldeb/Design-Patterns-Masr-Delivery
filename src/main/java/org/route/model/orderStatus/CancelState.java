package org.route.model.orderStatus;

import org.route.exception.IllegalOrderTransitionException;
import org.route.model.Order;

public class CancelState extends OrderState{


    @Override
    public  void cancel(Order order){
        throw new IllegalOrderTransitionException("order already cancelled");
    }

}
