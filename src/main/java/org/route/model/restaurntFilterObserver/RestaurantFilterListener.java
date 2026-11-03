package org.route.model.restaurntFilterObserver;

import org.route.model.Restaurant;

import java.util.List;

public interface RestaurantFilterListener {
    List<Restaurant> filterRestaurants(List<Restaurant> restaurants);
}
