package pl.khamul.handworkshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.khamul.handworkshop.entity.UserDetails;

public interface UserDetailsRepo extends JpaRepository<UserDetails, Long> {
}
