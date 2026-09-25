package model;

import enums.CustomerLoyaltyTier;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Customer {
    private static int count=0;
    private final int id;
    private String name;
    private String phone;
    private List<Address> addresses;
    private BigDecimal balance;
    private int completedOrders;
    private CustomerLoyaltyTier loyaltyTier;
    private LocalDateTime lastOrder;
    private List<Restaurant> search;

    public Customer(String name, String phone, List<Address> addresses,Address address, BigDecimal balance,List<Restaurant> search) {
        this.id = ++count;
        this.name = name;
        this.phone = phone;
        this.addresses=addresses;
        this.addresses.add(address);
        this.lastOrder=null;
        this.balance = balance;
        this.search=search;
        this.loyaltyTier=CustomerLoyaltyTier.BRONZE;
    }


}
