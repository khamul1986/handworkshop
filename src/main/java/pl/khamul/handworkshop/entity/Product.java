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


    @Range(min=0, message = "brak towaru w magazynie")
    private int storagequantity;

    private String description;

    private String size;

    //zdjęcia produktu w formie kolekcji linków(set?) --> id i nazwa w bazie

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", storagequantity=" + storagequantity +
                ", description='" + description + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
