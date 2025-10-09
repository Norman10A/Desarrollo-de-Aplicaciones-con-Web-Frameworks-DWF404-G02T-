package sv.edu.udb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/academias")
public class AcademiasController {
    @GetMapping
    public String index() {
        return "academias/index"; // templates/academias/index.html
    }
}
