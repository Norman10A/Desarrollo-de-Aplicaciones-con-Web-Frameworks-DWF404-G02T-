package sv.edu.udb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import sv.edu.udb.repository.ProgramRepository;

@Controller
@RequiredArgsConstructor
@RequestMapping("/programas")
public class ProgramController {

    private final ProgramRepository repo;

    @GetMapping
    public String index(@RequestParam(required = false) String categoria,
                        @RequestParam(required = false) String academia,
                        Model model) {

        var data = (categoria != null && !categoria.isBlank())
                ? repo.findByCategory_NombreIgnoreCase(categoria)
                : (academia != null && !academia.isBlank())
                ? repo.findByAcademy_NombreIgnoreCase(academia)
                : repo.findAll();

        model.addAttribute("programas", data);
        return "programas/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        var programa = repo.findById(id).orElseThrow();
        model.addAttribute("programa", programa);
        return "programas/show";
    }
}

