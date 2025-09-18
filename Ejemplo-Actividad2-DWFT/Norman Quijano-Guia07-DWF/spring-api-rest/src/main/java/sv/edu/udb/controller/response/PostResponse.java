package sv.edu.udb.controller.response;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import java.time.LocalDate;
@Getter
@Setter
@Builder(toBuilder = true) //Es para poder modificar el objeto luego de creado
@FieldNameConstants //Para generar constantes con los nombres de las propiedades

public class PostResponse {
    private String title;
    private LocalDate postDate;
}
