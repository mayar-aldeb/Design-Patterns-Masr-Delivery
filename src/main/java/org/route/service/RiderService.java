package org.route.service;

import org.route.enums.VehicleType;
import lombok.RequiredArgsConstructor;
import org.route.model.Order;
import org.route.model.Rider;
import org.springframework.stereotype.Service;
import org.route.repository.RiderRepository;
import org.route.validation.InputValidation;


@Service
@RequiredArgsConstructor
public class RiderService {
    private final RiderRepository riderRepository;


    public void updateDashboard(Order order){
        System.out.println("new dashboard "+order.getOrderStatus());
    }

    public void displayRiderInfo(Rider rider){
        System.out.println("rider name: "+rider.getName());
        System.out.println("rider vehicle type: "+rider.getVehicleType());
        System.out.println("rider completed deliveries: "+rider.getCompletedOrders());
        System.out.println("rider is "+(rider.isAvailable()?"available.":"unavailable."));
    }

    public Rider findRiderById(){
       int riderId= InputValidation.setInt("enter rider ID: ");
        return riderRepository.findRiderById(riderId).orElseThrow(()->new RuntimeException("Rider not found"));
    }

    public void assignOrderToRider(Rider rider,Order order){
        rider.setDistrict(order.getDeliveryAddress().getDistrict());
        rider.setActiveOrderId(order.getId());
    }

    public void deliverOrder(Rider rider,Order order){
        rider.setDistrict(null);
        rider.setCompletedOrders(rider.getCompletedOrders()+1);
        rider.setActiveOrderId(0);
        rider.getCompletedDeliveries().addFirst(order);
    }

    public VehicleType setVehicleType(){
        System.out.println("""
                1- Motorcycle.
                2- Bicycle.
                3- Car.""");
        System.out.print("Enter vehicle type: ");
        int choice=0;
        while (choice<1||choice>3){
            choice= InputValidation.setInt("enter valid choice: ");
        }

        return switch (choice){
            case 1->VehicleType.MOTORCYCLE;
            case 2->VehicleType.BICYCLE;
            case 3->VehicleType.CAR;
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        };

    }
}
