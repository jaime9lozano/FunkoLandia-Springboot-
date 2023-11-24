package jaime.funkoext2.FunkoyCategorias.controlador;



import jaime.funkoext2.FunkoyCategorias.dto.Funkodto;
import jaime.funkoext2.FunkoyCategorias.dto.FunkodtoUpdated;
import jaime.funkoext2.FunkoyCategorias.models.Funko;
import jaime.funkoext2.FunkoyCategorias.services.FunkoService;
import jaime.funkoext2.FunkoyCategorias.util.PageResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("${api.version}/funkos")
public class FunkoControlador {
    private final FunkoService funkoServicio;
    @Autowired
    public FunkoControlador(FunkoService funkoServicio) {
        this.funkoServicio = funkoServicio;
    }
    @GetMapping
    public ResponseEntity<PageResponse<Funko>> getProducts(
            @RequestParam(required = false) Optional<String> nombre,
            @RequestParam(required = false) Optional<Double> preciomax,
            @RequestParam(required = false) Optional<Double> preciomin,
            @RequestParam(required = false) Optional<Integer> cantidadmax,
            @RequestParam(required = false) Optional<Integer> cantidadmin,
            @RequestParam(required = false) Optional<String> imagen,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<Funko> pageResult = funkoServicio.findall(nombre, preciomax, preciomin, cantidadmax, cantidadmin, imagen, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funko> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(funkoServicio.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Funko> createProduct(@Valid @RequestBody Funkodto funko)  {
        return ResponseEntity.status(HttpStatus.CREATED).body(funkoServicio.save(funko));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Funko> updateProduct(@PathVariable Long id, @Valid @RequestBody FunkodtoUpdated funko) {
        return ResponseEntity.ok(funkoServicio.update(id,funko));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        funkoServicio.DeleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Funko> nuevoProducto(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file) {
        List<String> permittedContentTypes = List.of("image/png", "image/jpg", "image/jpeg", "image/gif");
        try {
            String contentType = file.getContentType();

            if (!file.isEmpty() && contentType != null && !contentType.isEmpty() && permittedContentTypes.contains(contentType.toLowerCase())) {
                return ResponseEntity.ok(funkoServicio.updateImage(id, file, true));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha enviado una imagen para el producto válida o esta está vacía");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede saber el tipo de la imagen");
        }
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
