package pl.khamul.handworkshop.entity;



import org.hibernate.annotations.Cascade;

import org.springframework.stereotype.Component;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Component
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<CartItem> items = new ArrayList<>();


    public ShoppingCart(ArrayList<CartItem> items) {
        this.items = items;
    }

    public ShoppingCart(){}

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "cart=" + items +
                '}';
    }
}
