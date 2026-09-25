package platform;

import enums.VehicleType;
import model.*;
import model.orderPromotion.Promotion;
import model.orderPromotion.PromotionFactory;
import model.orderPromotion.promotionCondition.PromotionCondition;
import service.*;
import validation.InputValidation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class AdminPlatform extends Platform{


    public AdminPlatform(Map<Integer, Restaurant> restaurants, Map<Integer, Customer> customers, Map<Integer, OrderInvoice> invoices, Map<Integer, Order> orders, Map<Integer, Rider> riders, Set<String> districts, Set<String> cuisines, Queue<Order> readyOrders, Map<String, Promotion> promotions, RestaurantService restaurantService, OrderService orderService, CustomerService customerService, OrderInvoiceService invoiceService, OrderStatusFacade statusFacade, RiderService riderService, AdminService adminService) {
        super(restaurants, customers, invoices, orders, riders, districts, cuisines, readyOrders, promotions, restaurantService, orderService, customerService, invoiceService, statusFacade, riderService, adminService);
    }

    public void addCustomer(){
        String name = InputValidation.setValidString("enter customer name: ");
        String phone = InputValidation.setValidString("enter phone number: ");
        String district=InputValidation.setValidString("enter district: ");
        String detail=InputValidation.setValidString("enter address details: ");
        BigDecimal balance=InputValidation.setBigDecimal("enter balance: ");
        Customer customer=customerService.createCustomer(name,phone,district,detail,balance,new ArrayList<>());
        if(customer==null)
            return;
        System.out.println("customer created successfully.");
        customers.put(customer.getId(),customer);
        districts.add(district);
    }

    public void addRider(){
        String name =InputValidation.setValidString("enter rider name: ");
        VehicleType vehicleType =riderService.setVehicleType();
        Rider rider=new Rider(name,vehicleType,new LinkedList<>());
        riders.put(rider.getId(),rider);
        System.out.println("rider created successfully with id: "+rider.getId());
    }

    public void addRestaurant(){
        String name=InputValidation.setValidString("enter restaurant name: ");
        String district=InputValidation.setValidString("enter restaurant district: ");
        double rate =InputValidation.setDouble("enter restaurant rate: ");
        Restaurant restaurant=restaurantService.createRestaurant(name,district,rate,new LinkedHashMap<>(),new HashSet<>());
        districts.add(district);
        if(restaurant==null)
            return;
        System.out.println("restaurant created successfully.");
        restaurants.put(restaurant.getId(),restaurant);
    }

    public void removeRestaurant(){
        int restaurantId=InputValidation.setInt("enter Restaurant ID: ");
        Restaurant restaurant =restaurants.remove(restaurantId);
        if(restaurant==null){
            System.out.println("invalid ID.");
            return;
        }
        System.out.println("restaurant removed successfully.");
    }

    public void createPromotion(List<PromotionCondition> conditions){
        String promotionCode=InputValidation.setValidString("enter promotion code: ");
        if(promotions.containsKey(promotionCode.toLowerCase())){
            System.out.println("promotion code invalid.");
            return;
        }
        System.out.println("Promotion types: ");
        System.out.println("""
                1- fixed amount off.
                2- free delivery.
                3- subtotal discount with a maximum discount cap.""");
        int choice=0;
        while (choice<1||choice>3){
            choice= InputValidation.setInt("enter your choice: ");
        }
        PromotionFactory.setParameters(choice);

        int conditionChoice=adminService.setPromotionConditions(conditions);
        while (conditionChoice!=5){
            conditionChoice=adminService.setPromotionConditions(conditions);
        }

        Promotion promotion=PromotionFactory.create(choice,conditions);
        promotions.put(promotionCode.toLowerCase(),promotion);
        System.out.println("promotion created successfully");
    }

    public void viewPlatformWideStatistics(){
        System.out.println("Platform has => ");
        System.out.println("Restaurants: ");
        restaurants.values().forEach(restaurantService::displayRestaurant);
        System.out.println("======================");
        System.out.println("Orders: ");
        orders.values().forEach(orderService::displayOrder);
        System.out.println("Customers: ");
        customers.values().forEach(customerService::displayCustomer);
        System.out.println("======================");
        System.out.println("Riders: ");
        riders.values().forEach(riderService::displayRiderInfo);
        System.out.println("======================");
        System.out.println("Promotions: ");
        promotions.forEach((key, value) -> {
            System.out.println("Promotion code: " + key);
            System.out.println("Promotion type: " + value.displayPromotionType());
        });
        System.out.println("======================");
        System.out.println("Districts: ");
        districts.forEach(System.out::println);
        System.out.println("======================");
        System.out.println("Cuisines: ");
        cuisines.forEach(System.out::println);

    }

    public void calculateTotalRevenue(){
        if(invoices.isEmpty()){
            System.out.println("no orders created yet.");
            return;
        }
        String date =InputValidation.setValidString("Enter start date (yyyy-MM-dd): ");
        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date.");
            return;
        }
        date =InputValidation.setValidString("Enter end date (yyyy-MM-dd): ");
        try {
            endDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date.");
            return;
        }
        adminService.calculateTotalRevenue(startDate,endDate,invoices);
    }

    public void displayReportPlatformMenu() {
        System.out.println("""
                ========================================
                            Report Platform
                ========================================""");
        System.out.println("""
                1.Total revenue for a given date range.
                2.The top five restaurants by revenue, for a given month.
                3.Average order value per district.
                4.Filterd restaurants by rate and completed orders.
                5.Count of orders grouped by their current status.
                6.rider’s completed deliveries and	their average delivery duration.
                7.The most frequently ordered menu item across the whole platform.
                8.Customer’s full order history.
                9.The peak ordering hour of the day.
                10.Every customer who has not ordered in the last thirty days.
                11.Back to admin platform.""");
        System.out.println("=======================");
    }

    public void reportMenu(){
        int choice;
        do {
            displayReportPlatformMenu();
            choice = InputValidation.setInt("Enter your choice: ");
            switch (choice){
                case 1-> calculateTotalRevenue();
                case 2-> adminService.displayTopFiveRestaurants(orders,restaurants);
                case 3-> adminService.calculateAverageOrder(orders);
                case 4-> adminService.filterRestaurants(restaurants);
                case 5-> adminService.countOrdersPerStatus(orders);
                case 6-> adminService.displayRidersDeliveries(riders);
                case 7-> adminService.displayFrequentlyMenuItem(orders);
                case 8-> adminService.displayOrdersOfCustomer(orders);
                case 9-> adminService.findPeakHour(orders);
                case 10-> adminService.findNotActiveCustomers(customers);
                case 11-> System.out.println("Back to admin platform.");
                default->System.out.println("invalid choice, choose from the menu");
            }
        }while(choice!=11);
    }

    public void displayAdminPlatformMenu() {
        System.out.println("""
                ========================================
                            Admin Platform
                ========================================""");
        System.out.println("""
                1.Add restaurant.
                2.Remove restaurant.
                3.Add rider.
                4.Add customer.
                5.Create promotion.
                6.View Platform-wide statistics.
                7.Report Options.
                8.Back to main platform.""");
        System.out.println("=======================");
    }

    public void adminMenu(){
        int choice;
        do {
            displayAdminPlatformMenu();
            choice = InputValidation.setInt("Enter your choice: ");
            switch (choice){
                case 1-> addRestaurant();
                case 2-> removeRestaurant();
                case 3-> addRider();
                case 4-> addCustomer();
                case 5-> createPromotion(new ArrayList<>() );
                case 6-> viewPlatformWideStatistics();
                case 7-> reportMenu();
                case 8-> System.out.println("Back to main platform.");
                default->System.out.println("invalid choice, choose from the menu");
            }
        }while(choice!=8);
    }


}
