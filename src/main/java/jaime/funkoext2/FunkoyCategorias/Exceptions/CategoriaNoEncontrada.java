package jaime.funkoext2.FunkoyCategorias.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoriaNoEncontrada extends CategoriaException{
    public CategoriaNoEncontrada(String nombre) {
        super("Categoria con nombre " + nombre + " no encontrada");
    }
    public CategoriaNoEncontrada(Long id) {
        super("Categoria con id " + id + " no encontrada");
    }
}
