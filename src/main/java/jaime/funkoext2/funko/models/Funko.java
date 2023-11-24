package jaime.funkoext2.funko.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jaime.funkoext2.Categoria.models.Categoria;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FUNKOS")
public class Funko {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador del producto", example = "1")
    private Long id;
    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre del funko", example = "Stitch")
    String nombre;
    @Column(nullable = false)
    @Positive(message = "El precio no puede ser negativo")
    @NotNull(message = "El precio no puede estar vacío")
    @Schema(description = "Precio del funko", example = "100.0")
    Double precio;
    @Column(nullable = false)
    @Positive(message = "La cantidad no puede ser negativo")
    @NotNull(message = "El precio no puede estar vacío")
    @Schema(description = "Cantidad del funko", example = "10")
    int cantidad;
    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Imagen del funko", example = "https://via.placeholder.com/150")
    String imagen;
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @JsonManagedReference
    @Schema(description = "Categoría del funko", example = "DISNEY")
    private Categoria categoria;
    @Column
    @Schema(description = "Fecha de creación del funko", example = "2021-01-01")
    LocalDate fecha_cre;
    @Column
    @Schema(description = "Fecha de actualizacion del funko", example = "2021-01-01")
    LocalDate fecha_act;
}
