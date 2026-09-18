package hu.gyoridenes.alkatresz_raktar.repository;

import hu.gyoridenes.alkatresz_raktar.model.AppUser;
import hu.gyoridenes.alkatresz_raktar.model.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    // Dátum szerint csökkenő sorrendben (legújabb legelöl) adja vissza
    List<OrderHistory> findByUserOrderByOrderDateDesc(AppUser user);
}