package hu.gyoridenes.alkatresz_raktar.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    // Itt tároljuk, hogy az illető sima vásárló (USER) vagy (ADMIN)
    private String role;

    // Itt tároljuk a Google kép linkjét VAGY a feltöltött kép adatait (Base64)
    @Column(columnDefinition = "TEXT")
    private String avatarUrl;
}
