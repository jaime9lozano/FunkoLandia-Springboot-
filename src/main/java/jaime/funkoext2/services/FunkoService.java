package jaime.funkoext2.services;

import jaime.funkoext2.dto.Funkodto;
import jaime.funkoext2.dto.FunkodtoUpdated;
import jaime.funkoext2.models.Funko;

import java.util.List;

public interface FunkoService {
    List<Funko> findall();
    List<Funko> findByName(String name);
    Funko findById(Long id);
    Funko save(Funkodto funkodto);
    Funko update(Long id, FunkodtoUpdated funko);
    boolean DeleteById(Long id);
}
