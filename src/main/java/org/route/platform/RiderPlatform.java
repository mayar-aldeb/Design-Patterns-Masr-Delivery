package org.route.platform;

import org.route.exception.BusyRiderException;
import org.route.exception.IllegalOrderTransitionException;
import lombok.RequiredArgsConstructor;
import org.route.model.*;
import org.springframework.stereotype.Component;
import org.route.repository.*;
import org.route.service.*;
import org.route.validation.InputValidation;
import org.route.validation.RiderValidation;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class RiderPlatform {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final RiderService riderService;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReadyOrdersRepository readyOrdersRepository;
    private final OrderStatusFacade statusFacade;



    public void goOnDuty(Rider rider){
        if(rider.isAvailable()){
            System.out.println("rider is already on duty.");
            return;
        }
        rider.setAvailable(true);
    }

    public void goOffDuty(Rider rider){
        if(!rider.isAvailable()){
            System.out.println("rider is already off duty.");
            return;
        }
        rider.setAvailable(false);
    }

    public void assignOrder(Rider rider){
        try {
            RiderValidation.validateRider(rider);
        }catch (BusyRiderException e){
            System.out.println(e.getMessage());
            return;
        }
        if(rider.getActiveOrderId()>0){
            System.out.println("rider already has order.");
            return;
        }
        if(readyOrdersRepository.findReadyOrders().isEmpty()){
            System.out.println("no ready orders to deliver.");
            return;
        }
        Order order=readyOrdersRepository.peekReadyOrder();
        try {
            order.getState().assign(order);
        }catch (IllegalOrderTransitionException e){
            System.out.println(e.getMessage());
            return;
        }
        statusFacade.changeOrderStatus(order);
        readyOrdersRepository.removeReadyOrder();
        riderService.assignOrderToRider(rider,order);
    }

    public void markPickedUp(Rider rider){
        if(rider.getActiveOrderId()==0){
            System.out.println("rider has no orders assigned.");
            return;
        }
        Order order=orderRepository.findById(rider.getActiveOrderId()).orElseThrow(()->new RuntimeException("order not found"));
        try {
            order.getState().outForDelivery(order);
        }catch (IllegalOrderTransitionException e){
            System.out.println(e.getMessage());
            return;
        }
        statusFacade.changeOrderStatus(order);
    }

    public void viewAssignedOrder(Rider rider){
        if(rider.getActiveOrderId()==0){
            System.out.println("rider has no orders assigned.");
            return;
        }
        Order order=orderRepository.findById(rider.getActiveOrderId()).orElseThrow(()->new RuntimeException("order not found"));
        orderService.displayOrder(order);
    }

    public void deliverOrder(Rider rider){
        if(rider.getActiveOrderId()==0){
            System.out.println("rider has no orders assigned.");
            return;
        }
        Order order;
        Restaurant restaurant;
        Customer customer;
        try {
            order=orderRepository.findById(rider.getActiveOrderId()).orElseThrow(()->new RuntimeException("order not found"));
            restaurant = restaurantRepository.findById(order.getRestaurantId()).orElseThrow(()->new RuntimeException("restaurant not found"));
            customer=customerRepository.findById(order.getCustomerId()).orElseThrow(()->new RuntimeException("customer not found"));
        }catch (NullPointerException e){
            System.out.println(e.getMessage());
            return;
        }
        try {
            order.getState().deliver(order);
        }catch (IllegalOrderTransitionException e){
            System.out.println(e.getMessage());
            return;
        }
        statusFacade.changeOrderStatus(order);
        riderService.deliverOrder(rider,order);
        customerService.setCompletedOrderToCustomer(customer,order);
        restaurant.setCompletedOrders(restaurant.getCompletedOrders()+1);
    }

    public void personalDeliveryStatistics(Rider rider){
        riderService.displayRiderInfo(rider);

        BigDecimal [] totalCollectedFunds= {BigDecimal.valueOf(0)};
        System.out.println("delivered orders: ");
        rider.getCompletedDeliveries().forEach(order->{
            orderService.displayOrder(order);
            totalCollectedFunds[0]=totalCollectedFunds[0].add(order.getTotalPrice());
        });
        System.out.println("total collected funds = "+totalCollectedFunds[0]);
    }

    public void displayRiderPlatformMenu() {
        System.out.println("""
                ========================================
                            Rider Platform
                ========================================""");
        System.out.println("""
                1.Go on duty.
                2.Go off duty.
                3.assign order.
                4.View the currently assigned order.
                5.Mark order picked up.
                6.Mark order delivered.
                7.View personal delivery statistics.
                8.Back to main org.route.platform.""");
        System.out.println("=======================");
    }

    public void riderMenu(Rider rider){
        int choice;
        do {
            displayRiderPlatformMenu();
            choice = InputValidation.setInt("Enter your choice: ");
            switch (choice){
                case 1-> goOnDuty(rider);
                case 2-> goOffDuty(rider);
                case 3-> assignOrder(rider);
                case 4-> viewAssignedOrder(rider);
                case 5-> markPickedUp(rider);
                case 6-> deliverOrder(rider);
                case 7-> personalDeliveryStatistics(rider);
                case 8-> System.out.println("Back to main org.route.platform.");
                default->System.out.println("invalid choice, choose from the menu");
            }
        }while(choice!=8);
    }


}
