package org.route.validation;

import org.route.exception.ClosedRestaurantException;
import org.route.exception.StockShortageException;
import org.route.exception.UnavailableItemException;
import org.route.model.Items.MenuItem;
import org.route.model.Restaurant;

public class RestaurantValidation {

    public static void validateRate(double rate){
        if(rate >= 0 && rate <= 5)
            return;
        throw  new UnavailableItemException("Invalid restaurant rate.");
    }

    public static void isOpen(Restaurant restaurant) {
        if(!restaurant.isOpen())
            throw new ClosedRestaurantException("Restaurant "+restaurant.getName()+" is closed.");
    }

    public static void validateStock(MenuItem item,int quantity){
        if(item.getStock()<quantity)
            throw new StockShortageException("Item doesn't have enough stock.");
    }

    public static void validateItem(MenuItem item){
        if(!item.isAvailable())
            throw new UnavailableItemException("item is not available.");
    }

}
