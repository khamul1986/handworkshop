package pl.khamul.handworkshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.khamul.handworkshop.entity.Role;



public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
