package pl.khamul.handworkshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.khamul.handworkshop.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
