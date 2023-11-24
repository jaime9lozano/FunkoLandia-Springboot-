package jaime.funkoext2.FunkoyCategorias.controlador;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jaime.funkoext2.FunkoyCategorias.dto.Funkodto;
import jaime.funkoext2.FunkoyCategorias.dto.FunkodtoUpdated;
import jaime.funkoext2.FunkoyCategorias.Exceptions.FunkoNoEncontrado;
import jaime.funkoext2.FunkoyCategorias.models.Categoria;
import jaime.funkoext2.FunkoyCategorias.models.Funko;
import jaime.funkoext2.FunkoyCategorias.services.FunkoService;
import jaime.funkoext2.FunkoyCategorias.util.PageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class FunkoControladorTest {
    private final String myEndpoint = "/v1/funkos";
    private final Categoria categoria1 = new Categoria(1L, "Categoria1",LocalDate.now(), LocalDate.now());
    private final Categoria categoria2 = new Categoria(2L, "Categoria2",LocalDate.now(), LocalDate.now());
    private final Funko funko1 =new Funko(1L, "Funko1", 10.29,3 ,"imagen1",categoria1, LocalDate.now(), LocalDate.now());
    private final Funko funko2 =new Funko(2L, "Funko2", 13.99, 4,"imagen2", categoria2, LocalDate.now(), LocalDate.now());
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private FunkoService funkoService;
    @Autowired
    private JacksonTester<Funkodto> jsonFunkoCreateDto;
    @Autowired
    private JacksonTester<FunkodtoUpdated> jsonProductoUpdateDto;
    @Autowired
    public FunkoControladorTest(FunkoService funkoService) {
        this.funkoService = funkoService;
    }

    @Test
    void getProducts_NoParams() throws Exception {
        List<Funko> clientsList = List.of(funko1, funko2);
        Page<Funko> page = new PageImpl<>(clientsList);

        when(funkoService.findall(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class) ,any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findall",
                () -> assertEquals(200, response.getStatus())
        );
    }

    @Test
    void getProducts_Name() throws Exception {
        List<Funko> clientsList = List.of(funko1, funko2);
        Page<Funko> page = new PageImpl<>(clientsList);
        Optional<String> name = Optional.of("Funko1");

        when(funkoService.findall(eq(name), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class) ,any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?nombre=Funko1")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findall",
                () -> assertEquals(200, response.getStatus())
        );
    }

    @Test
    void getProducts_PrecioMax() throws Exception {
        List<Funko> clientsList = List.of(funko1, funko2);
        Page<Funko> page = new PageImpl<>(clientsList);
        Optional<Double> precioMax = Optional.of(10.29);

        when(funkoService.findall(any(Optional.class), eq(precioMax), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class) ,any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?preciomax=10.29")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findall",
                () -> assertEquals(200, response.getStatus())
        );
    }

    @Test
    void getProducts_PrecioMin() throws Exception {
        List<Funko> clientsList = List.of(funko1, funko2);
        Page<Funko> page = new PageImpl<>(clientsList);
        Optional<Double> precioMin = Optional.of(10.29);

        when(funkoService.findall(any(Optional.class), any(Optional.class), eq(precioMin), any(Optional.class), any(Optional.class), any(Optional.class) ,any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?preciomin=10.29")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findall",
                () -> assertEquals(200, response.getStatus())
        );
    }

    @Test
    void getProducts_CantidadMax() throws Exception {
        List<Funko> clientsList = List.of(funko1, funko2);
        Page<Funko> page = new PageImpl<>(clientsList);
        Optional<Integer> cantMax = Optional.of(3);

        when(funkoService.findall(any(Optional.class), any(Optional.class), any(Optional.class), eq(cantMax), any(Optional.class), any(Optional.class) ,any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?cantidadmax=3")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findall",
                () -> assertEquals(200, response.getStatus())
        );
    }

    @Test
    void getProducts_CantidadMin() throws Exception {
        List<Funko> clientsList = List.of(funko1, funko2);
        Page<Funko> page = new PageImpl<>(clientsList);
        Optional<Integer> cantMin = Optional.of(3);

        when(funkoService.findall(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), eq(cantMin), any(Optional.class) ,any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?cantidadmin=3")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findall",
                () -> assertEquals(200, response.getStatus())
        );
    }

    @Test
    void getProducts_imagen() throws Exception {
        List<Funko> clientsList = List.of(funko1, funko2);
        Page<Funko> page = new PageImpl<>(clientsList);
        Optional<String> imagen = Optional.of("imagen1");

        when(funkoService.findall(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), eq(imagen) ,any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?imagen=imagen1")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findall",
                () -> assertEquals(200, response.getStatus())
        );
    }
    @Test
    void getProduct() throws Exception{
        when(funkoService.findById(1L)).thenReturn(funko1);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint + "/1")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findById",
                () -> assertEquals(200, response.getStatus()),
                () -> verify(funkoService, times(1)).findById(1L)
        );
    }
    @Test
    void getProduct_NotFound() throws Exception {
        Long productId = 45L;

        when(funkoService.findById(productId))
                .thenThrow(new FunkoNoEncontrado(productId));

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint + "/" + productId)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
        verify(funkoService, times(1)).findById(productId);
    }


    @Test
    void createProduct() throws Exception {
        Funkodto newFunko = Funkodto.builder()
                .nombre("Nuevo Funko")
                .precio(19.99)
                .cantidad(5)
                .imagen("Nueva Categoría")
                .categoria("DISNEY")
                .build();

        when(funkoService.save(any(Funkodto.class))).thenReturn(funko1);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonFunkoCreateDto.write(newFunko).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(201, response.getStatus());
        verify(funkoService, times(1)).save(any(Funkodto.class));
    }
    @Test
    void createProduct_InvalidName() throws Exception {
        Funkodto newFunko = Funkodto.builder()
                .nombre("")
                .precio(19.99)
                .cantidad(5)
                .imagen("Nueva Categoría")
                .categoria("DISNEY")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonFunkoCreateDto.write(newFunko).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }
    @Test
    void createProduct_InvalidPrecio() throws Exception {
        Funkodto newFunko = Funkodto.builder()
                .nombre("Nuevo Funko")
                .precio(-5.0)
                .cantidad(5)
                .imagen("Nueva Categoría")
                .categoria("DISNEY")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonFunkoCreateDto.write(newFunko).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void createProduct_InvalidCantidad() throws Exception {
        Funkodto newFunko = Funkodto.builder()
                .nombre("Nuevo Funko")
                .precio(19.99)
                .cantidad(-5)
                .imagen("Nueva Categoría")
                .categoria("DISNEY")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonFunkoCreateDto.write(newFunko).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void createProduct_InvalidImagen() throws Exception {
        Funkodto newFunko = Funkodto.builder()
                .nombre("Nuevo Funko")
                .precio(19.99)
                .cantidad(5)
                .imagen("")
                .categoria("DISNEY")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonFunkoCreateDto.write(newFunko).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void createProduct_InvalidCategoria() throws Exception {
        Funkodto newFunko = Funkodto.builder()
                .nombre("Nuevo Funko")
                .precio(19.99)
                .cantidad(5)
                .imagen("Nueva Categoría")
                .categoria("")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonFunkoCreateDto.write(newFunko).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }
    @Test
    void updateProduct() throws Exception {
        FunkodtoUpdated updatedFunko = FunkodtoUpdated.builder()
                .nombre("Funko actualizado")
                .precio(15.99)
                .cantidad(5)
                .imagen("imagen_actualizada")
                .categoria("DISNEY")
                .build();

        when(funkoService.update(eq(1L), any(FunkodtoUpdated.class))).thenReturn(funko1);

        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoUpdateDto.write(updatedFunko).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
        verify(funkoService, times(1)).update(eq(1L), any(FunkodtoUpdated.class));
    }

    @Test
    void updateProduct_InvalidName() throws Exception {
        FunkodtoUpdated updatedFunko = FunkodtoUpdated.builder()
                .nombre("")
                .precio(19.99)
                .cantidad(5)
                .imagen("Nueva Categoría")
                .categoria("DISNEY")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoUpdateDto.write(updatedFunko).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void updateProduct_InvalidPrecio() throws Exception {
        FunkodtoUpdated updatedFunko = FunkodtoUpdated.builder()
                .nombre("Nuevo Funko")
                .precio(-5.0)
                .cantidad(5)
                .imagen("Nueva Categoría")
                .categoria("DISNEY")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoUpdateDto.write(updatedFunko).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void updateProduct_InvalidCantidad() throws Exception {
        FunkodtoUpdated updatedFunko = FunkodtoUpdated.builder()
                .nombre("Nuevo Funko")
                .precio(19.99)
                .cantidad(-5)
                .imagen("Nueva Categoría")
                .categoria("DISNEY")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoUpdateDto.write(updatedFunko).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void updateProduct_InvalidImagen() throws Exception {
        FunkodtoUpdated updatedFunko = FunkodtoUpdated.builder()
                .nombre("Nuevo Funko")
                .precio(19.99)
                .cantidad(5)
                .imagen("")
                .categoria("DISNEY")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoUpdateDto.write(updatedFunko).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deleteProduct() throws Exception {
        Long productId = 1L;

        when(funkoService.DeleteById(productId)).thenReturn(true);

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndpoint + "/" + productId)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(204, response.getStatus());
        verify(funkoService, times(1)).DeleteById(productId);
    }
    @Test
    void deleteProduct_NotFound() throws Exception {
        Long productId = 3L;

        when(funkoService.DeleteById(productId))
                .thenThrow(new FunkoNoEncontrado(productId));

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndpoint + "/" + productId)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
        verify(funkoService, times(1)).DeleteById(productId);
    }
}