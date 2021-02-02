package pl.khamul.handworkshop.entity;


import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userName;
    @Email
    private String email;
    private String password;

    @OneToMany
    private List<Adres> adres;
   @OneToMany
    private List<OrderHistory> order;

    public User(Long id, String userName, @Email String email, String password, List<Adres> adres, List<OrderHistory> order) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.adres = adres;
        this.order = order;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Adres> getAdres() {
        return adres;
    }

    public void setAdres(List<Adres> adres) {
        this.adres = adres;
    }

    public List<OrderHistory> getOrder() {
        return order;
    }

    public void setOrder(List<OrderHistory> order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", adres=" + adres +
                ", order=" + order +
                '}';
    }
}
