package org.route.validation;

import org.route.exception.InsufficientBalanceException;
import org.route.model.Customer;

import java.math.BigDecimal;

public class CustomerValidation {

    public static void isValidPhone(String phone){
        if (!phone.matches("01[0125]\\d{8}"))
            throw new RuntimeException("Invalid phone number");
    }

    public static void validateBalance(Customer customer, BigDecimal invoiceBalance){
        if(customer.getBalance().compareTo(invoiceBalance)<0)
            throw new InsufficientBalanceException("Insufficient Balance.");
    }

}
