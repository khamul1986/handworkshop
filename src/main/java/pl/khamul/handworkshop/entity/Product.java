package pl.khamul.handworkshop.entity;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Range(min=0, message = "naprawdę dopłacasz że ktoś to weźmie?")
    private BigDecimal price;


    @Range(min=0, message = "mniej niż zero to raczej ciężko")
    private int storagequantity;


    //zdjęcia produktu w formie kolekcji linków(set?)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStoragequantity() {
        return storagequantity;
    }

    public void setStoragequantity(int quantity) {
        this.storagequantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", storagequantity=" + storagequantity +
                '}';
    }
}
