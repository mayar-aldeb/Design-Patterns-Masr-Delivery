package model;

import enums.VehicleType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Rider {
    private static int count=0;
    private final int id;
    private String name;
    private VehicleType vehicleType;
    private String district;
    private boolean available;
    private int completedOrders;
    private int activeOrderId;
    private List<Order> completedDeliveries;


    public Rider(String name, VehicleType vehicleType,List<Order>completedDeliveries) {
        this.id = ++count;
        this.name = name;
        this.vehicleType = vehicleType;
        this.district = null;
        this.available = true;
        completedOrders=0;
        activeOrderId=0;
        this.completedDeliveries = completedDeliveries;
    }
}
