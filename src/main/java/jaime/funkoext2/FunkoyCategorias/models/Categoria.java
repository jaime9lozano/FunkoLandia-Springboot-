package jaime.funkoext2.FunkoyCategorias.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CATEGORIAS")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column( nullable = false, unique = true)
    @NotBlank(message = "La categoria no puede estar vacía")
    private String categoria;
    @Column
    LocalDate fecha_cre;
    @Column
    LocalDate fecha_act;
}
