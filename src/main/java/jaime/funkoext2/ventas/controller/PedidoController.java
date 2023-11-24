package jaime.funkoext2.ventas.controller;

import jaime.funkoext2.FunkoyCategorias.util.PageResponse;
import jaime.funkoext2.ventas.models.Pedido;
import jaime.funkoext2.ventas.service.PedidosService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("${api.version}/pedidos")
public class PedidoController {
    private final PedidosService service;
    @Autowired
    public PedidoController(PedidosService service) {
        this.service = service;
    }
    @GetMapping
    public ResponseEntity<PageResponse<Pedido>> getAllPedidos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<Pedido> pageResult = service.findAll(PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .body(PageResponse.of(pageResult, sortBy, direction));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedido(@PathVariable("id") ObjectId idPedido) {
        return ResponseEntity.ok(service.findById(idPedido));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<PageResponse<Pedido>> getPedidosByUsuario(
            @PathVariable("id") Long idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(service.findByIdUsuario(idUsuario, pageable), sortBy, direction));
    }
    @PostMapping
    public ResponseEntity<Pedido> createPedido(@Valid @RequestBody Pedido pedido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(pedido));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> updatePedido(@PathVariable("id") ObjectId idPedido, @Valid @RequestBody Pedido pedido) {
        return ResponseEntity.ok(service.update(idPedido, pedido));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Pedido> deletePedido(@PathVariable("id") ObjectId idPedido) {
        service.delete(idPedido);
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
