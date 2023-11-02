package jaime.funkoext2.storage.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StorageNotFound extends StorageException {
    public StorageNotFound(String mensaje) {
        super(mensaje);
    }
}
