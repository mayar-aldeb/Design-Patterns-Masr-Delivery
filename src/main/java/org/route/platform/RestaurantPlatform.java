package org.route.platform;

import org.route.exception.IllegalOrderTransitionException;
import lombok.RequiredArgsConstructor;
import org.route.model.*;
import org.route.model.Items.Combo;
import org.route.model.Items.MenuItem;
import org.springframework.stereotype.Component;
import org.route.repository.*;
import org.route.service.*;
import org.route.validation.InputValidation;

import java.math.BigDecimal;
import java.time.LocalDate;



@Component
@RequiredArgsConstructor
public class RestaurantPlatform {

    private final RestaurantService restaurantService;
    private final OrderService orderService;
    private final CustomerService customerService;
    private final OrderInvoiceService invoiceService;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ReadyOrdersRepository readyOrdersRepository;
    private final OrderStatusFacade statusFacade;


    public void markOrderAsAccepted(Restaurant restaurant){
        Order order=orderService.findOrderById();

        if(order.getRestaurantId()!=restaurant.getId()){
            System.out.println("this order belong to author restaurant.");
            return;
        }
        if(orderService.checkOrderWithUnavailableItems(order)){
            try {
                order.getState().cancel(order);
            }catch (IllegalOrderTransitionException e){
                System.out.println(e.getMessage());
                return;
            }
            orderService.refundOrderQuantity(order);
            OrderInvoice invoice=invoiceService.findInvoiceById(order.getId());
            Customer customer=customerRepository.findById(order.getCustomerId()).orElseThrow(()->new RuntimeException("customer not found"));
            if(invoice.isPayed()){
                customerService.refundToWallet(customer,order.getTotalPrice());
            }
            System.out.println("order rejected as it has unavailable items.");
            return;
        }
        try {
            order.getState().accept(order);
        }catch (IllegalOrderTransitionException e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("order marked as accepted successfully.");
        statusFacade.changeOrderStatus(order);
    }

    public void markOrderAsPreparing(Restaurant restaurant){
        Order order=orderService.findOrderById();

        if(order.getRestaurantId()!=restaurant.getId()){
            System.out.println("this order belong to author restaurant.");
            return;
        }
        try {
            order.getState().prepare(order);
        }catch (IllegalOrderTransitionException e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("order marked as preparing successfully.");
        statusFacade.changeOrderStatus(order);
    }

    public void markOrderAsReady(Restaurant restaurant){
        Order order=orderService.findOrderById();
        if(order.getRestaurantId()!=restaurant.getId()){
            System.out.println("this order belong to author restaurant.");
            return;
        }
        OrderInvoice invoice=invoiceService.findInvoiceById(order.getId());
        if(invoice==null)
            return;
        if(!invoice.isPayed()){
            System.out.println("order is unpaid cannot be ready.");
            return;
        }
        try {
            order.getState().ready(order);
        }catch (IllegalOrderTransitionException e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("order marked as ready successfully.");
        readyOrdersRepository.addOrder(order);
        statusFacade.changeOrderStatus(order);

    }

    public void toggleItemAvailability(Restaurant restaurant,boolean availability){
        restaurantService.displayRestaurantItems(restaurant);
        MenuItem item=restaurantService.findItemById(restaurant.getItems());
        if(item==null)
            return;
        if(item.isAvailable()==availability){
            System.out.println("item already "+(availability?"available.":"unavailable."));
            return;
        }
        item.setAvailable(availability);
        System.out.println("item marked as "+(availability?"available":"unavailable")+" successfully.");
    }

    public void adjustStock(Restaurant restaurant){
        restaurantService.displayRestaurantItems(restaurant);
        MenuItem item=restaurantService.findItemById(restaurant.getItems());
        if(item==null)
            return;
        System.out.println("item's current stock = "+item.getStock());
        int stock= InputValidation.setInt("enter new stock: ");
        item.setStock(stock);
    }

    public void displayTodayOrders(Restaurant restaurant){
        BigDecimal[] totalRevenue={BigDecimal.valueOf(0)};
        orderRepository.findAll().stream().
                filter(o->o.getRestaurantId()==restaurant.getId()).
                filter(o->o.getPlacedAt().toLocalDate().equals(LocalDate.now())).
                forEach(order -> {
                    orderService.displayOrder(order);
                    totalRevenue[0]=totalRevenue[0].add(order.getTotalPrice());
                });
        System.out.println("total revenue = "+totalRevenue[0]);
    }

    public void addMenuItem(Restaurant restaurant){
        int itemType =restaurantService.setItemType();
        String name =InputValidation.setValidString("enter item name: ");
        String cuisine=InputValidation.setValidString("enter cuisine: ");
        BigDecimal price= BigDecimal.valueOf(0);
        int preparationTime=0;
        if(itemType!=1) {
            price = InputValidation.setBigDecimal("enter item price: ");
            preparationTime = InputValidation.setInt("enter preparation time in minutes: ");
        }
        int stock=InputValidation.setInt("enter item stock: ");
        restaurantService.addItem(restaurant,itemType,name,cuisine,price,preparationTime,stock);
    }

    public void addComboItem(Restaurant restaurant){
        Combo combo =restaurantService.findComboById(restaurant.getItems());
        if(combo==null)
            return;
        MenuItem item= restaurantService.findItemById(restaurant.getItems());
        if(item==null)
            return;
        restaurantService.addItemToCombo(item, combo);
        System.out.println("item added successfully.");
    }

    public void removeMenuItem(Restaurant restaurant){
        int itemId=InputValidation.setInt("enter item ID: ");
        MenuItem item=restaurant.getItems().remove(itemId);
        if(item==null) {
            System.out.println("invalid ID.");
            return;
        }
        System.out.println("item removed successfully.");
    }

    public void removeComboItem(Restaurant restaurant){
        Combo combo =restaurantService.findComboById(restaurant.getItems());
        if(combo==null)
            return;
        MenuItem item= restaurantService.findItemById(combo.getComboItems());
        if(item==null)
            return;
        restaurantService.removeItemFromCombo(item,combo);

    }

    public void displayRestaurantPlatformMenu() {
        System.out.println("""
                ========================================
                         Restaurant Platform
                ========================================""");
        System.out.println("""
                1.Accept or reject pending order.
                2.Mark an order preparing.
                3.Mark an order ready.
                4.Make item available.
                5.Make item unavailable.
                6.Add menu item.
                7.Remove menu item.
                8.add item to combo.
                9.remove item from combo.
                10.Adjust daily stock.
                11.View today’s orders and revenue.
                12.Back to main org.route.platform.""");
        System.out.println("=======================");
    }

    public void restaurantMenu(Restaurant restaurant){
        int choice;
        do {
            displayRestaurantPlatformMenu();
            choice = InputValidation.setInt("Enter your choice: ");
            switch (choice){
                case 1->markOrderAsAccepted(restaurant);
                case 2->markOrderAsPreparing(restaurant);
                case 3->markOrderAsReady(restaurant);
                case 4->toggleItemAvailability(restaurant, true);
                case 5->toggleItemAvailability(restaurant, false);
                case 6-> addMenuItem(restaurant);
                case 7->removeMenuItem(restaurant);
                case 8->addComboItem(restaurant);
                case 9->removeComboItem(restaurant);
                case 10->adjustStock(restaurant);
                case 11->displayTodayOrders(restaurant);
                case 12-> System.out.println("Back to main org.route.platform.");
                default->System.out.println("invalid choice, choose from the menu");
            }
        }while(choice!=12);
    }




}
