package model.restaurntFilterObserver;

import model.Items.MenuItem;
import model.Restaurant;

import java.math.BigDecimal;
import java.util.List;


public class PriceCeilingFilter implements RestaurantFilterListener {
    private final BigDecimal priceCeiling;

    public PriceCeilingFilter(BigDecimal priceCeiling) {
        this.priceCeiling = priceCeiling;
    }

    @Override
    public List<Restaurant> filterRestaurants(List<Restaurant> restaurants) {
        return restaurants.stream().
                filter(restaurant ->{
                   BigDecimal restaurantPriceCeiling= restaurant.getItems().values().stream().
                           map(MenuItem::getPrice).
                           max(BigDecimal::compareTo).
                           orElse(BigDecimal.valueOf(0));
                   return priceCeiling.compareTo(restaurantPriceCeiling) > 0;
                }).
                toList();

    }
}
