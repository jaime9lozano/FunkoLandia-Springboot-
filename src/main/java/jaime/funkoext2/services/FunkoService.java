package jaime.funkoext2.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import jaime.funkoext2.dto.Funkodto;
import jaime.funkoext2.dto.FunkodtoUpdated;
import jaime.funkoext2.models.Funko;
import jaime.funkoext2.page.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FunkoService {
    List<Funko> findall();
    List<Funko> findByName(String name);
    Funko findById(Long id);
    Funko save(Funkodto funkodto);
    Funko update(Long id, FunkodtoUpdated funko);
    boolean DeleteById(Long id);
}
