package jaime.funkoext2.controlador;

import jaime.funkoext2.dto.Categoriadto;
import jaime.funkoext2.dto.CategoriadtoUpdated;
import jaime.funkoext2.models.Categoria;
import jaime.funkoext2.services.CategoriaService;
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
public class CategoriaControlador {
    private final CategoriaService categoriaService;
    @Autowired
    public CategoriaControlador(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }
    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> getProducts() {
        return ResponseEntity.ok(categoriaService.findall());
    }

    @GetMapping("/categorias/{id}")
    public ResponseEntity<Categoria> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.findById(id));
    }

    @PostMapping("/categorias")
    public ResponseEntity<Categoria> createProduct(@Valid @RequestBody Categoriadto categoriadto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.save(categoriadto));
    }
    @PutMapping("/categorias/{id}")
    public ResponseEntity<Categoria> updateProduct(@PathVariable Long id, @Valid @RequestBody CategoriadtoUpdated categoriaUpdated) {
        return ResponseEntity.ok(categoriaService.update(id,categoriaUpdated));
    }
    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        categoriaService.categoriaNull(id);
        categoriaService.DeleteById(id);
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
