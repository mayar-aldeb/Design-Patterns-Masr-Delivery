package org.route.model;

import lombok.Getter;
import lombok.Setter;
import org.route.model.Items.MenuItem;

import java.util.*;


@Getter
@Setter
public class Restaurant  {
    private static int count=0;
    private final int id;
    private int itemCount;
    private String name;
    private String district;
    private double rate;
    private boolean open;
    private int completedOrders;
    private Map<Integer,MenuItem> items;
    private Set<String> cuisines;


    public Restaurant(String name, String district, double rate,Map<Integer,MenuItem> items,Set<String> cuisines) {
        this.id = ++count;
        this.name = name;
        this.district = district;
        this.open = true;
        this.rate = rate;
        this.items =items;
        this.cuisines=cuisines;
        itemCount=0;
        completedOrders=0;
    }

    public void setItem(MenuItem item) {
        this.items.put(item.getId(),item);
    }



}
