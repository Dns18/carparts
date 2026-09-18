package hu.gyoridenes.alkatresz_raktar.repository;

import hu.gyoridenes.alkatresz_raktar.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    // Ez a metódus megkeresi a usert név alapján a bejelentkezéshez
    Optional<AppUser> findByUsername(String username);
}