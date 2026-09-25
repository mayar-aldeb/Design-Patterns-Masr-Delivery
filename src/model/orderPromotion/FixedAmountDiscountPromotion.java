package model.orderPromotion;

import model.Customer;
import model.Order;
import model.OrderInvoice;
import model.orderPromotion.promotionCondition.PromotionCondition;

import java.math.BigDecimal;
import java.util.List;

public class FixedAmountDiscountPromotion extends Promotion {
    private final BigDecimal amount;

    public FixedAmountDiscountPromotion(BigDecimal amount, List<PromotionCondition> condition) {
        super(condition);
        this.amount = amount;
    }

    @Override
    public boolean applyPromotion(OrderInvoice invoice, Customer customer, Order order) {
        checkConditions(invoice, customer, order);
        if(invoice.getSubTotal().compareTo(amount)<0)
            return false;
        invoice.setSubTotal(invoice.getSubTotal().subtract(amount));
        return true;
    }

    @Override
    public String displayPromotionType() {
        return "fixed amount off promotion";
    }
}
