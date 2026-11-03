package org.route.model.orderPromotion.promotionCondition;

import org.route.exception.PromotionException;
import org.route.model.Customer;
import org.route.model.Order;
import org.route.model.OrderInvoice;

public interface PromotionCondition {

    void checkCondition(OrderInvoice invoice, Customer customer, Order order) throws PromotionException;
}
