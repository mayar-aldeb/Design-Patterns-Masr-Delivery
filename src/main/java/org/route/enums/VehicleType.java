package org.route.enums;

import lombok.Getter;

@Getter
public enum VehicleType {
    MOTORCYCLE(100,100,15),
    BICYCLE(15,25,5),
    CAR(300,160,50);
    private final double range;
    private final int speed;
    private final double eligibleOrderSize;

    VehicleType(double range, int speed, double eligibleOrderSize) {
        this.range = range;
        this.speed = speed;
        this.eligibleOrderSize = eligibleOrderSize;
    }

}
