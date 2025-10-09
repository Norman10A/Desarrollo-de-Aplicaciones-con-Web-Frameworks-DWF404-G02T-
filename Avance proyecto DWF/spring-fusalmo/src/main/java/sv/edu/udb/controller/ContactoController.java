package sv.edu.udb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
// aceptamos singular y plural por si el navbar dice "Contactos"
@RequestMapping({"/contacto", "/contactos"})
public class ContactoController {
    @GetMapping
    public String index() {
        return "contacto/index"; // templates/contacto/index.html
    }
}

