package platform;

import model.*;
import model.orderPromotion.Promotion;
import service.*;
import validation.InputValidation;


import java.util.*;

public class Platform {
    protected final Map<Integer, Restaurant> restaurants;
    protected final Map<Integer, Customer> customers;
    protected final Map<Integer, OrderInvoice> invoices;
    protected final Map<Integer, Order> orders;
    protected final Map<Integer, Rider> riders;
    protected final Set<String> districts;
    protected final Set<String> cuisines;
    protected final Queue<Order> readyOrders;
    protected final Map<String, Promotion> promotions;

    protected final RestaurantService restaurantService;
    protected final OrderService orderService;
    protected final CustomerService customerService;
    protected final OrderInvoiceService invoiceService;
    protected final RiderService riderService;
    protected final AdminService adminService;

    protected final OrderStatusFacade statusFacade;




    public Platform(Map<Integer,Restaurant> restaurants, Map<Integer, Customer> customers, Map<Integer, OrderInvoice> invoices,
                    Map<Integer, Order> orders, Map<Integer, Rider> riders, Set<String> districts,
                    Set<String> cuisines, Queue<Order> readyOrders,
                    Map<String, Promotion> promotions, RestaurantService restaurantService, OrderService orderService, CustomerService customerService, OrderInvoiceService invoiceService, OrderStatusFacade statusFacade, RiderService riderService, AdminService adminService) {
        this.restaurants = restaurants;
        this.customers = customers;
        this.invoices = invoices;
        this.orders = orders;
        this.riders = riders;
        this.districts = districts;
        this.cuisines = cuisines;
        this.readyOrders = readyOrders;
        this.promotions = promotions;

        this.restaurantService = restaurantService;
        this.orderService = orderService;
        this.customerService = customerService;
        this.invoiceService = invoiceService;
        this.riderService = riderService;
        this.adminService = adminService;

        this.statusFacade = statusFacade;
    }

    public Customer findCustomerById(){
        int customerId= InputValidation.setInt("enter customer ID: ");
        return customerService.findCustomerById(customers,customerId);
    }

    public Restaurant findRestaurantById(){
        int restaurantId = InputValidation.setInt("enter restaurant ID: ");
        Restaurant restaurant=restaurants.get(restaurantId);
        if(restaurant==null)
            System.out.println("invalid ID.");
        return restaurant;
    }

    public void displayPlatformMenu(){
        System.out.println("""
                ========================================
                        Masr Delivery Platform
                ========================================""");
        System.out.println("""
                1.Customer.
                2.Restaurant.
                3.Rider.
                4.Admin & Reports.
                5.exit.""");
        System.out.println("=======================");
    }

    public void platformMenu(AdminPlatform adminPlatform, CustomerPlatform customerPlatform, RestaurantPlatform restaurantPlatform,RiderPlatform riderPlatform){
        int choice;
        do {
            displayPlatformMenu();
            choice = InputValidation.setInt("Enter your choice: ");
            switch (choice){
                case 1->{
                    Customer customer =findCustomerById();
                    if(customer!=null)
                        customerPlatform.customerMenu(customer);
                }
                case 2->{
                    Restaurant restaurant =findRestaurantById();
                    if(restaurant!=null)
                        restaurantPlatform.restaurantMenu(restaurant);
                }
                case 3->{
                    Rider rider =riderService.findRiderById(riders);
                    if(rider!=null)
                        riderPlatform.riderMenu(rider);
                }
                case 4->adminPlatform.adminMenu();
                case 5-> System.out.println("thank you for visiting our platform.");
                default->System.out.println("invalid choice, choose from the menu");
            }
        }while(choice!=5);
    }


}
