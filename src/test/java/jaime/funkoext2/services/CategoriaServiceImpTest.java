package jaime.funkoext2.services;

import jaime.funkoext2.FunkoyCategorias.Exceptions.CategoriaConflict;
import jaime.funkoext2.FunkoyCategorias.Exceptions.CategoriaNoEncontrada;
import jaime.funkoext2.FunkoyCategorias.dto.Categoriadto;
import jaime.funkoext2.FunkoyCategorias.dto.CategoriadtoUpdated;
import jaime.funkoext2.FunkoyCategorias.mapper.mapeador;
import jaime.funkoext2.FunkoyCategorias.models.Categoria;
import jaime.funkoext2.FunkoyCategorias.repository.CategoriaRepository;
import jaime.funkoext2.FunkoyCategorias.services.CategoriaServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CategoriaServiceImpTest {
    private final Categoria categoria1 = new Categoria(1L, "MARVEL", LocalDate.now(), LocalDate.now());
    private final Categoria categoria2 = new Categoria(2L, "DISNEY",LocalDate.now(), LocalDate.now());
    private mapeador map = new mapeador();
    @Mock
    CategoriaRepository repository;
    @InjectMocks
    CategoriaServiceImp service;
    @Test
    void findByCategoria() {
        when(repository.findByCategoria(categoria1.getCategoria())).thenReturn(categoria1);

        var result = repository.findByCategoria(categoria1.getCategoria());

        assertAll("FindByCategoria",
                () -> assertNotNull(result),
                () -> assertEquals(categoria1, result),
                () -> verify(repository, times(1)).findByCategoria(categoria1.getCategoria())
        );
    }

    @Test
    void findById() {
        when(repository.findById(1L)).thenReturn(Optional.of(categoria1));

        var result = repository.findById(1L);

        assertAll("findById",
                () -> assertNotNull(result),
                () -> assertEquals(categoria1, result.get()),
                () -> verify(repository, times(1)).findById(1L)
        );
    }
    @Test
    void findByIdNo() {
        when(repository.findById(43L)).thenReturn(Optional.empty());

        assertThrows(CategoriaNoEncontrada.class, () -> {
            service.findById(43L);
        });

        verify(repository, times(1)).findById(43L);
    }

    @Test
    void save() {
        Categoriadto  dto = new Categoriadto("MARVEL");

        when(repository.save(map.toCategoriaNew(dto))).thenReturn(categoria1);

        var result = service.save(dto);

        assertAll("save",
                () -> assertNotNull(result),
                () -> assertEquals(categoria1, result),
                () -> verify(repository, times(1)).save(map.toCategoriaNew(dto))
        );
    }

    @Test
    void update() {
        CategoriadtoUpdated dto = new CategoriadtoUpdated("MARVEL");
        when(repository.findById(1L)).thenReturn(Optional.of(categoria1));

        when(repository.save(map.toCategoriaUpdated(dto, categoria1))).thenReturn(categoria1);

        var result = service.update(1L, dto);

        assertAll("update",
                () -> assertNotNull(result),
                () -> assertEquals(categoria1, result),
                () -> verify(repository, times(1)).findById(1L),
                () -> verify(repository, times(1)).save(map.toCategoriaUpdated(dto, categoria1))
        );
    }

    @Test
    void updateNo() {
        CategoriadtoUpdated dto = new CategoriadtoUpdated("MARVEL");
        when(repository.findById(43L)).thenReturn(Optional.empty());

        assertThrows(CategoriaNoEncontrada.class, () -> {
            service.update(43L, dto);
        });
    }

    @Test
    void deleteById() {
        when(repository.findById(1L)).thenReturn(Optional.of(categoria1));

        when(repository.existsProductoById(1L)).thenReturn(false);

        service.DeleteById(1L);

        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
    }
    @Test
    void deleteByIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoriaNoEncontrada.class, () -> {
            service.DeleteById(1L);
        });
    }
    @Test
    void deleteByIdNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.of(categoria1));

        when(repository.existsProductoById(1L)).thenReturn(true);

        assertThrows(CategoriaConflict.class, () -> {
            service.DeleteById(1L);
        });
    }
}