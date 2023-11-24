package jaime.funkoext2.funko.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FunkodtoUpdated {
    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre del funko", example = "Stitch")
    String nombre;
    @NotNull(message = "El precio no puede estar vacío")
    @Positive(message = "El precio no puede ser negativo")
    @Schema(description = "Precio del funko", example = "100.0")
    Double precio;
    @NotNull(message = "El precio no puede estar vacío")
    @Positive(message = "La cantidad no puede ser negativo")
    @Schema(description = "Cantidad del funko", example = "10")
    int cantidad;
    @NotBlank(message = "La imagen no puede estar vacía")
    @Schema(description = "Imagen del funko", example = "https://via.placeholder.com/150")
    String imagen;
    @Schema(description = "Categoría del funko", example = "DISNEY")
    String categoria;
}