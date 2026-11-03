package org.route.model.orderPromotion;


import org.route.model.orderPromotion.promotionCondition.PromotionCondition;
import org.route.validation.InputValidation;

import java.math.BigDecimal;
import java.util.List;

public class PromotionFactory {
    private static BigDecimal amount;
    private static BigDecimal discountRate;
    private static BigDecimal maximumDiscountCap;

    public static void setParameters(int type){
        if(type ==1)
            amount = InputValidation.setBigDecimal("enter amount: ");
        else if(type==3) {
            discountRate = InputValidation.setBigDecimal("enter discount rate: ");
            maximumDiscountCap = InputValidation.setBigDecimal("enter maximum discount cape: ");
        }
    }

    public static Promotion create(int type, List<PromotionCondition> condition){
        return switch (type){
            case 1->
                    new FixedAmountDiscountPromotion(amount,condition);
            case 2->
                    new FreeDeliveryPromotion(condition);
            case 3->
                    new SubtotalDiscountPromotion(condition,discountRate,maximumDiscountCap);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
