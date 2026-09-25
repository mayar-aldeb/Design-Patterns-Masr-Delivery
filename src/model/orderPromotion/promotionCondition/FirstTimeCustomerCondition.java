package model.orderPromotion.promotionCondition;

import exception.PromotionException;
import model.Customer;
import model.Order;
import model.OrderInvoice;

public class FirstTimeCustomerCondition implements PromotionCondition{


    @Override
    public void checkCondition(OrderInvoice invoice, Customer customer, Order order) {
        if(customer.getCompletedOrders() > 0)
            throw new PromotionException("promotion is restriction to first time customers only.");
    }
}
