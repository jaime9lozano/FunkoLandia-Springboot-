package jaime.funkoext2.funko.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FunkoNoEncontrado extends FunkoException{
    public FunkoNoEncontrado(Long id) {
        super("Funko con id " + id + " no encontrado");
    }

}
