package jaime.funkoext2.controlador;



import com.fasterxml.jackson.core.JsonProcessingException;
import jaime.funkoext2.dto.Funkodto;
import jaime.funkoext2.dto.FunkodtoUpdated;
import jaime.funkoext2.models.Funko;
import jaime.funkoext2.services.FunkoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FunkoControlador {
    private final FunkoService funkoServicio;
    @Autowired
    public FunkoControlador(FunkoService funkoServicio) {
        this.funkoServicio = funkoServicio;
    }
    @GetMapping("/funkos")
    public ResponseEntity<List<Funko>> getProducts() {
        return ResponseEntity.ok(funkoServicio.findall());
    }

    @GetMapping("/funkos/{id}")
    public ResponseEntity<Funko> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(funkoServicio.findById(id));
    }

    @PostMapping("/funkos")
    public ResponseEntity<Funko> createProduct(@Valid @RequestBody Funkodto funko)  {
        return ResponseEntity.status(HttpStatus.CREATED).body(funkoServicio.save(funko));
    }

    @PutMapping("/funkos/{id}")
    public ResponseEntity<Funko> updateProduct(@PathVariable Long id, @Valid @RequestBody FunkodtoUpdated funko) {
        return ResponseEntity.ok(funkoServicio.update(id,funko));
    }

    @DeleteMapping("/funkos/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        funkoServicio.DeleteById(id);
        return ResponseEntity.noContent().build();
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
