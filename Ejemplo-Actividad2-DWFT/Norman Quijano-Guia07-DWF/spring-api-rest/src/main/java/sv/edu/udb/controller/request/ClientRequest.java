package sv.edu.udb.controller.request;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.udb.controller.validation.PhoneNumber;
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) //Ignora propiedades desconocidas
@JsonInclude(JsonInclude.Include.NON_NULL) //No incluye los valoros devueltos nulos
public class ClientRequest {
    @NotBlank
    @PhoneNumber(message = "Numero de telefono invalido", pattern = "^\\d{4}-\\d{4}$")
    private String phoneNumber;
}

