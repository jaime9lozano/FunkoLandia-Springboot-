package jaime.funkoext2.FunkoyCategorias.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private Long id;
    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    String nombre;
    @Column(nullable = false)
    @Positive(message = "El precio no puede ser negativo")
    @NotNull(message = "El precio no puede estar vacío")
    Double precio;
    @Column(nullable = false)
    @Positive(message = "La cantidad no puede ser negativo")
    @NotNull(message = "El precio no puede estar vacío")
    int cantidad;
    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    String imagen;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categoria_id")
    @JsonManagedReference
    private Categoria categoria;
    @Column
    LocalDate fecha_cre;
    @Column
    LocalDate fecha_act;
}
