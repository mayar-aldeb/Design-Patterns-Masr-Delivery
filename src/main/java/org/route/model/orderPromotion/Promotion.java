package org.route.model.orderPromotion;

import org.route.exception.PromotionException;
import org.route.model.Customer;
import org.route.model.Order;
import org.route.model.OrderInvoice;
import org.route.model.orderPromotion.promotionCondition.PromotionCondition;

import java.util.List;

public abstract class Promotion {
    protected final List<PromotionCondition> condition;

    public Promotion(List<PromotionCondition> condition) {
        this.condition = condition;
    }

    public void checkConditions(OrderInvoice invoice, Customer customer, Order order)throws PromotionException {
        condition.forEach(c->c.checkCondition(invoice,customer,order));
    }
    public abstract boolean applyPromotion(OrderInvoice invoice, Customer customer, Order order) throws PromotionException;

    public abstract String displayPromotionType();


}
