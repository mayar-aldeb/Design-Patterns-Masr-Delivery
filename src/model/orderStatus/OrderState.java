package model.orderStatus;

import exception.IllegalOrderTransitionException;
import model.Order;

public abstract class OrderState {

    public void accept(Order order){
       throw new IllegalOrderTransitionException("Cannot accept order");
   }

   public  void prepare(Order order){
       throw new IllegalOrderTransitionException("Cannot prepare order");
   }

   public  void ready(Order order){
       throw new IllegalOrderTransitionException("Cannot get order ready");
   }

   public  void assign(Order order){
       throw new IllegalOrderTransitionException("Cannot assign order");
   }

   public  void outForDelivery(Order order){
       throw new IllegalOrderTransitionException("Cannot get order out for delivery");
   }

   public  void deliver(Order order){
       throw new IllegalOrderTransitionException("Cannot deliver order");
   }

   public  void cancel(Order order){
       throw new IllegalOrderTransitionException("Cannot cancel order");
   }


}
