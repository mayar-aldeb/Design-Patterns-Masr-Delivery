package model.restaurntFilterObserver;

import validation.InputValidation;

import java.math.BigDecimal;

public class FilterFactory {
    private static String cuisine;
    private static String district;
    private static BigDecimal priceCeil;
    private static double rate;

    public static void setParameters(int type){
        if(type ==1)
            cuisine = InputValidation.setValidString("enter cuisine: ");
        else if(type==2)
            district = InputValidation.setValidString("enter district: ");
        else if(type==3)
            priceCeil = InputValidation.setBigDecimal("enter priceCeil: ");
        else if(type==4)
            rate=InputValidation.setDouble("enter minimum rate: ");

    }


    public static RestaurantFilterListener create(int type){
        return switch (type){
            case 1->
                new CuisineFilter(cuisine);
            case 2->
                new DistrictFilter(district);
            case 3->
                new PriceCeilingFilter(priceCeil);
            case 4->
                new RateFilter(rate);
            default -> throw new IllegalArgumentException();
        };
    }


}
