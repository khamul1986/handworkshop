package pl.khamul.handworkshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.khamul.handworkshop.entity.ReservationItem;

public interface ReservationRepo extends JpaRepository <ReservationItem, Long> {



    ReservationItem findByProductId(Long id);
}
