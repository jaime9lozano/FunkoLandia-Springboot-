package jaime.funkoext2.FunkoyCategorias.storage.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StorageBadRequest extends StorageException {
    public StorageBadRequest(String mensaje) {
        super(mensaje);
    }
}
