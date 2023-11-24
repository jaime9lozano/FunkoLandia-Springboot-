package jaime.funkoext2.FunkoyCategorias.controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import jaime.funkoext2.FunkoyCategorias.Exceptions.CategoriaNoEncontrada;
import jaime.funkoext2.FunkoyCategorias.dto.Categoriadto;
import jaime.funkoext2.FunkoyCategorias.dto.CategoriadtoUpdated;
import jaime.funkoext2.FunkoyCategorias.models.Categoria;
import jaime.funkoext2.FunkoyCategorias.services.CategoriaService;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class CategoriaControladorTest {
    private final String myEndpoint = "/v1/categorias";
    private final Categoria categoria1 = new Categoria(1L, "MARVEL", LocalDate.now(), LocalDate.now());
    private final Categoria categoria2 = new Categoria(2L, "DISNEY",LocalDate.now(), LocalDate.now());
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private CategoriaService service;
    @Autowired
    private JacksonTester<Categoriadto> jsonCategoriaCreateDto;
    @Autowired
    private JacksonTester<CategoriadtoUpdated> jsonCategoriaUpdateDto;
    @Autowired
    public CategoriaControladorTest(CategoriaService service) {
        this.service = service;
    }

    @Test
    @WithAnonymousUser
    void NotAuthenticated() throws Exception {
        // Localpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(403, response.getStatus());
    }

    @Test
    void getProducts_NoParams() throws Exception {
        List<Categoria> clientsList = List.of(categoria1, categoria2);
        Page<Categoria> page = new PageImpl<>(clientsList);

        when(service.findall(any(Optional.class), any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findall",
                () -> assertEquals(200, response.getStatus())
        );
    }

    @Test
    void getProducts_Cat() throws Exception {
        List<Categoria> clientsList = List.of(categoria1, categoria2);
        Page<Categoria> page = new PageImpl<>(clientsList);
        Optional<String> cat = Optional.of("MARVEL");

        when(service.findall(eq(cat), any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?categoria=MARVEL")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findall",
                () -> assertEquals(200, response.getStatus())
        );
    }
    @Test
    void getProduct() throws Exception {
        when(service.findById(1L)).thenReturn(categoria1);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint + "/" + 1)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findById",
                () -> assertEquals(200, response.getStatus()),
                () -> verify(service, times(1)).findById(1L)
        );
    }
    @Test
    void getProduct_NotFound() throws Exception {
        Long productId = 45L;

        when(service.findById(productId))
                .thenThrow(new CategoriaNoEncontrada(productId));

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint + "/" + productId)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
        verify(service, times(1)).findById(productId);
    }

    @Test
    void createProduct() throws Exception {
        Categoriadto dto = Categoriadto.builder().categoria("MARVEL").build();

        when(service.save(dto)).thenReturn(categoria1);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonCategoriaCreateDto.write(dto).getJson())
                ).andReturn().getResponse();

        assertEquals(201, response.getStatus());
        verify(service, times(1)).save(dto);
    }
    @Test
    void invalid_createCategoria() throws Exception {
        Categoriadto dto = Categoriadto.builder().categoria("").build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonCategoriaCreateDto.write(dto).getJson())
                ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
        verify(service, times(0)).save(dto);
    }

    @Test
    void updateProduct() throws Exception {
        CategoriadtoUpdated dto = CategoriadtoUpdated.builder().categoria("MARVEL").build();

        when(service.update(1L, dto)).thenReturn(categoria1);

        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonCategoriaUpdateDto.write(dto).getJson())
                ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
        verify(service, times(1)).update(1L, dto);
    }
    @Test
    void updateProduct_NotFound() throws Exception {
        CategoriadtoUpdated dto = CategoriadtoUpdated.builder().categoria("MARVEL").build();

        when(service.update(43L, dto)).thenThrow(new CategoriaNoEncontrada(43L));

        MockHttpServletResponse response = mockMvc.perform(
                put(myEndpoint + "/43")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCategoriaUpdateDto.write(dto).getJson())
        ).andReturn().getResponse();

        assertEquals(404, response.getStatus());
        verify(service, times(1)).update(43L, dto);
    }
    @Test
    void deleteProduct() throws Exception {
        Long productId = 1L;

        doNothing().when(service).DeleteById(productId);
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndpoint + "/" + productId)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(204, response.getStatus());
        verify(service, times(1)).DeleteById(productId);
    }
    @Test
    void deleteProduct_NotFound() throws Exception {
        Long productId = 3L;

        doThrow(new CategoriaNoEncontrada(productId)).when(service).DeleteById(productId);

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndpoint + "/" + productId)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
        verify(service, times(1)).DeleteById(productId);
    }
}