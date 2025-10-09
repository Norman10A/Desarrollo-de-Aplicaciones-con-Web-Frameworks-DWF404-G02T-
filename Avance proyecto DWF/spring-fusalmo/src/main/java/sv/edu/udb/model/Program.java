package sv.edu.udb.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Program {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 2000)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    private Academy academy;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    private Integer cupoMax;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    public enum Estado { ACTIVO, INACTIVO }
}

