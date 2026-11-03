package org.route.model;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.route.service.CustomerService;
import org.route.service.AdminService;
import org.route.service.RestaurantService;
import org.route.service.RiderService;

@Service
@AllArgsConstructor
public class OrderStatusFacade {
private final CustomerService customerService;
private final RiderService riderService;
private final RestaurantService restaurantService;
private final AdminService adminService;


    public void changeOrderStatus(Order order){
        customerService.notifyCustomer(order);
        riderService.updateDashboard(order);
        restaurantService.recordAuditLog(order);
        adminService.recalculateStatistics(order);
    }
}
