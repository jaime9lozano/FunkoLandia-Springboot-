package jaime.funkoext2.storage.Exception;

import java.io.Serial;

public abstract class StorageException extends RuntimeException {
    public StorageException(String mensaje) {
        super(mensaje);
    }
}
