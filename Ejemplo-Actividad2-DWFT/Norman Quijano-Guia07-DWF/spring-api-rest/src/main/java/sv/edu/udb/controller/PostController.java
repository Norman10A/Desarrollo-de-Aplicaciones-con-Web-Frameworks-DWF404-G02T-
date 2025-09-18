package sv.edu.udb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sv.edu.udb.controller.request.PostRequest;
import sv.edu.udb.controller.response.PostResponse;
import sv.edu.udb.service.PostService;

import java.util.List;

// <<< ADD
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.ArrayList;
// >>> ADD

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "posts")
public class PostController {

    private final PostService postService;


    // --- Datos del estudiante desde application.properties ---
    @Value("${student.name:Estudiante}")
    private String studentName;

    @Value("${student.carnet:000000}")
    private String studentCarnet;

    @Value("${student.group:GXX}")
    private String studentGroup;

    // Listas CSV de materias y notas
    @Value("${student.subjects:}")
    private String subjectsCsv;

    @Value("${student.scores:}")
    private String scoresCsv;

    // ==== Integrantes en memoria (sin BD) ====
    private final List<Map<String, String>> integrantes = new ArrayList<>(List.of(
            Map.of("nombreCompleto", "Javier Stanley Valladares", "carrera", "Ingeniería en Computación"),
            Map.of("nombreCompleto", "Norman Enmanuel Quijano Amaya", "carrera", "Ingeniería en Software"),
            Map.of("nombreCompleto", "Otro Integrante", "carrera", "Otra carrera")
    ));


    @GetMapping
    public List<PostResponse> findAll() {
        return postService.findAll();
    }

    @GetMapping(path = "{id}")
    public PostResponse findById(@PathVariable(name = "id") final Long id) {
        return postService.findById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public PostResponse save(@Valid @RequestBody final PostRequest request) {
        return postService.save(request);
    }

    @PutMapping(path = "{id}")
    public PostResponse updatePost(@PathVariable(name = "id") final Long id,
                                   @Valid @RequestBody final PostRequest request) {
        return postService.update(id, request);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(NO_CONTENT)
    public void deletePost(@PathVariable(name = "id") final Long id) {
        postService.delete(id);
    }



    // GET /posts/me
    @GetMapping("me")
    public Map<String, Object> me() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", studentName);
        data.put("carnet", studentCarnet);
        data.put("group", studentGroup);
        return data;
    }

    // GET /posts/notas
    @GetMapping("notas")
    public List<Map<String, Object>> notas() {
        List<String> subjects = parseCsv(subjectsCsv);
        List<Double> scores = parseCsv(scoresCsv).stream()
                .map(s -> {
                    try { return Double.parseDouble(s); }
                    catch (Exception e) { return 0.0; }
                })
                .collect(Collectors.toList());

        int n = Math.min(subjects.size(), scores.size());
        List<Map<String, Object>> out = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Map<String, Object> m = new HashMap<>();
            m.put("subject", subjects.get(i));
            m.put("score", scores.get(i));
            out.add(m);
        }
        return out;
    }

    // === INTEGRANTES ===

    // GET /posts/integrantes → devuelve todos
    @GetMapping("integrantes")
    public List<Map<String, String>> integrantes() {
        return integrantes;
    }

    // POST /posts/integrantes → inscribe/agrega uno nuevo
    @PostMapping("integrantes")
    @ResponseStatus(CREATED)
    public Map<String, String> agregarIntegrante(@RequestBody Map<String, String> nuevo) {
        if (!nuevo.containsKey("nombreCompleto") || !nuevo.containsKey("carrera")) {
            throw new IllegalArgumentException("nombreCompleto y carrera son obligatorios");
        }
        integrantes.add(Map.of(
                "nombreCompleto", nuevo.get("nombreCompleto"),
                "carrera", nuevo.get("carrera")
        ));
        return nuevo;
    }

    // GET /posts/notas/promedio
    @GetMapping("notas/promedio")
    public Map<String, Object> promedio() {
        List<Double> scores = parseCsv(scoresCsv).stream()
                .map(s -> {
                    try { return Double.parseDouble(s); }
                    catch (Exception e) { return 0.0; }
                })
                .collect(Collectors.toList());

        double avg = scores.isEmpty() ? 0.0 :
                scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        Map<String, Object> out = new HashMap<>();
        out.put("carnet", studentCarnet);
        out.put("promedio", avg);
        return out;
    }

    // --- Util interno para partir CSV en lista ---
    private static List<String> parseCsv(String csv) {
        if (csv == null || csv.isBlank()) return List.of();
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
