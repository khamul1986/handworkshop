package pl.khamul.handworkshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.khamul.handworkshop.entity.Product;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository <Product, Long>{
}
