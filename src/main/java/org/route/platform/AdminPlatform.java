package org.route.platform;

import org.route.enums.VehicleType;
import lombok.RequiredArgsConstructor;
import org.route.model.*;
import org.route.model.orderPromotion.Promotion;
import org.route.model.orderPromotion.PromotionFactory;
import org.route.model.orderPromotion.promotionCondition.PromotionCondition;
import org.springframework.stereotype.Component;
import org.route.repository.*;
import org.route.service.*;
import org.route.validation.InputValidation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class AdminPlatform{

    private final RestaurantService restaurantService;
    private final OrderService orderService;
    private final CustomerService customerService;
    private final RiderService riderService;
    private final AdminService adminService;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final RiderRepository riderRepository;
    private final PromotionRepository promotionRepository;
    private final OrderInvoiceRepository orderInvoiceRepository;




    public void addCustomer(){
        String name = InputValidation.setValidString("enter customer name: ");
        String phone = InputValidation.setValidString("enter phone number: ");
        String district=InputValidation.setValidString("enter district: ");
        String detail=InputValidation.setValidString("enter address details: ");
        BigDecimal balance=InputValidation.setBigDecimal("enter balance: ");
        Customer customer=customerService.createCustomer(name,phone,district,detail,balance,new ArrayList<>());
        System.out.println("customer created successfully.");
        customerRepository.save(customer);
    }

    public void addRider(){
        String name =InputValidation.setValidString("enter rider name: ");
        VehicleType vehicleType =riderService.setVehicleType();
        Rider rider=new Rider(name,vehicleType,new LinkedList<>());
        riderRepository.addRider(rider);
        System.out.println("rider created successfully with id: "+rider.getId());
    }

    public void addRestaurant(){
        String name=InputValidation.setValidString("enter restaurant name: ");
        String district=InputValidation.setValidString("enter restaurant district: ");
        double rate =InputValidation.setDouble("enter restaurant rate: ");
        Restaurant restaurant=restaurantService.createRestaurant(name,district,rate,new LinkedHashMap<>(),new HashSet<>());
        System.out.println("restaurant created successfully.");
        restaurantRepository.save(restaurant);
    }

    public void removeRestaurant(){
        int restaurantId=InputValidation.setInt("enter Restaurant ID: ");
        restaurantRepository.delete(restaurantId).orElseThrow(()->new RuntimeException("invalid ID"));
        System.out.println("restaurant removed successfully.");
    }

    public void createPromotion(List<PromotionCondition> conditions){
        String promotionCode=InputValidation.setValidString("enter promotion code: ");
        if(promotionRepository.getPromotion(promotionCode).orElse(null)!=null)
            throw new RuntimeException("promotion already exists");
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
        promotionRepository.addPromotion(promotion,promotionCode.toLowerCase());
        System.out.println("promotion created successfully");
    }

    public void viewPlatformWideStatistics(){
        System.out.println("Platform has => ");
        System.out.println("Restaurants: ");
        restaurantRepository.findAll().forEach(restaurantService::displayRestaurant);
        System.out.println("======================");
        System.out.println("Orders: ");
        orderRepository.findAll().forEach(orderService::displayOrder);
        System.out.println("Customers: ");
        customerRepository.findAll().forEach(customerService::displayCustomer);
        System.out.println("======================");
        System.out.println("Riders: ");
        riderRepository.findAllRiders().forEach(riderService::displayRiderInfo);
        System.out.println("======================");
        System.out.println("Promotions: ");
        promotionRepository.findAll().forEach((key, value) -> {
            System.out.println("Promotion code: " + key);
            System.out.println("Promotion type: " + value.displayPromotionType());
        });
        System.out.println("======================");
        System.out.println("Districts: ");
        customerRepository.findDistricts().forEach(System.out::println);
        System.out.println("======================");
        System.out.println("Cuisines: ");
        restaurantRepository.findAllCuisines().forEach(System.out::println);

    }

    public void calculateTotalRevenue(){
        if(orderInvoiceRepository.findAll().isEmpty()){
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
        adminService.calculateTotalRevenue(startDate,endDate);
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
                7.The most frequently ordered menu item across the whole org.route.platform.
                8.Customer’s full order history.
                9.The peak ordering hour of the day.
                10.Every customer who has not ordered in the last thirty days.
                11.Back to admin org.route.platform.""");
        System.out.println("=======================");
    }

    public void reportMenu(){
        int choice;
        do {
            displayReportPlatformMenu();
            choice = InputValidation.setInt("Enter your choice: ");
            switch (choice){
                case 1-> calculateTotalRevenue();
                case 2-> adminService.displayTopFiveRestaurants();
                case 3-> adminService.calculateAverageOrder();
                case 4-> adminService.filterRestaurants();
                case 5-> adminService.countOrdersPerStatus();
                case 6-> adminService.displayRidersDeliveries();
                case 7-> adminService.displayFrequentlyMenuItem();
                case 8-> adminService.displayOrdersOfCustomer();
                case 9-> adminService.findPeakHour();
                case 10-> adminService.findNotActiveCustomers();
                case 11-> System.out.println("Back to admin org.route.platform.");
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
                8.Back to main org.route.platform.""");
        System.out.println("=======================");
    }

    public void adminMenu(){
        int choice;
        do {
            displayAdminPlatformMenu();
            choice = InputValidation.setInt("Enter your choice: ");
            try {
                switch (choice) {
                    case 1 -> addRestaurant();
                    case 2 -> removeRestaurant();
                    case 3 -> addRider();
                    case 4 -> addCustomer();
                    case 5 -> createPromotion(new ArrayList<>());
                    case 6 -> viewPlatformWideStatistics();
                    case 7 -> reportMenu();
                    case 8 -> System.out.println("Back to main org.route.platform.");
                    default -> System.out.println("invalid choice, choose from the menu");
                }
            }catch(RuntimeException e){
                System.out.println(e.getMessage());
            }
        }while(choice!=8);
    }


}
