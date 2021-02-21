package pl.khamul.handworkshop.entity;


import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.math.BigDecimal;
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
    private Adres adres;

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

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
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
