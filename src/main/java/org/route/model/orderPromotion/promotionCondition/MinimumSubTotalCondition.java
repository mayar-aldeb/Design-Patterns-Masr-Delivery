package org.route.model.orderPromotion.promotionCondition;

import org.route.exception.PromotionException;
import org.route.model.Customer;
import org.route.model.Order;
import org.route.model.OrderInvoice;

import java.math.BigDecimal;

public class MinimumSubTotalCondition implements PromotionCondition{
    private final BigDecimal minimumTotal;

    public MinimumSubTotalCondition(BigDecimal minimumTotal) {
        this.minimumTotal = minimumTotal;
    }

    @Override
    public void checkCondition(OrderInvoice invoice, Customer customer, Order order) {
        if(invoice.getSubTotal().compareTo(minimumTotal) < 0)
            throw new PromotionException("total didn't reach minimum total for promotion.");
    }
}
