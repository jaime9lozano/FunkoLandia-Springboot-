package jaime.funkoext2.FunkoyCategorias.repository;

import jaime.funkoext2.FunkoyCategorias.models.Categoria;
import jaime.funkoext2.FunkoyCategorias.models.Funko;
import jaime.funkoext2.FunkoyCategorias.repository.FunkoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class FunkoRepositoryTest {
    @Autowired
    private FunkoRepository repository;
    @Autowired
    private TestEntityManager entityManager;
    private final Categoria categoria1 = new Categoria(1L, "Categoria1",LocalDate.now(), LocalDate.now());
    private final Funko funko1 =new Funko(1L, "Funko1", 10.29,3 ,"imagen1",categoria1, LocalDate.now(), LocalDate.now());
    @Test
    void findByNombre() {
        var res = entityManager.merge(funko1);
        entityManager.flush();

        var funkos = repository.findByNombre(res.getNombre());

        assertAll(
                () -> assertNotNull(funkos),
                () -> assertEquals(1, funkos.size())
        );
    }
}