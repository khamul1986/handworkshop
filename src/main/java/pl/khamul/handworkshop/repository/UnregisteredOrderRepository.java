package pl.khamul.handworkshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.khamul.handworkshop.entity.UnregisteredOrder;

public interface UnregisteredOrderRepository extends JpaRepository<UnregisteredOrder, Long> {
}
