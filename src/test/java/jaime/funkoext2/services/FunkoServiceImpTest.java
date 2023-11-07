package jaime.funkoext2.services;
import com.fasterxml.jackson.core.JsonProcessingException;
import jaime.funkoext2.Exceptions.CategoriaNoEncontrada;
import jaime.funkoext2.dto.Funkodto;
import jaime.funkoext2.dto.FunkodtoUpdated;
import jaime.funkoext2.Exceptions.FunkoNoEncontrado;
import jaime.funkoext2.mapper.mapeador;
import jaime.funkoext2.models.Categoria;
import jaime.funkoext2.models.Funko;
import jaime.funkoext2.repository.CategoriaRepository;
import jaime.funkoext2.repository.FunkoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FunkoServicioImpTest {
    private final Categoria categoria1 = new Categoria(1L, "MARVEL",LocalDate.now(), LocalDate.now());
    private final Categoria categoria2 = new Categoria(2L, "DISNEY",LocalDate.now(), LocalDate.now());
    private final Funko funko1 =new Funko(1L, "Funko1", 10.29,3 ,"Categoria1", categoria1, LocalDate.now(), LocalDate.now());
    private final Funko funko2 =new Funko(2L, "Funko2", 13.99, 4,"Categoria2", categoria2, LocalDate.now(), LocalDate.now());
    @Mock
    private FunkoRepository funkoRepositorio;
    @Mock
    private CategoriaRepository categoriaRepository;
    @InjectMocks
    private FunkoServiceImp funkoServicioImp;
    private mapeador map = new mapeador();

    @Test
    void findall() {
        List<Funko> funkos = new ArrayList<>();
        funkos.add(funko1);
        funkos.add(funko2);

        when(funkoRepositorio.findAll()).thenReturn(funkos);

        List<Funko> result = funkoServicioImp.findall();

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(funkos.size(), result.size()),
                () -> verify(funkoRepositorio, times(1)).findAll()
        );
    }


    @Test
    void findByName() {
        List<Funko> funkos = new ArrayList<>();
        funkos.add(funko1);
        funkos.add(funko2);

        when(funkoRepositorio.findByNombre("funko1")).thenReturn(funkos);

        List<Funko> result = funkoServicioImp.findByName("funko1");

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(funkos.size(), result.size()),
                () -> verify(funkoRepositorio, times(1)).findByNombre("funko1")
        );

    }
    @Test
    void findByNoName(){
        when(funkoRepositorio.findByNombre("pepito")).thenReturn(Collections.emptyList());

        List<Funko> result = funkoServicioImp.findByName("pepito");

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(0, result.size()),
                () -> verify(funkoRepositorio, times(1)).findByNombre("pepito")
        );
    }
    @Test
    void findById() {
        when(funkoRepositorio.findById(funko1.getId())).thenReturn(Optional.of(funko1));

        Funko result=funkoServicioImp.findById(funko1.getId());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(funko1, result),
                () -> verify(funkoRepositorio, times(1)).findById(funko1.getId())
        );

    }
    @Test
    void findByIdthrow(){
        when(funkoRepositorio.findById(43L)).thenReturn(Optional.empty());

        assertThrows(FunkoNoEncontrado.class, () -> {
            funkoServicioImp.findById(43L);
        });

        verify(funkoRepositorio, times(1)).findById(43L);

    }
    @Test
    void save() throws JsonProcessingException {
        Funkodto funkoDto = new Funkodto("Nuevo Funko", 19.99, 5, "Nueva Categoría", "marvel");
        when(categoriaRepository.findByCategoria(funkoDto.getCategoria().toUpperCase())).thenReturn(categoria1);
        Funko newFunko = map.toFunkoNew(funkoDto,categoria1);

        when(funkoRepositorio.save(any(Funko.class))).thenReturn(newFunko);

        Funko result = funkoServicioImp.save(funkoDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(newFunko.getNombre(), result.getNombre()),
                () -> assertEquals(newFunko.getPrecio(), result.getPrecio()),
                () -> assertEquals(newFunko.getCantidad(), result.getCantidad()),
                () -> assertEquals(newFunko.getImagen(), result.getImagen()),
                () -> assertEquals(newFunko.getCategoria(), result.getCategoria()),
                () -> verify(funkoRepositorio, times(1)).save(any(Funko.class))
        );
    }
    @Test
    void saveCatNo() {
        Funkodto funkoDto = new Funkodto("Nuevo Funko", 19.99, 5, "Nueva Categoría", "marvel");
        when(categoriaRepository.findByCategoria(funkoDto.getCategoria().toUpperCase())).thenReturn(null);

        assertThrows(CategoriaNoEncontrada.class, () -> {
            funkoServicioImp.save(funkoDto);
        });
    }

    @Test
    void update() {
        FunkodtoUpdated updatedFunkoDto = new FunkodtoUpdated("Nuevo Nombre", 24.99, 10, "Nueva Imagen", "DISNEY");
        when(categoriaRepository.findByCategoria(updatedFunkoDto.getCategoria().toUpperCase())).thenReturn(categoria2);
        Long id = 1L;
        Funko existingFunko = new Funko(id, "Funko Existente", 19.99, 5, "Imagen Existente", categoria1, LocalDate.now(), LocalDate.now());
        when(funkoRepositorio.findById(id)).thenReturn(Optional.of(existingFunko));

        Funko updatedFunko = map.toFunkoUpdated(updatedFunkoDto, existingFunko, categoria2);
        when(funkoRepositorio.save(updatedFunko)).thenReturn(updatedFunko);

        Funko result = funkoServicioImp.update(id, updatedFunkoDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(id, result.getId()),
                () -> assertEquals(updatedFunkoDto.getNombre(), result.getNombre()),
                () -> assertEquals(updatedFunkoDto.getPrecio(), result.getPrecio(), 0.001),
                () -> assertEquals(updatedFunkoDto.getCantidad(), result.getCantidad()),
                () -> assertEquals(updatedFunkoDto.getImagen(), result.getImagen()),
                () -> assertEquals(updatedFunkoDto.getCategoria(), result.getCategoria().getCategoria()),
                () -> verify(funkoRepositorio, times(1)).findById(id),
                () -> verify(funkoRepositorio, times(1)).save(updatedFunko)
        );
    }

    @Test
    void updateNoCat(){
        when(funkoRepositorio.findById(1L)).thenReturn(Optional.of(funko1));
        FunkodtoUpdated updatedFunkoDto = new FunkodtoUpdated("Nuevo Nombre", 24.99, 10, "Nueva Imagen", "DISNEY");
        when(categoriaRepository.findByCategoria(updatedFunkoDto.getCategoria().toUpperCase())).thenReturn(null);

        assertThrows(CategoriaNoEncontrada.class, () -> {
            funkoServicioImp.update(1L,updatedFunkoDto);
        });
    }
    @Test
    void updateFunkoNoEncontrado() {
        // Simular que el Funko no existe en el repositorio
        Long id = 1L;
        when(funkoRepositorio.findById(id)).thenReturn(Optional.empty());

        // Crear un FunkodtoUpdated con los nuevos datos
        FunkodtoUpdated updatedFunkoDto = new FunkodtoUpdated("Nuevo Nombre", 24.99, 10, "Nueva Imagen", "Nueva Categoría");

        // Intentar actualizar el Funko y verificar que se lanza una excepción FunkoNoEncontrado
        assertThrows(FunkoNoEncontrado.class, () -> funkoServicioImp.update(id, updatedFunkoDto));

        // Verificar que el método findById se llamó una vez
        verify(funkoRepositorio, times(1)).findById(id);
    }

    @Test
    void deleteById() {
        Long id = 1L;
        Funko existingFunko = new Funko(id, "Funko Existente", 19.99, 5, "Imagen Existente", categoria1, LocalDate.now(), LocalDate.now());
        when(funkoRepositorio.findById(id)).thenReturn(Optional.of(existingFunko));

        boolean result=funkoServicioImp.DeleteById(id);

        assertAll(
                () -> verify(funkoRepositorio, times(1)).findById(id),
                () -> verify(funkoRepositorio, times(1)).deleteById(existingFunko.getId()),
                () -> assertTrue(result)
        );

    }

    @Test
    void deleteByIdFunkoNoEncontrado() {
        Long id = 1L;
        when(funkoRepositorio.findById(id)).thenReturn(Optional.empty());

        assertThrows(FunkoNoEncontrado.class, () -> funkoServicioImp.DeleteById(id));

        verify(funkoRepositorio, times(1)).findById(id);
    }
}