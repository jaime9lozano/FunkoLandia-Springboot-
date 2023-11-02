package jaime.funkoext2.storage.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StorageBadRequest extends StorageException {
    public StorageBadRequest(String mensaje) {
        super(mensaje);
    }
}
