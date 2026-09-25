package model.restaurntFilterObserver;

import model.Restaurant;

import java.util.List;



public class CuisineFilter implements RestaurantFilterListener {
    private final String cuisine;

    public CuisineFilter(String cuisine) {
        this.cuisine = cuisine;
    }

    @Override
    public List<Restaurant> filterRestaurants(List<Restaurant> restaurants) {
        return  restaurants.stream().
                filter(restaurant -> restaurant.getCuisines().contains(cuisine)).
                toList();
    }
}
