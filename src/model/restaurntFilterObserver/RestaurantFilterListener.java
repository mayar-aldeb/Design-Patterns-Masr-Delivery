package model.restaurntFilterObserver;

import model.Restaurant;

import java.util.List;

public interface RestaurantFilterListener {
    List<Restaurant> filterRestaurants(List<Restaurant> restaurants);
}
