package model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderInvoice {
    public static final BigDecimal DELIVERY_BASE=new BigDecimal(15);
    public static final BigDecimal DELIVERY_FEE_PER_KM =new BigDecimal(3);
    public static final BigDecimal SERVICE_RATE=new BigDecimal("0.1");

    private int orderId;
    private BigDecimal subTotal;
    private BigDecimal deliveryFee;
    private BigDecimal serviceFee;
    private LocalDateTime orderDate;
    private boolean payed;
    private boolean promotion;


    public OrderInvoice( int orderId,LocalDateTime orderDate) {
        this.orderId=orderId;
        this.orderDate=orderDate;
        promotion=false;
        payed=false;
    }


}
