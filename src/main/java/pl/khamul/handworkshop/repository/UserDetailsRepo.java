package pl.khamul.handworkshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.khamul.handworkshop.entity.UserNames;

public interface UserDetailsRepo extends JpaRepository<UserNames, Long> {
}
