package org.route.repository;

import org.route.model.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class RestaurantRepository {
    private final Map<Integer, Restaurant> restaurants= new HashMap<>();



    public Optional<Restaurant> findById(int id) {
        return Optional.ofNullable(restaurants.get(id));
    }
    public List<Restaurant> findAll() {
        return restaurants.values().stream().toList();
    }

    public void save(Restaurant restaurant) {
        restaurants.put(restaurant.getId(), restaurant);
    }

    public Optional<Restaurant>  delete(int id) {
        return Optional.ofNullable(restaurants.remove(id));
    }

    public List<String> findAllCuisines() {
        return restaurants.values().stream().flatMap(restaurant -> restaurant.getCuisines().stream()).distinct().toList();
    }


}
