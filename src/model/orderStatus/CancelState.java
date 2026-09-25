package model.orderStatus;

import exception.IllegalOrderTransitionException;
import model.Order;

public class CancelState extends OrderState{


    @Override
    public  void cancel(Order order){
        throw new IllegalOrderTransitionException("order already cancelled");
    }

}
