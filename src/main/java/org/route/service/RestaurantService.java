package org.route.service;

import org.route.exception.ClosedRestaurantException;
import org.route.exception.UnavailableItemException;
import lombok.RequiredArgsConstructor;
import org.route.model.*;
import org.route.model.Items.Combo;
import org.route.model.Items.ItemFactory;
import org.route.model.Items.MenuItem;
import org.route.model.restaurntFilterObserver.FilterFactory;
import org.route.model.restaurntFilterObserver.RestaurantFilterListener;
import org.springframework.stereotype.Service;
import org.route.repository.RestaurantRepository;
import org.route.validation.InputValidation;
import org.route.validation.RestaurantValidation;

import java.math.BigDecimal;
import java.util.*;


@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final CustomerService customerService;
    private final RestaurantRepository restaurantRepository;


    public Restaurant findRestaurantById(){

        int restaurantId = InputValidation.setInt("enter restaurant ID: ");
        return restaurantRepository.findById(restaurantId).orElseThrow(()-> new RuntimeException("invalid ID."));
    }



    public MenuItem createItem(int type, int id, String name, String cuisine, BigDecimal price, int preparationTime,int stock){
        return ItemFactory.create(type,id,name,cuisine,price,preparationTime,stock);
    }

    public void recordAuditLog(Order order){
        System.out.println("record new order status "+order.getOrderStatus());
    }

    public void addItemToCombo(MenuItem item, Combo combo){
        combo.setPrice(combo.getPrice().add(item.getPrice()));
        combo.setPreparationTime(combo.getPreparationTime()+item.getPreparationTime());
        combo.getComboItems().put(item.getId(),item);
    }

    public void removeItemFromCombo(MenuItem item, Combo combo){
        combo.setPrice(combo.getPrice().subtract(item.getPrice()));
        combo.setPreparationTime(combo.getPreparationTime()-item.getPreparationTime());
        combo.getComboItems().remove(item.getId());
    }

    public int setRestaurantFilters(List<RestaurantFilterListener> filters){
        System.out.println("""
                filter criteria:\s
                1- cuisine.
                2- district.
                3- price ceiling.
                4- rating.
                enter 5 if you don't want filter.""");
        int choice = InputValidation.setFilterOption("enter filter number: ");
        FilterFactory.setParameters(choice);
        try {
            filters.add(FilterFactory.create(choice));
        }catch (IllegalArgumentException e){
            return choice;
        }
        return choice;
    }

    public Restaurant findRestaurantById(Customer customer){
            int restaurantId=InputValidation.setInt("enter Restaurant ID: ");
        Restaurant restaurant=customerService.findInSearch(customer,restaurantId);
        if(restaurant==null){
            restaurant =restaurantRepository.findById(restaurantId).orElseThrow(()->new RuntimeException("Restaurant not found"));
            customerService.addToSearch(customer,restaurant);
        }
        try {
            RestaurantValidation.isOpen(restaurant);
        }catch (ClosedRestaurantException e){
            System.out.println(e.getMessage());
            return null;
        }
        return restaurant;
    }

    public Restaurant createRestaurant(String name, String district, double rate, Map<Integer,MenuItem> items, Set<String> cuisines){
        RestaurantValidation.validateRate(rate);
        return new Restaurant(name,district,rate,items,cuisines);

    }

    public int setItemType(){
        System.out.println("""
                1- Combo.
                2- Standard Item.
                3- Weighted Item.""");
        System.out.print("Enter your choice: ");
        int choice=0;
        while (choice<1||choice>3){
            choice= InputValidation.setInt("enter valid choice: ");
        }
        return choice;
    }

    public void addItem(Restaurant restaurant,int type, String name, String cuisine, BigDecimal price, int preparationTime,int stock){
        restaurant.setItemCount(restaurant.getItemCount()+1);
        restaurant.getCuisines().add(cuisine);
        MenuItem item=createItem(type, restaurant.getItemCount(), name, cuisine, price, preparationTime,stock);
        restaurant.setItem(item);
    }

    public void displayRestaurant(Restaurant restaurant){
        System.out.println("Restaurant ID: "+restaurant.getId());
        System.out.println("Restaurant Name: "+restaurant.getName());
        System.out.println("Restaurant Rating: "+restaurant.getRate());
        System.out.println("Restaurant Status: "+(restaurant.isOpen()?"Open":"Close"));
        System.out.println("Restaurant district: "+restaurant.getDistrict());
        displayRestaurantItems(restaurant);
        displayRestaurantCuisines(restaurant);
    }


    public void displayRestaurantItems(Restaurant restaurant){
        if(restaurant.getItems().isEmpty()){
            System.out.println("no items added yet.");
            return;
        }
        System.out.println("Menu Items: ");
        restaurant.getItems().values().forEach(MenuItem::displayItem);
    }

    public Restaurant viewRestaurantMenu(Customer customer){
        Restaurant restaurant=findRestaurantById(customer);
        if(restaurant==null)
            return null;
        displayRestaurantItems(restaurant);
        return restaurant;
    }

    public boolean isUnValidItem(MenuItem item){
        try {
            RestaurantValidation.validateItem(item);
        }catch (UnavailableItemException e){
            System.out.println(e.getMessage());
            return true;
        }
        return false;
    }

    public void displayRestaurantCuisines(Restaurant restaurant){
        if(restaurant.getCuisines().isEmpty()){
            System.out.println("no cuisines added yet.");
            return;
        }
        System.out.println("cuisines: ");
        restaurant.getCuisines().forEach(System.out::println);
    }

    public MenuItem findItemById(Map<Integer,MenuItem> items){
        int itemId=InputValidation.setInt("enter item ID: ");
        MenuItem item=items.get(itemId);
        if(item==null) {
            System.out.println("invalid ID.");
            return null;
        }
        return item;
    }

    public Combo findComboById(Map<Integer,MenuItem> items){
        int comboItem=InputValidation.setInt("enter combo ID: ");
        MenuItem item =items.get(comboItem);
        if(item ==null){
            System.out.println("invalid ID.");
            return null;
        }
        if(item instanceof Combo combo){
            return combo;
        }
        System.out.println("this item is not combo.");
        return null;
    }

}
