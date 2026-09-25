package model.Items;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class OrderItem {
    private MenuItem item;
    private int quantity;

    public OrderItem(int quantity, MenuItem item) {
        this.quantity = quantity;
        this.item=item;
    }



}
