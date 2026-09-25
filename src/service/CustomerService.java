package service;

import enums.CustomerLoyaltyTier;
import exception.InsufficientBalanceException;
import model.Customer;
import model.Address;
import model.Order;
import model.Restaurant;
import validation.CustomerValidation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CustomerService {

    public void updateLoyaltyTier(Customer customer){
        if(customer.getCompletedOrders()>30)
            customer.setLoyaltyTier(CustomerLoyaltyTier.GOLD);
        else if(customer.getCompletedOrders()>9)
            customer.setLoyaltyTier(CustomerLoyaltyTier.SILVER);
    }

    public Customer createCustomer(String name, String phone, String district, String details, BigDecimal balance, List<Restaurant> search){
        Address address=new Address(district,details);
        if(!CustomerValidation.isValidPhone(phone)) {
            System.out.println("invalid phone number.");
            return null;
        }
        if(balance.compareTo(BigDecimal.valueOf(0))<0) {
            System.out.println("invalid balance: ");
            return null;
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

    public Customer findCustomerById(Map<Integer, Customer> customers,int id){
        Customer customer=customers.get(id);
        if(customer==null)
            System.out.println("Customer not found.");
        return customer;
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


}
