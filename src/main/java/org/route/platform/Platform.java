package org.route.platform;

import lombok.RequiredArgsConstructor;
import org.route.model.*;
import org.springframework.stereotype.Component;
import org.route.service.*;
import org.route.validation.InputValidation;


@Component
@RequiredArgsConstructor
public class Platform {


    private final RestaurantService restaurantService;
    private final CustomerService customerService;
    private final RiderService riderService;
    private final AdminPlatform adminPlatform;
    private final CustomerPlatform customerPlatform;
    private final RiderPlatform riderPlatform;
    private final RestaurantPlatform restaurantPlatform;



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

    public void platformMenu(){
        int choice;
        do {
            displayPlatformMenu();
            choice = InputValidation.setInt("Enter your choice: ");
            try {
                switch (choice) {
                    case 1 -> {
                        Customer customer = customerService.findCustomerById();
                        customerPlatform.customerMenu(customer);
                    }
                    case 2 -> {
                        Restaurant restaurant = restaurantService.findRestaurantById();
                        restaurantPlatform.restaurantMenu(restaurant);
                    }
                    case 3 -> {
                        Rider rider = riderService.findRiderById();
                        riderPlatform.riderMenu(rider);
                    }
                    case 4 -> adminPlatform.adminMenu();
                    case 5 -> System.out.println("thank you for visiting our org.route.platform.");
                    default -> System.out.println("invalid choice, choose from the menu");
                }
            }catch (RuntimeException e){
                System.out.println(e.getMessage());
            }

        }while(choice!=5);
    }


}
