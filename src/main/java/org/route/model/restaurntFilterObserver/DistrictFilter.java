package org.route.model.restaurntFilterObserver;

import org.route.model.Restaurant;

import java.util.List;

public class DistrictFilter implements RestaurantFilterListener {
    private final String district;

    public DistrictFilter(String district) {
        this.district = district;
    }

    @Override
    public List<Restaurant> filterRestaurants(List<Restaurant> restaurants) {
        return restaurants.stream().
                filter(restaurant -> restaurant.getDistrict().equals(district)).
                toList();
    }
}
