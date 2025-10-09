package sv.edu.udb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import sv.edu.udb.service.EnrollmentService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inscripcion")
public class EnrollmentController {

    private final EnrollmentService service;

    @GetMapping("/nueva")
    public String form(@RequestParam Long programId, Model model) {
        model.addAttribute("programId", programId);
        return "inscripciones/form";
    }

    @PostMapping
    public String create(@RequestParam Long studentId,
                         @RequestParam Long programId,
                         @RequestParam String periodo,
                         Model model) {
        var e = service.enroll(studentId, programId, periodo);
        model.addAttribute("ok", true);
        model.addAttribute("estado", e.getEstado().name());
        model.addAttribute("programId", programId);
        return "inscripciones/form";
    }
}

