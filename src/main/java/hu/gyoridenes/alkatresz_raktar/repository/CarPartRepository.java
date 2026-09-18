package hu.gyoridenes.alkatresz_raktar.repository;

import hu.gyoridenes.alkatresz_raktar.model.CarPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarPartRepository extends JpaRepository<CarPart, Long> {
}