package jaime.funkoext2.funko.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jaime.funkoext2.funko.dto.Funkodto;
import jaime.funkoext2.funko.dto.FunkodtoUpdated;
import jaime.funkoext2.funko.models.Funko;
import jaime.funkoext2.funko.service.FunkoService;
import jaime.funkoext2.util.PageResponse;
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
@Tag(name = "Funkos", description = "Endpoint de Funkos de nuestra tienda")
public class FunkoControlador {
    private final FunkoService funkoServicio;
    @Autowired
    public FunkoControlador(FunkoService funkoServicio) {
        this.funkoServicio = funkoServicio;
    }
    @Operation(summary = "Obtiene todos los funkos", description = "Obtiene una lista de funkos")
    @Parameters({
            @Parameter(name = "nombre", description = "Nombre del funko", example = ""),
            @Parameter(name = "preciomax", description = "Precio máximo del funko", example = ""),
            @Parameter(name = "preciomin", description = "Precio minimo del Funko", example = ""),
            @Parameter(name = "cantidadmin", description = "Cantidad mínimo del funko", example = ""),
            @Parameter(name = "cantidadmax", description = "Cantidad máximo del funko", example = ""),
            @Parameter(name = "imagen", description = "Imagen del funko", example = ""),
            @Parameter(name = "page", description = "Número de página", example = "0"),
            @Parameter(name = "size", description = "Tamaño de la página", example = "10"),
            @Parameter(name = "sortBy", description = "Campo de ordenación", example = "id"),
            @Parameter(name = "direction", description = "Dirección de ordenación", example = "asc")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de funkos"),
    })
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

    @Operation(summary = "Obtiene un funko por su id", description = "Obtiene un funko por su id")
    @Parameters({
            @Parameter(name = "id", description = "Identificador del funko", example = "1", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funko"),
            @ApiResponse(responseCode = "404", description = "Funko no encontrado"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Funko> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(funkoServicio.findById(id));
    }

    @Operation(summary = "Crea un funko", description = "Crea un funko")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Funko a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Funko creado"),
            @ApiResponse(responseCode = "400", description = "Funko no válido"),
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Funko> createProduct(@Valid @RequestBody Funkodto funko)  {
        return ResponseEntity.status(HttpStatus.CREATED).body(funkoServicio.save(funko));
    }

    @Operation(summary = "Actualiza un funko", description = "Actualiza un funko")
    @Parameters({
            @Parameter(name = "id", description = "Identificador del funko", example = "1", required = true)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Funko a actualizar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funko actualizado"),
            @ApiResponse(responseCode = "400", description = "Funko no válido"),
            @ApiResponse(responseCode = "404", description = "Funko no encontrado"),
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Funko> updateProduct(@PathVariable Long id, @Valid @RequestBody FunkodtoUpdated funko) {
        return ResponseEntity.ok(funkoServicio.update(id,funko));
    }

    @Operation(summary = "Borra un Funko", description = "Borra un Funko")
    @Parameters({
            @Parameter(name = "id", description = "Identificador del funko", example = "1", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Funko borrado"),
            @ApiResponse(responseCode = "404", description = "Funko no encontrado"),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        funkoServicio.DeleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualiza la imagen de un Funko", description = "Actualiza la imagen de un Funko")
    @Parameters({
            @Parameter(name = "id", description = "Identificador del funko", example = "1", required = true),
            @Parameter(name = "file", description = "Fichero a subir", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funko actualizado"),
            @ApiResponse(responseCode = "400", description = "Funko no válido"),
            @ApiResponse(responseCode = "404", description = "Funko no encontrado"),
    })
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
