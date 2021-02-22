package pl.khamul.handworkshop.entity;


import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
public class OrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private LocalDateTime orderDate;

    @OneToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private ShoppingCart productList;

    @OneToOne
    private Adress adress;

   private Enum status;

   private double paid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public ShoppingCart getProductList() {
        return productList;
    }

    public void setProductList(ShoppingCart productList) {
        this.productList = productList;
    }

    public Adress getAdres() {
        return adress;
    }

    public void setAdres(Adress adress) {
        this.adress = adress;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }
}
