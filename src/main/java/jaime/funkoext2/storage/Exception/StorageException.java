package jaime.funkoext2.storage.Exception;

public abstract class StorageException extends RuntimeException {
    public StorageException(String mensaje) {
        super(mensaje);
    }
}
