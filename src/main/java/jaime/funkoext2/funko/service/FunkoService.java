package jaime.funkoext2.funko.service;

import jaime.funkoext2.funko.models.Funko;
import jaime.funkoext2.funko.dto.Funkodto;
import jaime.funkoext2.funko.dto.FunkodtoUpdated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface FunkoService {
    Page<Funko> findall(Optional<String> nombre, Optional <Double> preciomax, Optional<Double> preciomin, Optional<Integer> cantidadmax, Optional<Integer> cantidadmin, Optional<String> imagen, Pageable pageable); ;
    List<Funko> findByName(String name);
    Funko findById(Long id);
    Funko save(Funkodto funkodto);
    Funko update(Long id, FunkodtoUpdated funko);
    boolean DeleteById(Long id);
    Funko updateImage (Long id, MultipartFile image, Boolean withUrl);
}
