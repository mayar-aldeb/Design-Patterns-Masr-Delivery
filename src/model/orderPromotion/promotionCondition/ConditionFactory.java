package model.orderPromotion.promotionCondition;

import validation.InputValidation;

import java.math.BigDecimal;


public class ConditionFactory {

    private static String district;
    private static int expirationDate;
    private static BigDecimal minimumTotal;

    public static void setParameters(int type){
        if(type ==1)
            district = InputValidation.setValidString("enter district: ");
        else if(type==2) {
            int month =InputValidation.setInt("enter expiration month: ");
            if(month>12)
                throw new IllegalArgumentException("invalid month.");
            expirationDate=month;
        }
        else if(type==4)
            minimumTotal= InputValidation.setBigDecimal("enter minimum total: ");

    }

    public static PromotionCondition create(int type){
        return switch (type){
            case 1->
                    new DistrictCondition(district);
            case 2->
                    new ExpiryDateCondition(expirationDate);
            case 3->
                    new FirstTimeCustomerCondition();
            case 4->
                    new MinimumSubTotalCondition(minimumTotal);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

}
