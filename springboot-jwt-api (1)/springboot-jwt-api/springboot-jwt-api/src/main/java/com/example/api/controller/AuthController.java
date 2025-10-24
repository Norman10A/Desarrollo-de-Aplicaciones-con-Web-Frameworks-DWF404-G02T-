package com.example.api.controller;

import com.example.api.entity.Role;
import com.example.api.entity.User;
import com.example.api.repository.UserRepository;
import com.example.api.config.JwtService;
import com.example.api.Service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/api/auth")
@Transactional
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private DatabaseService databaseService;

    @PostMapping("/login")
    @Transactional(readOnly = true)
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            System.out.println("=== INICIANDO LOGIN ===");
            String email = body.get("email");
            String password = body.get("password");

            System.out.println("Email recibido: " + email);

            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El email es requerido"));
            }

            Optional<User> userOpt = userRepo.findByEmail(email);
            if (userOpt.isEmpty()) {
                System.out.println("Usuario no encontrado con email: " + email);
                return ResponseEntity.badRequest().body(Map.of("error", "Usuario no encontrado"));
            }

            User user = userOpt.get();
            System.out.println("Usuario encontrado: " + user.getUsername() + " (ID: " + user.getId() + ")");

            if (!encoder.matches(password, user.getPassword())) {
                System.out.println("Contraseña incorrecta para usuario: " + user.getUsername());
                return ResponseEntity.badRequest().body(Map.of("error", "Contraseña incorrecta"));
            }

            String token = jwtService.generateToken(user.getUsername());
            System.out.println("Token generado para: " + user.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("roles", user.getRoles());
            response.put("isFusalmo", user.getEmail().toLowerCase().endsWith("@fusalmo.org"));

            System.out.println("LOGIN EXITOSO: " + user.getUsername());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("ERROR EN LOGIN: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Error interno del servidor"));
        }
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        try {
            System.out.println("=== INICIANDO REGISTRO ===");
            String username = body.get("username");
            String email = body.get("email");
            String pass = body.get("password");

            System.out.println("Datos recibidos - Username: " + username + ", Email: " + email);

            // Validaciones
            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El nombre de usuario es requerido"));
            }
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El email es requerido"));
            }
            if (pass == null || pass.length() < 6) {
                return ResponseEntity.badRequest().body(Map.of("error", "La contraseña debe tener al menos 6 caracteres"));
            }

            // Verificar si el usuario ya existe
            if (userRepo.findByUsername(username).isPresent()) {
                System.out.println("Usuario ya existe: " + username);
                return ResponseEntity.badRequest().body(Map.of("error", "El usuario ya existe"));
            }

            // Verificar si el email ya está registrado
            if (userRepo.findByEmail(email).isPresent()) {
                System.out.println("Email ya registrado: " + email);
                return ResponseEntity.badRequest().body(Map.of("error", "El email ya está registrado"));
            }

            // Crear nuevo usuario
            User u = new User();
            u.setUsername(username.trim());
            u.setEmail(email.trim().toLowerCase());
            u.setPassword(encoder.encode(pass));

            // Asignar rol según el email
            if (email.toLowerCase().endsWith("@fusalmo.org")) {
                u.getRoles().add(Role.ADMIN);
                System.out.println("Rol asignado: ADMIN");
            } else {
                u.getRoles().add(Role.USER);
                System.out.println("Rol asignado: USER");
            }

            // Guardar usuario usando el servicio transaccional
            System.out.println("Guardando usuario mediante servicio...");
            User savedUser = databaseService.createUser(u);
            System.out.println("Usuario guardado con ID: " + savedUser.getId());

            // Verificar que realmente se guardó
            Optional<User> verifiedUser = userRepo.findById(savedUser.getId());
            if (verifiedUser.isPresent()) {
                System.out.println("Usuario verificado en BD: " + verifiedUser.get().getUsername());
            } else {
                System.out.println("ERROR: Usuario no encontrado después de guardar");
                return ResponseEntity.badRequest().body(Map.of("error", "Error al guardar el usuario"));
            }

            // Generar token
            String token = jwtService.generateToken(savedUser.getUsername());
            System.out.println("Token generado para: " + savedUser.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", savedUser.getUsername());
            response.put("email", savedUser.getEmail());
            response.put("roles", savedUser.getRoles());
            response.put("isFusalmo", savedUser.getEmail().toLowerCase().endsWith("@fusalmo.org"));

            System.out.println("REGISTRO EXITOSO: " + savedUser.getUsername());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("ERROR EN REGISTRO: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }

    @GetMapping("/status")
    @Transactional(readOnly = true)
    public ResponseEntity<?> status() {
        try {
            long userCount = userRepo.count();
            System.out.println("Verificando estado - Total usuarios: " + userCount);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Servidor funcionando correctamente");
            response.put("users_count", userCount);
            response.put("timestamp", System.currentTimeMillis());
            response.put("database", "MySQL");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error en endpoint status: " + e.getMessage());
            return ResponseEntity.ok(Map.of("status", "Servidor con errores: " + e.getMessage()));
        }
    }

    @GetMapping("/test-db")
    @Transactional(readOnly = true)
    public ResponseEntity<?> testDatabase() {
        try {
            long userCount = userRepo.count();

            System.out.println("=== TEST DE BASE DE DATOS ===");
            System.out.println("Total usuarios en DB: " + userCount);

            // Listar todos los usuarios para debug
            Iterable<User> allUsers = userRepo.findAll();
            System.out.println("Usuarios en la base de datos:");
            allUsers.forEach(user -> {
                System.out.println(" - ID: " + user.getId() +
                        ", Username: " + user.getUsername() +
                        ", Email: " + user.getEmail() +
                        ", Roles: " + user.getRoles());
            });

            Map<String, Object> response = new HashMap<>();
            response.put("users", userCount);
            response.put("status", "Database connected successfully");
            response.put("database", "MySQL");
            response.put("total_users", userCount);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("ERROR EN TEST DB: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Database error: " + e.getMessage());
            errorResponse.put("status", "Database connection failed");

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/force-check")
    @Transactional
    public ResponseEntity<?> forceCheck() {
        try {
            System.out.println("=== FORZANDO VERIFICACIÓN ===");

            // Verificar estado actual
            long initialCount = userRepo.count();
            System.out.println("Usuarios iniciales: " + initialCount);

            // Crear un usuario de prueba directamente
            User testUser = new User();
            String timestamp = String.valueOf(System.currentTimeMillis());
            testUser.setUsername("test_" + timestamp);
            testUser.setEmail("test" + timestamp + "@test.com");
            testUser.setPassword(encoder.encode("test123"));
            testUser.getRoles().add(Role.USER);

            System.out.println("Creando usuario de prueba...");
            User savedUser = databaseService.createUser(testUser);
            System.out.println("Usuario de prueba creado: " + savedUser.getId());

            // Verificar inmediatamente
            Thread.sleep(1000); // Pequeño delay para asegurar persistencia
            long finalCount = userRepo.count();

            // Verificar que el usuario existe
            Optional<User> verifiedUser = userRepo.findById(savedUser.getId());
            boolean userExists = verifiedUser.isPresent();

            Map<String, Object> response = new HashMap<>();
            response.put("status", userExists ? "success" : "error");
            response.put("test_user_id", savedUser.getId());
            response.put("initial_users", initialCount);
            response.put("final_users", finalCount);
            response.put("user_exists", userExists);
            response.put("message", userExists ?
                    "Usuario de prueba creado y verificado correctamente" :
                    "Error: Usuario no persistido en BD");

            System.out.println("Resultado force-check: " + response);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("ERROR en force-check: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("status", "failure");

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/users")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getAllUsers() {
        try {
            System.out.println("Obteniendo todos los usuarios...");
            Iterable<User> users = userRepo.findAll();

            System.out.println("Usuarios encontrados:");
            users.forEach(user -> {
                System.out.println(" - ID: " + user.getId() +
                        ", Username: " + user.getUsername() +
                        ", Email: " + user.getEmail() +
                        ", Roles: " + user.getRoles());
            });

            return ResponseEntity.ok(users);
        } catch (Exception e) {
            System.out.println("Error obteniendo usuarios: " + e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error obteniendo usuarios: " + e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/check-email/{email}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> checkEmail(@PathVariable String email) {
        try {
            System.out.println("Verificando email: " + email);
            boolean exists = userRepo.findByEmail(email).isPresent();

            Map<String, Object> response = new HashMap<>();
            response.put("email", email);
            response.put("exists", exists);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error verificando email: " + e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error verificando email: " + e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/check-username/{username}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> checkUsername(@PathVariable String username) {
        try {
            System.out.println("Verificando username: " + username);
            boolean exists = userRepo.findByUsername(username).isPresent();

            Map<String, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("exists", exists);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error verificando username: " + e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error verificando username: " + e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/clear-test-users")
    @Transactional
    public ResponseEntity<?> clearTestUsers() {
        try {
            System.out.println("Limpiando usuarios de prueba...");

            Iterable<User> allUsers = userRepo.findAll();
            int deletedCount = 0;

            for (User user : allUsers) {
                if (user.getUsername().startsWith("test_")) {
                    userRepo.delete(user);
                    deletedCount++;
                    System.out.println("Eliminado usuario: " + user.getUsername());
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("deleted_count", deletedCount);
            response.put("remaining_users", userRepo.count());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("Error limpiando usuarios: " + e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error limpiando usuarios: " + e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}