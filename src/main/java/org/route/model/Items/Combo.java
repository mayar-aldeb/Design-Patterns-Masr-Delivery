package org.route.model.Items;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
public class Combo  extends MenuItem{

    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.1");
    private final Map<Integer,MenuItem> comboItems;

    public Combo(int id, String name, String cuisine,BigDecimal price, int preparationTime, Map<Integer,MenuItem> comboItems,int stock) {
        super(id, name, cuisine, price, preparationTime,stock);
        this.comboItems = comboItems;
    }


    @Override
    public BigDecimal calculatePrice() {
        BigDecimal discount=getPrice().multiply(DISCOUNT_RATE);
        return getPrice().subtract(discount);
    }

    @Override
    public void displayItem(){
        super.displayItem();
        System.out.println("Combo Items: ");
        comboItems.values().forEach(MenuItem::displayItem);
    }
}
