package jaime.funkoext2.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoriaNoEncontrada extends FunkoException{
    public CategoriaNoEncontrada(String nombre) {
        super("Categoria con nombre " + nombre + " no encontrada");
    }
}
