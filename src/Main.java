import model.*;
import model.orderPromotion.Promotion;
import platform.*;
import service.*;

void main() {
    Map<Integer, Restaurant> restaurants= new HashMap<>();
    Map<Integer, Customer> customers=new HashMap<>();
    Map<Integer, OrderInvoice> invoices=new HashMap<>();
    Map<Integer, Order> orders=new HashMap<>();
    Map<Integer, Rider> riders=new HashMap<>();
    Set<String> districts=new HashSet<>();
    Set<String> cuisines=new HashSet<>();
    Queue<Order> readyOrders=new PriorityQueue<>(Comparator.comparing(Order::getOrderPriority).
            thenComparing(Order::getPlacedAt));
    Map<String, Promotion> promotions=new HashMap<>();

    CustomerService customerService=new CustomerService();
    RiderService riderService=new RiderService();
    RestaurantService restaurantService=new RestaurantService(customerService);
    OrderService orderService=new OrderService(restaurantService);
    OrderInvoiceService invoiceService=new OrderInvoiceService(orderService);
    AdminService adminService=new AdminService(invoiceService,orderService);
    OrderStatusFacade statusFacade=new OrderStatusFacade(customerService,riderService,restaurantService,adminService);

    Platform platform=new Platform(restaurants,customers,invoices,orders,riders,districts,
            cuisines,readyOrders,promotions,restaurantService,orderService,customerService,
            invoiceService, statusFacade,riderService,adminService);

    CustomerPlatform customerPlatform= new CustomerPlatform(restaurants,customers,invoices,orders,riders,districts,
            cuisines,readyOrders,promotions,restaurantService,orderService,customerService,
            invoiceService, statusFacade,riderService,adminService);

    RestaurantPlatform restaurantPlatform=new RestaurantPlatform(restaurants,customers,invoices,orders,riders,districts,
            cuisines,readyOrders,promotions,restaurantService,orderService,customerService,
            invoiceService, statusFacade,riderService,adminService);

    RiderPlatform riderPlatform=new RiderPlatform(restaurants,customers,invoices,orders,riders,districts,
            cuisines,readyOrders,promotions,restaurantService,orderService,customerService,
            invoiceService, statusFacade,riderService,adminService);

    AdminPlatform adminPlatform=new AdminPlatform(restaurants,customers,invoices,orders,riders,districts,
            cuisines,readyOrders,promotions,restaurantService,orderService,customerService,
            invoiceService, statusFacade,riderService,adminService);

    platform.platformMenu(adminPlatform, customerPlatform, restaurantPlatform, riderPlatform);


}
