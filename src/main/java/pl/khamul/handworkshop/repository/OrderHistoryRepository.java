package pl.khamul.handworkshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.khamul.handworkshop.entity.OrderHistory;


public interface OrderHistoryRepository extends JpaRepository<OrderHistory,Long> {
}
