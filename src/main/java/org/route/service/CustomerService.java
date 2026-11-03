package org.route.service;

import org.route.enums.CustomerLoyaltyTier;
import org.route.exception.InsufficientBalanceException;
import lombok.RequiredArgsConstructor;
import org.route.model.Customer;
import org.route.model.Address;
import org.route.model.Order;
import org.route.model.Restaurant;
import org.springframework.stereotype.Service;
import org.route.repository.CustomerRepository;
import org.route.validation.CustomerValidation;
import org.route.validation.InputValidation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public void updateLoyaltyTier(Customer customer){
        if(customer.getCompletedOrders()>30)
            customer.setLoyaltyTier(CustomerLoyaltyTier.GOLD);
        else if(customer.getCompletedOrders()>9)
            customer.setLoyaltyTier(CustomerLoyaltyTier.SILVER);
    }

    public Customer createCustomer(String name, String phone, String district, String details, BigDecimal balance, List<Restaurant> search){
        Address address=new Address(district,details);
        CustomerValidation.isValidPhone(phone);
        if(balance.compareTo(BigDecimal.valueOf(0))<0) {
           throw new InsufficientBalanceException("invalid balance");
        }
        return new Customer(name,phone,new ArrayList<>(),address,balance,search);
    }

    public void addToSearch(Customer customer,Restaurant restaurant){
        if(customer.getSearch().contains(restaurant))
            return;
        customer.getSearch().addFirst(restaurant);
        if(customer.getSearch().size()>5)
            customer.getSearch().removeLast();
    }

    public Restaurant findInSearch(Customer customer,int restaurantId){
        for (Restaurant r: customer.getSearch()){
            if(r.getId()==restaurantId)
                return r;
        }
        return null;
    }

    public void notifyCustomer(Order order){
        System.out.println("your order status updated to "+order.getOrderStatus());
    }

    public boolean payFromWallet(Customer customer,BigDecimal invoiceBalance){
        try {
            CustomerValidation.validateBalance(customer,invoiceBalance);
        }catch (InsufficientBalanceException e){
            System.out.println(e.getMessage());
            return false;
        }
        customer.setBalance(customer.getBalance().subtract(invoiceBalance));
        return true;
    }

    public void refundToWallet(Customer customer,BigDecimal balance){
        customer.setBalance(customer.getBalance().add(balance));
    }

    public void displayCustomer(Customer customer){
        System.out.println("Customer name: "+customer.getName());
        System.out.println("Customer balance: "+customer.getBalance());
        System.out.println("Customer completed orders count = "+customer.getCompletedOrders());
        System.out.println("Customer loyalty tier: "+customer.getLoyaltyTier());
        customer.getAddresses().forEach(System.out::println);
    }

    public void setCompletedOrderToCustomer(Customer customer,Order order){
        customer.setCompletedOrders(customer.getCompletedOrders()+1);
        customer.setLastOrder(order.getPlacedAt());
        updateLoyaltyTier(customer);
    }

    public Customer findCustomerById(){
        int customerId= InputValidation.setInt("enter customer ID: ");
        return customerRepository.findById(customerId).orElseThrow(()->new RuntimeException("Customer not found"));
    }


}
