package com.example.api.config;

import com.example.api.entity.Category;
import com.example.api.entity.User;
import com.example.api.entity.Role;
import com.example.api.repository.CategoryRepository;
import com.example.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("=== INICIANDO VERIFICACIÓN DE DATOS ===");

        // Verificar datos existentes SIN BORRAR
        long initialUserCount = userRepo.count();
        long initialCategoryCount = categoryRepo.count();

        System.out.println("Usuarios existentes: " + initialUserCount);
        System.out.println("Categorías existentes: " + initialCategoryCount);

        // Solo crear usuarios si no existen
        if (initialUserCount == 0) {
            System.out.println("Creando usuarios de prueba...");

            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@fusalmo.org");
            admin.setPassword(encoder.encode("admin123"));
            admin.getRoles().add(Role.ADMIN);
            userRepo.save(admin);
            System.out.println("Usuario admin creado");

            User user = new User();
            user.setUsername("usuario");
            user.setEmail("usuario@ejemplo.com");
            user.setPassword(encoder.encode("user123"));
            user.getRoles().add(Role.USER);
            userRepo.save(user);
            System.out.println("Usuario regular creado");
        } else {
            System.out.println("Usuarios ya existen, no se crean nuevos");
        }

        // Solo crear categorías si no existen
        if (initialCategoryCount == 0) {
            System.out.println("Creando categorías por defecto...");

            List<Category> defaultCategories = Arrays.asList(
                    new Category("Programación", "Cursos de programación y desarrollo de software"),
                    new Category("Diseño", "Cursos de diseño gráfico y UX/UI"),
                    new Category("Marketing", "Cursos de marketing digital y estrategias"),
                    new Category("Finanzas", "Cursos de finanzas personales y empresariales"),
                    new Category("Idiomas", "Cursos de idiomas extranjeros"),
                    new Category("Arte", "Cursos de arte y expresión creativa"),
                    new Category("Música", "Cursos de instrumentos musicales y teoría"),
                    new Category("Deportes", "Cursos de actividades deportivas y fitness")
            );

            categoryRepo.saveAll(defaultCategories);
            System.out.println("Categorías creadas: " + defaultCategories.size());
        } else {
            System.out.println("Categorías ya existen, no se crean nuevas");
        }

        // Verificación final
        System.out.println("=== VERIFICACIÓN FINAL ===");
        System.out.println("Total usuarios: " + userRepo.count());
        System.out.println("Total categorías: " + categoryRepo.count());
        System.out.println("=== INICIALIZACIÓN COMPLETADA ===");
    }
}