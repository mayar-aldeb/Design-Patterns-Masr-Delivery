package validation;

import exception.InsufficientBalanceException;
import model.Customer;

import java.math.BigDecimal;

public class CustomerValidation {

    public static boolean isValidPhone(String phone){
        return phone.matches("01[0125]\\d{8}");
    }

    public static void validateBalance(Customer customer, BigDecimal invoiceBalance){
        if(customer.getBalance().compareTo(invoiceBalance)<0)
            throw new InsufficientBalanceException("Insufficient Balance.");
    }

}
