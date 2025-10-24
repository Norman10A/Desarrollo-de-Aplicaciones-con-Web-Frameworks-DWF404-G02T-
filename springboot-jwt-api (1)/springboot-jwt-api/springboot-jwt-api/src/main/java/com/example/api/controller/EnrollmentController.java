package com.example.api.controller;

import com.example.api.entity.Enrollment;
import com.example.api.entity.Course;
import com.example.api.repository.EnrollmentRepository;
import com.example.api.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepo;

    @Autowired
    private CourseRepository courseRepo;

    @PostMapping
    public ResponseEntity<?> createEnrollment(@RequestBody Map<String, Object> requestData) {
        try {
            // Validar campos obligatorios
            Long courseId = getLongValue(requestData, "courseId");
            String studentName = getStringValue(requestData, "studentName");
            String studentLastName = getStringValue(requestData, "studentLastName");
            Integer studentAge = getIntegerValue(requestData, "studentAge");
            String parentName = getStringValue(requestData, "parentName");
            String parentDui = getStringValue(requestData, "parentDui");
            String email = getStringValue(requestData, "email");
            String phone = getStringValue(requestData, "phone");
            String message = getStringValue(requestData, "message");
            String branch = getStringValue(requestData, "branch");

            // Validaciones
            if (courseId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "ID del curso es requerido"));
            }
            if (branch == null || !List.of("Fusalmo Soyapango", "Fusalmo Santa Ana", "Fusalmo San Miguel").contains(branch)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Sede inválida"));
            }

            // Buscar curso
            Optional<Course> courseOpt = courseRepo.findById(courseId);
            if (courseOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Curso no encontrado"));
            }
            Course course = courseOpt.get();

            // Validar cupo
            if (course.getEnrolled() >= course.getCapacity()) {
                return ResponseEntity.badRequest().body(Map.of("error", "No hay cupo disponible"));
            }

            // Validar DUI único por curso
            if (enrollmentRepo.existsByParentDuiAndCourseId(parentDui, courseId)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Este DUI ya está registrado en este curso"));
            }

            // Crear inscripción
            Enrollment enrollment = new Enrollment();
            enrollment.setCourse(course);
            enrollment.setStudentName(studentName);
            enrollment.setStudentLastName(studentLastName);
            enrollment.setStudentAge(studentAge);
            enrollment.setParentName(parentName);
            enrollment.setParentDui(parentDui);
            enrollment.setEmail(email);
            enrollment.setPhone(phone);
            enrollment.setMessage(message);
            enrollment.setBranch(branch); // ✅ Asignar sede

            // Actualizar cupo
            course.setEnrolled(course.getEnrolled() + 1);
            courseRepo.save(course);

            // Guardar inscripción
            Enrollment savedEnrollment = enrollmentRepo.save(enrollment);
            return ResponseEntity.ok(savedEnrollment);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Error al procesar la inscripción: " + e.getMessage()));
        }
    }

    // Métodos auxiliares
    private Long getLongValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Integer) return ((Integer) value).longValue();
        if (value instanceof Long) return (Long) value;
        return null;
    }

    private Integer getIntegerValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Double) return ((Double) value).intValue();
        return null;
    }

    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value == null ? null : value.toString().trim();
    }

    // Otros endpoints
    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepo.findAll();
    }

    @GetMapping("/course/{courseId}")
    public List<Enrollment> getEnrollmentsByCourse(@PathVariable Long courseId) {
        return enrollmentRepo.findByCourseId(courseId);
    }
}