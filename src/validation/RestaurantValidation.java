package validation;

import exception.ClosedRestaurantException;
import exception.StockShortageException;
import exception.UnavailableItemException;
import model.Items.MenuItem;
import model.Restaurant;

public class RestaurantValidation {

    public static boolean validateRate(double rate){
        return rate >= 0 && rate <= 5;
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
