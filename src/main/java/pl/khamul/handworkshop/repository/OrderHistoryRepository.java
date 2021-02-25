package pl.khamul.handworkshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.khamul.handworkshop.entity.OrderHistory;

import java.util.List;


public interface OrderHistoryRepository extends JpaRepository<OrderHistory,Long> {


    @Query("SELECT a FROM User u join u.order a Where u.id = :id")
    List<OrderHistory> findHistoryByUserId(Long id);
}
