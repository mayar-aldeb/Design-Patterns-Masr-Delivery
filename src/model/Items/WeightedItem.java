package model.Items;

import java.math.BigDecimal;

public class WeightedItem extends MenuItem {

    public WeightedItem(int id, String name, String cuisine, BigDecimal price, int preparationTime,int stock) {
        super(id, name, cuisine, price, preparationTime,stock);
    }

    @Override
    public BigDecimal calculatePrice() {
        return getPrice();
    }
}
