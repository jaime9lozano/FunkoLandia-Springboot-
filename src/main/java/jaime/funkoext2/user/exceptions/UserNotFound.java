package jaime.funkoext2.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFound extends UserException {
    public UserNotFound(String message) {
        super(message);
    }

    public UserNotFound(Long id) {
        super("Usuario con id " + id + " no encontrado");
    }
}
