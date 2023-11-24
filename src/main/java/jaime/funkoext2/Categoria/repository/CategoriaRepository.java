package jaime.funkoext2.Categoria.repository;

import jaime.funkoext2.Categoria.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>, JpaSpecificationExecutor<Categoria> {
    Categoria findByCategoria(String nombre);
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Funko p WHERE p.categoria.id = :id")
    boolean existsProductoById(Long id);
}
