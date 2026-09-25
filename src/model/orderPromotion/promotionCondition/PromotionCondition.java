package model.orderPromotion.promotionCondition;

import exception.PromotionException;
import model.Customer;
import model.Order;
import model.OrderInvoice;

public interface PromotionCondition {

    void checkCondition(OrderInvoice invoice, Customer customer, Order order) throws PromotionException;
}
