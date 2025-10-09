package sv.edu.udb.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id","program_id","periodo"})
)
public class Enrollment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Student student;

    @ManyToOne(optional = false)
    private Program program;

    @Column(nullable = false)
    private String periodo; // ej. 2025-I

    @Enumerated(EnumType.STRING)
    private Estado estado; // INSCRITO, EN_ESPERA, CANCELADO

    private LocalDateTime fecha;

    public enum Estado { INSCRITO, EN_ESPERA, CANCELADO }
}
