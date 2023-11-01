package jaime.funkoext2.dto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FunkodtoUpdated {
    @NotBlank(message = "El nombre no puede estar vacío")
    String nombre;
    @NotNull(message = "El precio no puede estar vacío")
    @Positive(message = "El precio no puede ser negativo")
    Double precio;
    @NotNull(message = "El precio no puede estar vacío")
    @Positive(message = "La cantidad no puede ser negativo")
    int cantidad;
    @NotBlank(message = "El nombre no puede estar vacío")
    String imagen;
    String categoria;
}