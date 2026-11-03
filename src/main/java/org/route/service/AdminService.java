package org.route.service;


import org.route.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.route.model.*;
import org.route.model.Items.MenuItem;
import org.route.model.Items.OrderItem;
import org.route.model.orderPromotion.promotionCondition.ConditionFactory;
import org.route.model.orderPromotion.promotionCondition.PromotionCondition;
import org.springframework.stereotype.Service;
import org.route.repository.*;
import org.route.validation.InputValidation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final OrderInvoiceService invoiceService;
    private final OrderService orderService;
    private final OrderInvoiceRepository orderInvoiceRepository;
    private final OrderRepository  orderRepository;
    private final RestaurantRepository  restaurantRepository;
    private final RiderRepository  riderRepository;
    private final CustomerRepository  customerRepository;


    public void calculateTotalRevenue(LocalDate startDate, LocalDate endDate ){
        if(endDate.isBefore(startDate)){
            System.out.println("invalid end date.");
        }
        BigDecimal [] totalSum = {BigDecimal.valueOf(0)};
        orderInvoiceRepository.findAll().stream().
                filter(invoice -> invoice.getOrderDate().toLocalDate().isAfter(startDate)||
                        invoice.getOrderDate().toLocalDate().equals(startDate)).
                filter(invoice -> invoice.getOrderDate().toLocalDate().isBefore(endDate)||
                        invoice.getOrderDate().toLocalDate().equals(endDate)).
                forEach(invoice ->
                   totalSum[0]=totalSum[0].add(invoiceService.calculateTotal(invoice))
                 );
        System.out.println("total Revenue = "+totalSum[0]);
    }

    public void displayTopFiveRestaurants(){
        if(orderRepository.findAll().isEmpty()||restaurantRepository.findAll().isEmpty()){
            System.out.println("no data created yet.");
            return;
        }
        int month=InputValidation.setInt("enter month number: ");
        if(month>12){
            System.out.println("invalid month number.");
            return;
        }
        int year=InputValidation.setInt("enter year number: ");
        if(year<2000||year>2026){
            System.out.println("invalid year number.");
            return;
        }
        Map<Integer,BigDecimal> restaurantsRevenues=new HashMap<>();
        orderRepository.findAll().stream().
                filter(order -> order.getPlacedAt().getMonthValue()== month).
                filter(order -> order.getPlacedAt().getYear()==year).
                filter(order -> order.getOrderStatus()!=OrderStatus.CANCELLED).
                forEach(order ->{
                if(restaurantsRevenues.containsKey(order.getRestaurantId())){
                    restaurantsRevenues.put(order.getRestaurantId(),
                            order.getTotalPrice().
                                    add(restaurantsRevenues.get(order.getRestaurantId())));
                }
                else
                    restaurantsRevenues.put(order.getRestaurantId(),order.getTotalPrice());
                });
        restaurantsRevenues.entrySet().stream().
                sorted(Map.Entry.<Integer, BigDecimal>comparingByValue().reversed()).
                limit(5).
                forEach(entry->
                    System.out.println(restaurantRepository.findById(entry.getKey()).orElseThrow().getName()+" : "+entry.getValue())
                );
    }

    public void calculateAverageOrder(){
        if(orderRepository.findAll().isEmpty()){
            System.out.println("no orders created yet.");
            return;
        }
        String district=InputValidation.setValidString("Enter district: ");
        double avg= orderRepository.findAll().stream().
                filter(order -> order.getDeliveryAddress().getDistrict().equals(district)).
                mapToDouble(o->o.getTotalPrice().doubleValue()).
                average().orElse(0.0);
        if(avg==0.0){
            System.out.println("no orders in this district.");
            return;
        }
        System.out.println("Average prices = "+avg);
    }

    public void filterRestaurants(){
        if(restaurantRepository.findAll().isEmpty()){
            System.out.println("no restaurant created yet");
            return;
        }
        boolean []flag = {false};
        Comparator<Restaurant> comparator=Comparator.comparing(Restaurant::getRate).reversed().thenComparing(Restaurant::getName);
        restaurantRepository.findAll().stream().
                filter(restaurant -> restaurant.getRate()>4.5).
                filter(restaurant -> restaurant.getCompletedOrders()>=20).
                sorted(comparator).
                forEach(r->{
                        System.out.println(r.getName());
                        flag[0]=true;
                });
        if(!flag[0])
            System.out.println("no restaurant matches the conditions.");
    }

    public void countOrdersPerStatus(){
        if(orderRepository.findAll().isEmpty()){
            System.out.println("no orders created yet.");
            return;
        }
        orderRepository.findAll().stream().
                map(Order::getOrderStatus).
                collect(Collectors.groupingBy(
                        Function.identity(),Collectors.counting())).
                forEach((key, value) -> System.out.println(key + " : " + value));
    }

    public void displayRidersDeliveries(){
        if(riderRepository.findAllRiders().isEmpty()){
            System.out.println("no riders created yet.");
            return;
        }
        riderRepository.findAllRiders().forEach(rider -> {
            System.out.println("rider name: "+rider.getName());
            rider.getCompletedDeliveries().
                    reversed().
                    forEach(o->System.out.println("order ID: "+o.getId()));
            System.out.println("============================================");
        });

    }

    public void displayFrequentlyMenuItem(){
        if(orderRepository.findAll().isEmpty()){
            System.out.println("no orders created yet.");
            return ;
        }
        MenuItem menuItem= orderRepository.findAll().stream().
                flatMap(order -> order.getItems().values().stream()).
                map(OrderItem::getItem).
                collect(Collectors.groupingBy(
                        Function.identity(),Collectors.counting())).
                entrySet().
                stream().
                max(Map.Entry.comparingByValue()).
                map(Map.Entry::getKey).
                orElse(null);
        if(menuItem==null)
            return;
        menuItem.displayItem();
    }

    public void displayOrdersOfCustomer(){
        if(orderRepository.findAll().isEmpty()){
            System.out.println("no orders created yet.");
            return;
        }
        int customerId=InputValidation.setInt("enter customer ID: ");
        Comparator<Order> comparator=Comparator.comparing(Order::getPlacedAt).reversed();
        BigDecimal[] totalSpend={new BigDecimal(0)};
        orderRepository.findAll().stream().
                filter(order -> order.getCustomerId()==customerId).
                sorted(comparator).forEach(order->{
                    orderService.displayOrder(order);
                    totalSpend[0]=totalSpend[0].add(order.getTotalPrice());
                });
        if((totalSpend[0].compareTo(BigDecimal.valueOf(0))==0)){
            System.out.println("there is no orders for this customer.");
            return;
        }
        System.out.println("Customer total spend = "+totalSpend[0]);
    }

    public void findPeakHour(){
        if(orderRepository.findAll().isEmpty()){
            System.out.println("no orders assigned yet.");
            return;
        }
        int peakHour= orderRepository.findAll().stream().
                map(order -> order.getPlacedAt().getHour()).
                collect(Collectors.groupingBy(
                        Function.identity(),Collectors.counting())).
                entrySet().
                stream().
                max(Map.Entry.comparingByValue()).
                map(Map.Entry::getKey).
                orElse(0);
        System.out.println("peak hour is: "+peakHour);
    }

    public void findNotActiveCustomers(){
        if(customerRepository.findAll().isEmpty()){
            System.out.println("no customer created yet.");
            return;
        }
        boolean [] flag ={false};
        customerRepository.findAll().stream().
                filter(customer ->
                        customer.getLastOrder()==null||
                                ChronoUnit.DAYS.between(customer.getLastOrder(),LocalDateTime.now())>30).
                forEach(customer ->{
                    System.out.println(customer.getName());
                    flag[0]=true;
                });
        if(!flag[0])
            System.out.println("there is no nonactive customers.");
    }

    public void recalculateStatistics(Order order){
        System.out.println("recalculate order statistics of status "+order.getOrderStatus());
    }

    public int setPromotionConditions(List<PromotionCondition> conditions){
        System.out.println("""
                filter criteria:\s
                1- district.
                2- expiry date.
                3- first time customer.
                4- minimum subtotal.
                enter 5 if you don't want conditions.""");
        int choice = InputValidation.setFilterOption("enter condition number: ");
        try {
            ConditionFactory.setParameters(choice);
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return 0;
        }
        try {
            conditions.add(ConditionFactory.create(choice));
        }catch (IllegalStateException e){
            return choice;
        }
        return choice;
    }

}
