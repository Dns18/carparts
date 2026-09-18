package hu.gyoridenes.alkatresz_raktar.repository;

import hu.gyoridenes.alkatresz_raktar.model.AppUser;
import hu.gyoridenes.alkatresz_raktar.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(AppUser user);
    void deleteByUser(AppUser user);
}