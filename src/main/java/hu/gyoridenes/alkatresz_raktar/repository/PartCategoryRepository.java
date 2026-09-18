package hu.gyoridenes.alkatresz_raktar.repository;

import hu.gyoridenes.alkatresz_raktar.model.PartCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartCategoryRepository extends JpaRepository<PartCategory, Long> {
}