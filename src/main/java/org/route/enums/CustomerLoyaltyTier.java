package org.route.enums;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum CustomerLoyaltyTier {
    BRONZE(BigDecimal.valueOf(0)),
    SILVER(BigDecimal.valueOf(0.1)),
    GOLD(BigDecimal.valueOf(1));
    private final BigDecimal deliveryFeeDiscount;

    CustomerLoyaltyTier(BigDecimal deliveryFeeDiscount) {
        this.deliveryFeeDiscount = deliveryFeeDiscount;
    }

}
