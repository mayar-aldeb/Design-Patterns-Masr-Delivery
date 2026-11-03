package org.route.platform;

import org.route.enums.OrderStatus;
import org.route.exception.*;
import lombok.RequiredArgsConstructor;
import org.route.model.*;
import org.route.model.restaurntFilterObserver.RestaurantFilterListener;
import org.springframework.stereotype.Component;
import org.route.repository.*;
import org.route.service.*;
import org.route.validation.InputValidation;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Component
@RequiredArgsConstructor
public class CustomerPlatform {

    private final RestaurantService restaurantService;
    private final OrderService orderService;
    private final CustomerService customerService;
    private final OrderInvoiceService invoiceService;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;


    private final OrderStatusFacade statusFacade;


    public void browseRestaurants(List<RestaurantFilterListener> filters){
        int filterChoice=restaurantService.setRestaurantFilters(filters);
        while (filterChoice!=5){
            filterChoice=restaurantService.setRestaurantFilters(filters);
        }
        List<Restaurant> filteredRestaurants=  restaurantRepository.findAll();
        for(var filter:filters)
            filteredRestaurants= filter.filterRestaurants(filteredRestaurants);
        Comparator<Restaurant> comparator=Comparator.comparing(Restaurant::getRate).reversed().thenComparing(Restaurant::getName);
        filteredRestaurants.stream().sorted(comparator).forEach(restaurantService::displayRestaurant);
    }

    public void searchRestaurants(){
        String search=InputValidation.setValidString("enter cuisine or restaurant name: ");
        Comparator<Restaurant> comparator=Comparator.comparing(Restaurant::getRate).reversed().thenComparing(Restaurant::getName);
        restaurantRepository.findAll().stream().
                sorted(comparator).
                filter(restaurant -> restaurant.getCuisines().contains(search) ||
                        restaurant.getName().equals(search)).
                forEach(restaurantService::displayRestaurant);
    }

    public void placeOrder(Customer customer){
        Restaurant restaurant=restaurantService.viewRestaurantMenu(customer);
        if(restaurant==null)
            return;
        if(restaurant.getItems().isEmpty())
            return;
        Address address;
        try {
            address=orderService.setAddressToOrder(customer.getAddresses());
        }catch (RuntimeException e){
            System.out.println("invalid input.");
            return;
        }
        Order order= orderService.placeOrder(customer,restaurant,address);
        invoiceService.setInvoice(order,customer);
    }

    public void payInvoice(Customer customer){
        Order order =orderService.findOrderById();
        if(order.getCustomerId()!= customer.getId()){
            System.out.println("you cannot access anther customer's invoice.");
            return;
        }
        if(order.getOrderStatus()== OrderStatus.CANCELLED){
            System.out.println("you cannot pay cancelled order");
            return;
        }
        OrderInvoice invoice =invoiceService.findInvoiceById(order.getId());
        if(invoice.isPayed()){
            System.out.println("order already payed.");
            return;
        }
        if(customerService.payFromWallet(customer,invoiceService.calculateTotal(invoice))) {
            System.out.println("payment done successfully.");
            System.out.println("new balance = "+customer.getBalance());
            invoice.setPayed(true);
        }
    }

    public void trackOrder(Customer customer){
        Order order =orderService.findOrderById();

        if(order.getCustomerId()!= customer.getId()){
            System.out.println("you cannot access anther customer's order.");
            return;
        }
        System.out.println("order status: "+order.getOrderStatus());
        long hours= ChronoUnit.HOURS.between(order.getPlacedAt(), LocalDateTime.now());
        long minutes =ChronoUnit.MINUTES.between(order.getPlacedAt(), LocalDateTime.now());
        System.out.println("elapsed time since placement is "+hours+" : "+minutes+" hours");
    }

    public void cancelOrder(Customer customer){
        Order order =orderService.findOrderById();

        if(order.getCustomerId()!= customer.getId()){
            System.out.println("you cannot access anther customer's order.");
            return;
        }
        try {
            order.getState().cancel(order);
        }catch (IllegalOrderTransitionException e){
            System.out.println(e.getMessage());
            return;
        }
        orderService.refundOrderQuantity(order);
        OrderInvoice invoice=invoiceService.findInvoiceById(order.getId());
        if(invoice.isPayed()){
            customerService.refundToWallet(customer,order.getTotalPrice());
        }
        statusFacade.changeOrderStatus(order);
    }

    public void displayOrdersHistory(Customer customer){
        Comparator<Order> comparator=Comparator.comparing(Order::getPlacedAt).reversed();
        orderRepository.findAll().stream().
                filter(o->o.getCustomerId()==customer.getId()).
                sorted(comparator).
                forEach(orderService::displayOrder);
    }

    public void displayCustomerPlatformMenu() {
        System.out.println("""
                ========================================
                          Customer Platform
                ========================================""");
        System.out.println("""
                1.Browse restaurants.
                2.Search.
                3.Veiw Menu.
                4.Place Order.
                5.Pay from wallet.
                6.Track order.
                7.Cancel order.
                8.Order history.
                9.Back to main org.route.platform.""");
        System.out.println("=======================");
    }

    public void customerMenu(Customer customer){
        int choice;
        do {
            displayCustomerPlatformMenu();
            choice = InputValidation.setInt("Enter your choice: ");
            try {
                switch (choice) {
                    case 1 -> browseRestaurants(new ArrayList<>());
                    case 2 -> searchRestaurants();
                    case 3 -> restaurantService.viewRestaurantMenu(customer);
                    case 4 -> placeOrder(customer);
                    case 5 -> payInvoice(customer);
                    case 6 -> trackOrder(customer);
                    case 7 -> cancelOrder(customer);
                    case 8 -> displayOrdersHistory(customer);
                    case 9 -> System.out.println("Back to main org.route.platform.");
                    default -> System.out.println("invalid choice, choose from the menu");
                }
            }catch (RuntimeException e){
                System.out.println(e.getMessage());
            }
        }while(choice!=9);
    }

}
