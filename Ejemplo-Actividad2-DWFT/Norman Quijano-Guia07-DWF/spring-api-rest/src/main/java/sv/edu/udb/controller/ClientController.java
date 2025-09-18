package sv.edu.udb.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sv.edu.udb.controller.request.ClientRequest;
import static org.springframework.http.HttpStatus.CREATED;
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "clients")
public class ClientController {
    @PostMapping
    @ResponseStatus(CREATED)
    public String checkPhoneNumber(@Valid @RequestBody final ClientRequest request) {
        return "Numero de telefono valido " + request.getPhoneNumber();
    }
}

