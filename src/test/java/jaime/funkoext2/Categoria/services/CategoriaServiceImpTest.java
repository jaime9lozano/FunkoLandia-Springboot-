package jaime.funkoext2.Categoria.services;

import jaime.funkoext2.Categoria.Exceptions.CategoriaConflict;
import jaime.funkoext2.Categoria.Exceptions.CategoriaNoEncontrada;
import jaime.funkoext2.Categoria.dto.Categoriadto;
import jaime.funkoext2.Categoria.dto.CategoriadtoUpdated;
import jaime.funkoext2.Categoria.mapper.Catmapeador;
import jaime.funkoext2.Categoria.models.Categoria;
import jaime.funkoext2.Categoria.repository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CategoriaServiceImpTest {
    private final Categoria categoria1 = new Categoria(1L, "MARVEL", LocalDate.now(), LocalDate.now());
    private final Categoria categoria2 = new Categoria(2L, "DISNEY",LocalDate.now(), LocalDate.now());
    private Catmapeador map = new Catmapeador();
    @Mock
    CategoriaRepository repository;
    @InjectMocks
    CategoriaServiceImp service;
    @Test
    void findall_noParam(){
        List<Categoria> expectedClients = Arrays.asList(categoria1, categoria2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Categoria> expectedPage = new PageImpl<>(expectedClients);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Categoria> actualPage = service.findall(Optional.empty(), pageable);
        assertAll("findAll",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findall_ParamCat(){
        List<Categoria> expectedClients = List.of(categoria1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Categoria> expectedPage = new PageImpl<>(expectedClients);
        Optional<String> categoria = Optional.of("MARVEL");
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Categoria> actualPage = service.findall(categoria, pageable);
        assertAll("findAll",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
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