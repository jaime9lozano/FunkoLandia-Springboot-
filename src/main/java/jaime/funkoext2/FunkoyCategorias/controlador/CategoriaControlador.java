package jaime.funkoext2.FunkoyCategorias.controlador;

import jaime.funkoext2.FunkoyCategorias.dto.Categoriadto;
import jaime.funkoext2.FunkoyCategorias.dto.CategoriadtoUpdated;
import jaime.funkoext2.FunkoyCategorias.models.Categoria;
import jaime.funkoext2.FunkoyCategorias.services.CategoriaService;
import jaime.funkoext2.FunkoyCategorias.util.PageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("${api.version}/categorias")
public class CategoriaControlador {
    private final CategoriaService categoriaService;
    @Autowired
    public CategoriaControlador(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }
    @GetMapping
    public ResponseEntity<PageResponse<Categoria>> getProducts(
            @RequestParam(required = false) Optional<String> categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<Categoria> pageResult = categoriaService.findall(categoria, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Categoria> createProduct(@Valid @RequestBody Categoriadto categoriadto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.save(categoriadto));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Categoria> updateProduct(@PathVariable Long id, @Valid @RequestBody CategoriadtoUpdated categoriaUpdated) {
        return ResponseEntity.ok(categoriaService.update(id,categoriaUpdated));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
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
