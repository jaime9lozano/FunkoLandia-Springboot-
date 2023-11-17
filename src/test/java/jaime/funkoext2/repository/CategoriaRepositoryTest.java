package jaime.funkoext2.repository;

import jaime.funkoext2.FunkoyCategorias.models.Categoria;
import jaime.funkoext2.FunkoyCategorias.repository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class CategoriaRepositoryTest {
    @Autowired
    private CategoriaRepository repository;
    @Autowired
    private TestEntityManager entityManager;
    private final Categoria categoria1 = new Categoria(1L, "Categoria1", LocalDate.now(), LocalDate.now());
    @Test
    void findByCategoria() {
        var res = entityManager.merge(categoria1);
        entityManager.flush();

        var categoria = repository.findByCategoria(res.getCategoria());

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals("Categoria1", categoria.getCategoria()),
                () -> assertEquals(1L, categoria.getId())
        );
    }
    @Test
    void existsProductoById() {
        var res = entityManager.merge(categoria1);
        entityManager.flush();

        boolean exists = repository.existsProductoById(res.getId());

        assertTrue(exists);
    }
    @Test
    void existsProductoById_NotExists() {
        boolean exists = repository.existsProductoById(999L);
        assertFalse(exists);
    }
}