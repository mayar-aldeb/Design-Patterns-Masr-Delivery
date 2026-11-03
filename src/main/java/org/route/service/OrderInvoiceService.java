package org.route.service;

import lombok.RequiredArgsConstructor;
import org.route.model.Customer;
import org.route.model.Items.OrderItem;
import org.route.model.Order;
import org.route.model.OrderInvoice;
import org.route.model.orderPromotion.Promotion;
import org.springframework.stereotype.Service;
import org.route.repository.OrderInvoiceRepository;
import org.route.repository.PromotionRepository;
import org.route.validation.InputValidation;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderInvoiceService {
    private final OrderService orderService;
    private final OrderInvoiceRepository invoiceRepository;
    private final PromotionRepository promotionRepository;


    public OrderInvoice createInvoice(Order order){
        return new OrderInvoice(order.getId(),order.getPlacedAt());
    }

    private void calculateSubTotal(Order order, OrderInvoice invoice){
        BigDecimal total =new BigDecimal("0");
        for (OrderItem item:order.getItems().values()) {
            total = total.add(orderService.calculateSubTotal(item));
        }
        invoice.setSubTotal(total);
    }

    private void calculateDeliveryFee(OrderInvoice invoice, int distance, Customer customer){
        distance-=3;
        invoice.setDeliveryFee(
                OrderInvoice.DELIVERY_BASE.add(
                        OrderInvoice.DELIVERY_FEE_PER_KM.multiply(BigDecimal.valueOf(distance))));

        BigDecimal loyaltyDiscount=customer.getLoyaltyTier().getDeliveryFeeDiscount().
                                            multiply(invoice.getDeliveryFee());
        invoice.setDeliveryFee(invoice.getDeliveryFee().
                subtract(loyaltyDiscount));
    }

    private void calculateServiceFee(OrderInvoice invoice){
        invoice.setServiceFee(invoice.getSubTotal().multiply(OrderInvoice.SERVICE_RATE));
    }

    public boolean applyPromotion(Promotion promotion,OrderInvoice invoice,Customer customer,Order order){
        if(invoice.isPromotion())
            return false;
        return promotion.applyPromotion(invoice,customer,order);
    }

    public BigDecimal calculateTotal(OrderInvoice invoice){
        return invoice.getSubTotal().add(invoice.getDeliveryFee()).add(invoice.getServiceFee());
    }

    public void displayInvoice(OrderInvoice invoice){
        System.out.println("Subtotal = "+invoice.getSubTotal());
        System.out.println("Delivery fee = "+invoice.getDeliveryFee());
        System.out.println("Service fee = "+invoice.getServiceFee());
        System.out.println("invoice total = "+calculateTotal(invoice));
    }

    public void calculateInvoice(Order order, OrderInvoice invoice, int distance, Customer customer){
        calculateSubTotal(order,invoice);
        calculateDeliveryFee(invoice,distance,customer);
        calculateServiceFee(invoice);

    }

    public OrderInvoice findInvoiceById(int id){
        return invoiceRepository.findById(id).orElseThrow(()->new RuntimeException("there is no invoice for this order."));
    }

    public void applyPromotion(OrderInvoice invoice, Customer customer, Order order){
        int choice =InputValidation.setInt("do you want to add promotion?\n1.Yes\n2.No\nEnter your choice: ");
        if(choice!=1)
            return;
        String promotionCode=InputValidation.setValidString("Enter Promotion Code: ");
        Promotion promotion=promotionRepository.getPromotion(promotionCode.toLowerCase()).orElseThrow(()->new RuntimeException("invalid promotion code"));
        if(applyPromotion(promotion,invoice,customer,order)){
            System.out.println("Promotion applied successfully.");
            invoice.setPromotion(true);
        }
    }

    public void setInvoice(Order order,Customer customer){
        OrderInvoice invoice=createInvoice(order);
        int distance=InputValidation.setInt("enter distance between restaurant and delivery address: ");
        calculateInvoice(order,invoice,distance,customer);
        invoiceRepository.save(invoice);
        applyPromotion(invoice,customer,order);
        order.setTotalPrice(calculateTotal(invoice));
        displayInvoice(invoice);
    }

}
