package jaime.funkoext2.repository;

import jaime.funkoext2.models.Categoria;
import jaime.funkoext2.models.Funko;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FunkoRepository extends JpaRepository<Funko, Long> {
    List<Funko> findByNombre(String nombre);
}
