package sv.edu.udb.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) private String nombres;
    @Column(nullable = false) private String apellidos;

    private LocalDate fechaNacimiento;
    @Column(length = 1) private String sexo; // M/F
    private String docIdentidad;
    private String telefono;
}
