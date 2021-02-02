package pl.khamul.handworkshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.khamul.handworkshop.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findFirstByEmail(String email);
}
