package org.route.model.restaurntFilterObserver;

import org.route.model.Restaurant;

import java.util.List;


public class RateFilter implements RestaurantFilterListener{
    private final double rate;

    public RateFilter(double rate) {
        this.rate = rate;
    }

    @Override
    public List<Restaurant> filterRestaurants(List<Restaurant> restaurants) {
        return restaurants.stream().
                filter(restaurant -> restaurant.getRate()>=rate).
                toList();
    }
}
