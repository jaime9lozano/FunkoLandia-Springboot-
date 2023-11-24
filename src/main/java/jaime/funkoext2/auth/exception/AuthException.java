package jaime.funkoext2.auth.exception;

public abstract class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
