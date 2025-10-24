package com.example.api.controller;

import com.example.api.entity.Course;
import com.example.api.entity.Category;
import com.example.api.repository.CourseRepository;
import com.example.api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @GetMapping
    public List<Course> getAll() {
        System.out.println("Obteniendo todos los cursos");
        List<Course> courses = courseRepo.findAll();


        courses.forEach(course -> {
            if (course.getCategory() != null) {
                course.getCategory().getName(); // Dispara la inicialización
            }
        });

        System.out.println("Cursos encontrados: " + courses.size());
        return courses;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getById(@PathVariable Long id) {
        System.out.println("Buscando curso con ID: " + id);
        Optional<Course> courseOpt = courseRepo.findById(id);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();

            if (course.getCategory() != null) {
                course.getCategory().getName();
            }
            System.out.println("Curso encontrado: " + course.getTitle());
            return ResponseEntity.ok(course);
        } else {
            System.out.println("Curso no encontrado con ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Course course) {
        try {
            System.out.println("=== INICIANDO CREACIÓN DE CURSO ===");
            System.out.println("Título recibido: " + course.getTitle());
            System.out.println("Categoría recibida: " + course.getCategory());

            if (course.getCategory() == null || course.getCategory().getId() == null) {
                System.out.println("ERROR: Categoría es NULL o ID es NULL");
                return ResponseEntity.badRequest().body("Categoría es requerida");
            }

            Long categoryId = course.getCategory().getId();
            System.out.println("Buscando categoría con ID: " + categoryId);

            Optional<Category> categoryOpt = categoryRepo.findById(categoryId);
            if (categoryOpt.isEmpty()) {
                System.out.println("ERROR: Categoría no encontrada con ID: " + categoryId);
                return ResponseEntity.badRequest().body("Categoría no encontrada con ID: " + categoryId);
            }

            Category category = categoryOpt.get();
            System.out.println("Categoría encontrada: " + category.getName() + " (ID: " + category.getId() + ")");
            course.setCategory(category);

            if (course.getEnrolled() == null) course.setEnrolled(0);
            if (course.getCapacity() == null) course.setCapacity(20);
            if (course.getImage() == null || course.getImage().isEmpty()) course.setImage("assets/default-course.jpg");
            if (course.getShortDescription() == null) course.setShortDescription("");
            if (course.getInfo() == null) course.setInfo("");
            if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El título es requerido");
            }

            System.out.println("Guardando curso en la base de datos...");
            Course savedCourse = courseRepo.save(course);
            System.out.println("✅ CURSO CREADO EXITOSAMENTE");
            System.out.println("ID del curso: " + savedCourse.getId());
            System.out.println("Título: " + savedCourse.getTitle());
            System.out.println("Categoría: " + savedCourse.getCategory().getName());
            System.out.println("Capacidad: " + savedCourse.getCapacity());
            System.out.println("Inscritos: " + savedCourse.getEnrolled());

            return ResponseEntity.ok(savedCourse);

        } catch (Exception e) {
            System.out.println("❌ ERROR CREANDO CURSO: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error interno: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> update(@PathVariable Long id, @RequestBody Course courseDetails) {
        try {
            Optional<Course> courseOpt = courseRepo.findById(id);
            if (courseOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Course course = courseOpt.get();

            if (courseDetails.getTitle() != null) course.setTitle(courseDetails.getTitle());
            if (courseDetails.getImage() != null) course.setImage(courseDetails.getImage());
            if (courseDetails.getShortDescription() != null) course.setShortDescription(courseDetails.getShortDescription());
            if (courseDetails.getInfo() != null) course.setInfo(courseDetails.getInfo());
            if (courseDetails.getCapacity() != null) course.setCapacity(courseDetails.getCapacity());

            if (courseDetails.getCategory() != null && courseDetails.getCategory().getId() != null) {
                Optional<Category> catOpt = categoryRepo.findById(courseDetails.getCategory().getId());
                if (catOpt.isPresent()) {
                    course.setCategory(catOpt.get());
                }
            }

            Course updated = courseRepo.save(course);

            if (updated.getCategory() != null) {
                updated.getCategory().getName();
            }
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (courseRepo.existsById(id)) {
            courseRepo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}