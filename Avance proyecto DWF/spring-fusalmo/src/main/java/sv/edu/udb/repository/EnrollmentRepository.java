package sv.edu.udb.repository;

import sv.edu.udb.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findFirstByProgram_IdAndEstadoOrderByFechaAsc(
            Long programId, Enrollment.Estado estado);

    boolean existsByStudent_IdAndProgram_IdAndPeriodo(
            Long studentId, Long programId, String periodo);

    long countByProgram_IdAndEstado(Long programId, Enrollment.Estado estado);
}
