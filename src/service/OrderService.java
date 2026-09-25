package service;

import enums.CustomerLoyaltyTier;
import enums.OrderStatus;
import exception.StockShortageException;
import model.*;
import model.Items.Combo;
import model.Items.MenuItem;
import model.Items.OrderItem;
import model.orderStatus.OrderState;
import model.orderStatus.PlacedState;
import validation.InputValidation;
import validation.RestaurantValidation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderService {
    private static int orderCount =0;
    private final RestaurantService restaurantService;

    public OrderService(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    public BigDecimal calculateSubTotal(OrderItem item){
        return item.getItem().calculatePrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }

    public void calculateOrderTotalPrice(Order order){
        BigDecimal totalPrice= BigDecimal.valueOf(0);
        for(OrderItem orderItem:order.getItems().values()){
            totalPrice=totalPrice.add(calculateSubTotal(orderItem));
        }
        order.setTotalPrice(totalPrice);
    }

    public void displayOrderItem(OrderItem orderItem){
        orderItem.getItem().displayItem();
        System.out.println("Quantity: "+orderItem.getQuantity());
        System.out.println("---------------------------------");
    }

    public void displayOrderItems(Order order){
        System.out.println("Order Items : ");
        order.getItems().values().forEach(this::displayOrderItem);
    }

    public void displayOrder(Order order){
        System.out.println("Order ID: "+order.getId());
        System.out.println("Order total price: "+order.getTotalPrice());
        System.out.println("Order status: "+order.getOrderStatus());
        displayOrderItems(order);
    }

    public void createOrderItem(Order order,MenuItem item,int quantity){
        OrderItem orderItem=order.getItems().get(item);
        if(orderItem!=null){
            orderItem.setQuantity(orderItem.getQuantity()+quantity);
            System.out.println("order item's quantity updated successfully.");
            displayOrderItems(order);
            return;
        }
        orderItem=new OrderItem(quantity,item);
        order.getItems().put(item,orderItem);
        System.out.println("order item added successfully.");
        displayOrderItems(order);
    }

    public Order findOrderById(Map<Integer, Order> orders){
        int orderId =InputValidation.setInt("enter order ID: ");
        Order order =orders.get(orderId);
        if(order==null)
            System.out.println("invalid order ID");
        return order;
    }

    public void addItemToOrder(Order order, Map<Integer, MenuItem> items){
        MenuItem item=restaurantService.findItemById(items);
        if(item==null)
            return;
        if(item instanceof Combo combo){
            if(combo.getComboItems().isEmpty()){
                System.out.println("you can't add empty combo.");
                return;
            }
        }
        if(restaurantService.isUnValidItem(item))
            return;
        int quantity=InputValidation.setInt("enter item quantity: ");
        try {
            RestaurantValidation.validateStock(item,quantity);
        } catch (StockShortageException e){
            System.out.println(e.getMessage());
            return;
        }
        item.setStock(item.getStock()-quantity);
        createOrderItem(order,item,quantity);
    }

    public void removeItemFromOrder(Order order,Map<Integer, MenuItem> items){
        MenuItem item=restaurantService.findItemById(items);
        if(item==null)
            return;
        OrderItem orderItem =order.getItems().remove(item);
        if(orderItem==null){
            System.out.println("this order doesn't have this item.");
            return;
        }
        item.setStock(item.getStock()+orderItem.getQuantity());
        System.out.println("item removed successfully.");
        displayOrderItems(order);
    }

    public void refundOrderQuantity(Order order){
        for(OrderItem orderItem:order.getItems().values()){
            orderItem.getItem().setStock(orderItem.getItem().getStock()+orderItem.getQuantity());
        }
    }

    public void createOrderItems(Order order, Map<Integer, MenuItem> items){
        int choice;
        do{
            choice=InputValidation.setItemOption();
            if(choice==1)
                addItemToOrder(order, items);
            else if(choice==2)
                removeItemFromOrder(order, items);
            else if(choice==3){
                if(order.getItems().isEmpty()){
                    System.out.println("you cannot confirm order without items.");
                    choice=0;
                }
            }
        }while (choice!=3);
    }

    public Order createOrder(int customerId, int restaurantId, Address address, int orderPriority, Map<MenuItem,OrderItem> items, OrderState state){
        return Order.builder()
                .id(++orderCount)
                .items(items)
                .customerId(customerId)
                .restaurantId(restaurantId)
                .deliveryAddress(address)
                .orderStatus(OrderStatus.PLACED)
                .state(state)
                .placedAt(LocalDateTime.now())
                .orderPriority(orderPriority)
                .build();
    }

    public Address setAddressToOrder(List<Address> addresses){
        int count =0;
        for (Address address:addresses){
            System.out.println(++count+"-"+address);
        }
        int choice=InputValidation.setInt("Choose Address: ");
        return addresses.get(choice-1);
    }

    public Order placeOrder(Customer customer,Restaurant restaurant,Map<Integer, Order> orders,Address address){
        int orderPriority= customer.getLoyaltyTier() == CustomerLoyaltyTier.GOLD ? 0 : 1;
        Order order= createOrder(customer.getId(),restaurant.getId(),address,orderPriority,new LinkedHashMap<>(),new PlacedState());
        createOrderItems(order, restaurant.getItems());
        calculateOrderTotalPrice(order);
        orders.put(order.getId(),order);
        return order;
    }

    public boolean checkOrderWithUnavailableItems(Order order){
        return order.getItems().values().stream()
                .anyMatch(item->!item.getItem().isAvailable());
    }

}
