package jaime.funkoext2.services;

import jaime.funkoext2.dto.Categoriadto;
import jaime.funkoext2.dto.CategoriadtoUpdated;
import jaime.funkoext2.models.Categoria;
import jaime.funkoext2.models.Funko;

import java.util.List;

public interface CategoriaService {
    List<Categoria> findall();
    Categoria findByCategoria(String name);
    Categoria findById(Long id);
    Categoria save(Categoriadto categoriadto);
    Categoria update(Long id, CategoriadtoUpdated categoriadto);
    void DeleteById(Long id);
}
