package model.orderPromotion.promotionCondition;

import exception.PromotionException;
import model.Customer;
import model.Order;
import model.OrderInvoice;


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
