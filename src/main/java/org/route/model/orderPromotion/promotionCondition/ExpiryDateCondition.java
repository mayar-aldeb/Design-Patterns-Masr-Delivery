package org.route.model.orderPromotion.promotionCondition;

import org.route.exception.PromotionException;
import org.route.model.Customer;
import org.route.model.Order;
import org.route.model.OrderInvoice;


public class ExpiryDateCondition implements PromotionCondition{
    private final int expirationDate;

    public ExpiryDateCondition(int expirationDate) {
        this.expirationDate = expirationDate;
    }


    @Override
    public void checkCondition(OrderInvoice invoice, Customer customer, Order order) {
        if(invoice.getOrderDate().getMonthValue()==expirationDate)
            throw new PromotionException("promotion is expired");
    }
}
