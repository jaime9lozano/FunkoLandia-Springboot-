package jaime.funkoext2.ventas.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jaime.funkoext2.util.PageResponse;
import jaime.funkoext2.ventas.exception.*;
import jaime.funkoext2.ventas.models.Cliente;
import jaime.funkoext2.ventas.models.Direccion;
import jaime.funkoext2.ventas.models.LineaPedido;
import jaime.funkoext2.ventas.models.Pedido;
import jaime.funkoext2.ventas.service.PedidosService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class PedidoControllerTest {
    private final String myEndpoint = "/v1/pedidos";
    private final ObjectMapper mapper = new ObjectMapper();
    private final Pedido pedido1 = Pedido.builder()
            .id(new ObjectId("5f9f1a3b9d6b6d2e3c1d6f1a"))
            .idUsuario(1L)
            .cliente(
                    new Cliente("JoseLuisGS", "joseluisgs@soydev.dev", "1234567890",
                            new Direccion("Calle", "1", "Ciudad", "Provincia", "Pais", "12345")
                    )
            )
            .lineasPedido(List.of(LineaPedido.builder()
                    .idProducto(1L)
                    .cantidad(2)
                    .precioProducto(10.0)
                    .build()))
            .build();
    @Autowired
    MockMvc mockMvc; // Cliente MVC
    @MockBean
    private PedidosService service;
    @Autowired
    public PedidoControllerTest(PedidosService service) {
        this.service = service;
        mapper.registerModule(new JavaTimeModule());
    }
    @Test
    void getAllPedidos() throws Exception {
        var pedidosList = List.of(pedido1);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(pedidosList);

        // Arrange
        when(service.findAll(pageable)).thenReturn(page);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<Pedido> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        // Assert
        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );

        // Verify
        verify(service, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getPedidoByID() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";

        // Arrange
        when(service.findById(any(ObjectId.class))).thenReturn(pedido1);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Pedido res = mapper.readValue(response.getContentAsString(), Pedido.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(pedido1, res)
        );

        // Verify
        verify(service, times(1)).findById(any(ObjectId.class));
    }

    @Test
    void getPedidoByIdNoFound() throws Exception {
        // Arrange
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";

        // Arrange
        when(service.findById(any(ObjectId.class)))
                .thenThrow(new PedidoNotFound("5f9f1a3b9d6b6d2e3c1d6f1a"));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );

        // Verify
        verify(service, times(1)).findById(any(ObjectId.class));
    }
    @Test
    void createPedido() throws Exception {
        // Arrange
        when(service.save(any(Pedido.class))).thenReturn(pedido1);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(pedido1)))
                .andReturn().getResponse();

        Pedido res = mapper.readValue(response.getContentAsString(), Pedido.class);

        // Assert
        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(pedido1, res)
        );

        // Verify
        verify(service, times(1)).save(any(Pedido.class));
    }

    @Test
    void createPedidoNoItemsBadRequest() throws Exception {
        // Arrange
        when(service.save(any(Pedido.class))).thenThrow(new PedidoNoItems("5f9f1a3b9d6b6d2e3c1d6f1a"));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(pedido1)))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus())
        );

        // Verify
        verify(service).save(any(Pedido.class));
    }
    @Test
    void createPedidoProductoBadPriceBadRequest() throws Exception {
        // Arrange
        when(service.save(any(Pedido.class))).thenThrow(new FunkoBadPrice(1L));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(pedido1)))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus())
        );

        // Verify
        verify(service).save(any(Pedido.class));
    }

    @Test
    void getPedidosProductoNotFoundBadRequest() throws Exception {
        // Arrange
        when(service.save(any(Pedido.class))).thenThrow(new FunkoNotFound(1L));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(pedido1)))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus())
        );

        // Verify
        verify(service).save(any(Pedido.class));
    }

    @Test
    void getPedidosProductoNotStockBadRequest() throws Exception {
        // Arrange
        when(service.save(any(Pedido.class))).thenThrow(new FunkoNotStock(1L));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(pedido1)))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus())
        );

        // Verify
        verify(service).save(any(Pedido.class));
    }
    @Test
    void updatePedido() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";

        // Arrange
        when(service.update(any(ObjectId.class), any(Pedido.class))).thenReturn(pedido1);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(pedido1)))
                .andReturn().getResponse();

        Pedido res = mapper.readValue(response.getContentAsString(), Pedido.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(pedido1, res)
        );

        // Verify
        verify(service, times(1)).update(any(ObjectId.class), any(Pedido.class));
    }

    @Test
    void updatePedidoNoFound() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";

        // Arrange
        when(service.update(any(ObjectId.class), any(Pedido.class)))
                .thenThrow(new PedidoNotFound("5f9f1a3b9d6b6d2e3c1d6f1a"));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(pedido1)))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );

        // Verify
        verify(service, times(1)).update(any(ObjectId.class), any(Pedido.class));
    }

    @Test
    void updatePedidoNoItemsBadRequest() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";

        // Arrange
        when(service.update(any(ObjectId.class), any(Pedido.class)))
                .thenThrow(new PedidoNoItems("5f9f1a3b9d6b6d2e3c1d6f1a"));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(pedido1)))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus())
        );

        // Verify
        verify(service, times(1)).update(any(ObjectId.class), any(Pedido.class));
    }

    @Test
    void updatePedidoProductoBadPriceBadRequest() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";

        // Arrange
        when(service.update(any(ObjectId.class), any(Pedido.class)))
                .thenThrow(new FunkoBadPrice(1L));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(pedido1)))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus())
        );

        // Verify
        verify(service, times(1)).update(any(ObjectId.class), any(Pedido.class));
    }

    @Test
    void updatePedidoProductoNotFoundBadRequest() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";

        // Arrange
        when(service.update(any(ObjectId.class), any(Pedido.class)))
                .thenThrow(new FunkoNotFound(1L));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(pedido1)))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus())
        );

        // Verify
        verify(service, times(1)).update(any(ObjectId.class), any(Pedido.class));
    }

    @Test
    void updatePedidoNotStockBadRequest() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";

        // Arrange
        when(service.update(any(ObjectId.class), any(Pedido.class)))
                .thenThrow(new FunkoNotStock(1L));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(pedido1)))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus())
        );

        // Verify
        verify(service, times(1)).update(any(ObjectId.class), any(Pedido.class));
    }

    @Test
    void deletePedido() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";

        // Arrange
        doNothing().when(service).delete(any(ObjectId.class));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(204, response.getStatus())
        );

        // Verify
        verify(service, times(1)).delete(any(ObjectId.class));
    }
    @Test
    void deletePedidoNoFound() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";

        // Arrange
        doThrow(new PedidoNotFound("5f9f1a3b9d6b6d2e3c1d6f1a")).when(service).delete(any(ObjectId.class));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );

        // Verify
        verify(service, times(1)).delete(any(ObjectId.class));
    }
}