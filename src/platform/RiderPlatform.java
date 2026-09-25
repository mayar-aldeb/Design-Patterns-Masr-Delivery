package platform;

import exception.BusyRiderException;
import exception.IllegalOrderTransitionException;
import model.*;
import model.orderPromotion.Promotion;
import service.*;
import validation.InputValidation;
import validation.RiderValidation;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class RiderPlatform extends Platform{


    public RiderPlatform(Map<Integer, Restaurant> restaurants, Map<Integer, Customer> customers, Map<Integer, OrderInvoice> invoices, Map<Integer, Order> orders, Map<Integer, Rider> riders, Set<String> districts, Set<String> cuisines, Queue<Order> readyOrders, Map<String, Promotion> promotions, RestaurantService restaurantService, OrderService orderService, CustomerService customerService, OrderInvoiceService invoiceService, OrderStatusFacade statusFacade, RiderService riderService, AdminService adminService) {
        super(restaurants, customers, invoices, orders, riders, districts, cuisines, readyOrders, promotions, restaurantService, orderService, customerService, invoiceService, statusFacade, riderService, adminService);
    }

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
        if(readyOrders.isEmpty()){
            System.out.println("no ready orders to deliver.");
            return;
        }
        Order order=readyOrders.peek();
        try {
            order.getState().assign(order);
        }catch (IllegalOrderTransitionException e){
            System.out.println(e.getMessage());
            return;
        }
        statusFacade.changeOrderStatus(order);
        readyOrders.poll();
        riderService.assignOrderToRider(rider,order);
    }

    public void markPickedUp(Rider rider){
        if(rider.getActiveOrderId()==0){
            System.out.println("rider has no orders assigned.");
            return;
        }
        Order order=orders.get(rider.getActiveOrderId());
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
        Order order=orders.get(rider.getActiveOrderId());
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
            order=orders.get(rider.getActiveOrderId());
            restaurant = restaurants.get(order.getRestaurantId());
            customer=customers.get(order.getCustomerId());
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
                8.Back to main platform.""");
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
                case 8-> System.out.println("Back to main platform.");
                default->System.out.println("invalid choice, choose from the menu");
            }
        }while(choice!=8);
    }


}
