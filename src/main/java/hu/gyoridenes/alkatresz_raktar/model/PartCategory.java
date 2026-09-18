package hu.gyoridenes.alkatresz_raktar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class PartCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String shelfLocation;

    // 1:N kapcsolat a kategória és az alkatrészek között
    @OneToMany(mappedBy = "category")
    @JsonIgnore // Fontos! Ne legyen végtelen ciklus a JSON generálásnál
    private List<CarPart> parts;
}
