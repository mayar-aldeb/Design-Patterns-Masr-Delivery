package org.route.model.Items;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;


@Getter
@Setter
public abstract class MenuItem {
    private final int id;
    private String name;
    private BigDecimal price;
    private String cuisine;
    private int preparationTime;
    private int stock;
    private boolean available;

    public MenuItem(int id, String name, String cuisine, BigDecimal price, int preparationTime,int stock) {
        this.id = id;
        this.name = name;
        this.cuisine = cuisine;
        this.price=price;
        this.preparationTime = preparationTime;
        this.available = true;
        this.stock=stock;
    }

    public abstract BigDecimal calculatePrice();

    public void displayItem(){
        System.out.println(id+"- "+name+": "+price+" -> "
                +(available?"Available.":"Not Available.")+
                " -> stock = "+stock);
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", cuisine='" + cuisine + '\'' +
                ", preparationTime=" + preparationTime +
                ", stock=" + stock +
                ", available=" + available +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem item = (MenuItem) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
