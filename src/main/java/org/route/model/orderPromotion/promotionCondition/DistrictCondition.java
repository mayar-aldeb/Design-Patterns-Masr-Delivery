package org.route.model.orderPromotion.promotionCondition;

import org.route.exception.PromotionException;
import org.route.model.Customer;
import org.route.model.Order;
import org.route.model.OrderInvoice;

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
