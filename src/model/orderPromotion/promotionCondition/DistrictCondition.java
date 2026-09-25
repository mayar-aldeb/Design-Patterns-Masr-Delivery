package model.orderPromotion.promotionCondition;

import exception.PromotionException;
import model.Customer;
import model.Order;
import model.OrderInvoice;

public class DistrictCondition implements PromotionCondition{

    private final String district;

    public DistrictCondition(String district) {
        this.district = district;
    }


    @Override
    public void checkCondition(OrderInvoice invoice, Customer customer, Order order) {
        if(!order.getDeliveryAddress().getDistrict().equals(district))
            throw new PromotionException("order district is not allowed for this promotion.");
    }
}
