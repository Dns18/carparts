package hu.gyoridenes.alkatresz_raktar.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CarPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String partNumber;
    private String name;
    private Integer price;
    private Integer stockQuantity;

    // A külső kulcs a kategóriához
    @ManyToOne
    @JoinColumn(name = "category_id")
    private PartCategory category;
}