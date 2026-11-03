package org.route.model.orderPromotion.promotionCondition;

import org.route.exception.PromotionException;
import org.route.model.Customer;
import org.route.model.Order;
import org.route.model.OrderInvoice;

public class FirstTimeCustomerCondition implements PromotionCondition{


    @Override
    public void checkCondition(OrderInvoice invoice, Customer customer, Order order) {
        if(customer.getCompletedOrders() > 0)
            throw new PromotionException("promotion is restriction to first time customers only.");
    }
}
