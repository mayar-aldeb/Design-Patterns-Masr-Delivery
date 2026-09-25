package model;

import service.CustomerService;
import service.AdminService;
import service.RestaurantService;
import service.RiderService;

public class OrderStatusFacade {
private final CustomerService customerService;
private final RiderService riderService;
private final RestaurantService restaurantService;
private final AdminService adminService;

    public OrderStatusFacade(CustomerService customerService, RiderService riderService, RestaurantService restaurantService, AdminService adminService) {
        this.customerService = customerService;
        this.riderService = riderService;
        this.restaurantService = restaurantService;
        this.adminService = adminService;
    }

    public void changeOrderStatus(Order order){
        customerService.notifyCustomer(order);
        riderService.updateDashboard(order);
        restaurantService.recordAuditLog(order);
        adminService.recalculateStatistics(order);
    }
}
