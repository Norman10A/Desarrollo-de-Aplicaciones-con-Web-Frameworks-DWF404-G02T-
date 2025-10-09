package sv.edu.udb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/preguntas", "/faq"})
public class PreguntasController {
    @GetMapping
    public String index() {
        return "preguntas/index"; // templates/preguntas/index.html
    }
}

