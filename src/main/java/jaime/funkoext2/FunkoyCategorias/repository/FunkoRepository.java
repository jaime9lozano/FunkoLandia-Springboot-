package jaime.funkoext2.FunkoyCategorias.repository;

import jaime.funkoext2.FunkoyCategorias.models.Funko;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FunkoRepository extends JpaRepository<Funko, Long>, JpaSpecificationExecutor<Funko> {
    List<Funko> findByNombre(String nombre);
}
