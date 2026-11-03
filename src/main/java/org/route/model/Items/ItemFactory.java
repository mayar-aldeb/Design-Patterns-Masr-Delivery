package org.route.model.Items;

import java.math.BigDecimal;
import java.util.HashMap;

public class ItemFactory {
    public static MenuItem create(int type, int id, String name, String cuisine, BigDecimal price, int preparationTime,int stock){
        return switch (type){
            case 1->
                new Combo(id,name,cuisine,BigDecimal.valueOf(0),0,new HashMap<>(),stock);
            case 2->
                new StandardItem(id,name,cuisine,price,preparationTime,stock);
            case 3->
                new WeightedItem(id,name,cuisine,price,preparationTime,stock);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
