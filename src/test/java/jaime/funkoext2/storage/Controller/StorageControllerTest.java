package jaime.funkoext2.storage.Controller;

import jaime.funkoext2.dto.FunkodtoUpdated;
import jaime.funkoext2.models.Funko;
import jaime.funkoext2.services.FunkoService;
import jaime.funkoext2.storage.Services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
class StorageControllerTest {

    private StorageController storageController;

    @Mock
    private StorageService storageService;

    @Mock
    private FunkoService funkoService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        storageController = new StorageController(storageService, funkoService);
    }

    @Test
    void testServeFile() throws Exception {
        // Arrange
        String filename = "example.jpg";
        MockHttpServletRequest request = new MockHttpServletRequest();
        Resource resource = mock(Resource.class);

        when(storageService.loadAsResource(filename)).thenReturn(resource);
        when(request.getServletContext().getMimeType(anyString())).thenReturn("image/jpeg");

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/storage/" + filename)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_JPEG));

        verify(storageService, times(1)).loadAsResource(filename);
    }

    @Test
    void testUploadFile() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "example.jpg", MediaType.IMAGE_JPEG.toString(), "file content".getBytes());

        when(storageService.store(file)).thenReturn("stored_filename");
        when(storageService.getUrl("stored_filename")).thenReturn("http://example.com/stored_filename");

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/storage")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.url").value("http://example.com/stored_filename"));

        verify(storageService, times(1)).store(file);
    }

    @Test
    void testUploadFileEmptyFile() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", new byte[0]);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/storage")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(storageService, times(0)).store(file);
    }

    @Test
    void testNuevoProducto() throws Exception {
        // Arrange
        Long id = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "example.jpg", MediaType.IMAGE_JPEG.toString(), "file content".getBytes());
        Funko funko = new Funko(); // Debes crear una instancia de Funko adecuada aquí

        when(funkoService.findById(id)).thenReturn(funko);
        when(storageService.store(file)).thenReturn("stored_filename");
        when(storageService.getUrl("stored_filename")).thenReturn("http://example.com/stored_filename");

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/storage/imagen/" + id)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertEquals("http://example.com/stored_filename", funko.getImagen());
        verify(funkoService, times(1)).update(eq(id), any(FunkodtoUpdated.class));
    }

    @Test
    void testNuevoProductoEmptyFile() throws Exception {
        // Arrange
        Long id = 1L;
        MockMultipartFile file = new MockMultipartFile("file", new byte[0]);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/storage/imagen/" + id)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(funkoService, times(0)).update(eq(id), any(FunkodtoUpdated.class));
    }
}
