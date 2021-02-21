package pl.khamul.handworkshop.entity;




import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userName;


    @Column(unique = true)
    private String email;

    private String password;

    private String role;

    @OneToMany
    private List<Adres> adres;

    @OneToMany
    private List<OrderHistory> order;

   @OneToOne
   private UserNames details;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public UserNames getDetails() {
        return details;
    }

    public void setDetails(UserNames details) {
        this.details = details;
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
