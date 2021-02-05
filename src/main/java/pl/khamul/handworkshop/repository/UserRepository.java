package pl.khamul.handworkshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.khamul.handworkshop.entity.User;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByEmail(String email);

    @Query("SELECT email FROM User")
    List <String> emailList();
}
