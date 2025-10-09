package sv.edu.udb.service;

import sv.edu.udb.model.Enrollment;
import sv.edu.udb.model.Program;
import sv.edu.udb.repository.EnrollmentRepository;
import sv.edu.udb.repository.ProgramRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enr;
    private final ProgramRepository prog;

    @Transactional
    public Enrollment enroll(Long studentId, Long programId, String periodo) {
        var program = prog.findById(programId).orElseThrow();

        if (enr.existsByStudent_IdAndProgram_IdAndPeriodo(studentId, programId, periodo)) {
            throw new IllegalStateException("Ya existe una inscripción para este período.");
        }

        long inscritos = enr.countByProgram_IdAndEstado(programId, Enrollment.Estado.INSCRITO);
        var estado = (program.getCupoMax() != null && inscritos >= program.getCupoMax())
                ? Enrollment.Estado.EN_ESPERA
                : Enrollment.Estado.INSCRITO;

        var e = Enrollment.builder()
                .student(sv.edu.udb.model.Student.builder().id(studentId).build())
                .program(Program.builder().id(programId).build())
                .periodo(periodo)
                .estado(estado)
                .fecha(LocalDateTime.now())
                .build();

        return enr.save(e);
    }

    @Transactional
    public void cancelar(Long enrollmentId) {
        var e = enr.findById(enrollmentId).orElseThrow();
        e.setEstado(Enrollment.Estado.CANCELADO);
        enr.save(e);

        enr.findFirstByProgram_IdAndEstadoOrderByFechaAsc(
                e.getProgram().getId(), Enrollment.Estado.EN_ESPERA
        ).ifPresent(waiting -> {
            waiting.setEstado(Enrollment.Estado.INSCRITO);
            enr.save(waiting);
        });
    }
}

