package jaime.funkoext2.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoriadtoUpdated {
    @NotBlank(message = "La categoria no puede estar vacía")
    private String categoria;
}
