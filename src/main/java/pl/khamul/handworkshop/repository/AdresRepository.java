package pl.khamul.handworkshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import pl.khamul.handworkshop.entity.Adress;

import java.util.List;


public interface AdresRepository extends JpaRepository <Adress, Long> {



    @Query("SELECT a FROM User u join u.adres a Where u.id = :id")
    List findAllByUserId(Long id);
}
