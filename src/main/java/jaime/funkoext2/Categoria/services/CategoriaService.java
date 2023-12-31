package jaime.funkoext2.Categoria.services;

import jaime.funkoext2.Categoria.dto.Categoriadto;
import jaime.funkoext2.Categoria.dto.CategoriadtoUpdated;
import jaime.funkoext2.Categoria.models.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoriaService {
    Page<Categoria> findall(Optional<String> categoria, Pageable pageable);
    Categoria findByCategoria(String name);
    Categoria findById(Long id);
    Categoria save(Categoriadto categoriadto);
    Categoria update(Long id, CategoriadtoUpdated categoriadto);
    void DeleteById(Long id);
}
