package sv.edu.udb.repository;

import sv.edu.udb.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    List<Program> findByCategory_NombreIgnoreCase(String categoria);
    List<Program> findByAcademy_NombreIgnoreCase(String academia);
    List<Program> findByEstado(Program.Estado estado);
}

