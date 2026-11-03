package org.route.model.orderPromotion;

import org.route.model.Customer;
import org.route.model.Order;
import org.route.model.OrderInvoice;
import org.route.model.orderPromotion.promotionCondition.PromotionCondition;

import java.math.BigDecimal;
import java.util.List;

public class FreeDeliveryPromotion extends Promotion {
    public FreeDeliveryPromotion(List<PromotionCondition> condition) {
        super(condition);
    }

    @Override
    public boolean applyPromotion(OrderInvoice invoice, Customer customer, Order order) {
        checkConditions(invoice,customer,order);
        invoice.setDeliveryFee(BigDecimal.valueOf(0));
        return true;
    }

    @Override
    public String displayPromotionType() {
        return "free delivery";
    }


}
