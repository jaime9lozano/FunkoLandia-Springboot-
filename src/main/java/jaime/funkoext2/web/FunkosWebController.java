package jaime.funkoext2.web;

import jaime.funkoext2.funko.dto.Funkodto;
import jaime.funkoext2.funko.dto.FunkodtoUpdated;
import jaime.funkoext2.Categoria.models.Categoria;
import jaime.funkoext2.funko.models.Funko;
import jaime.funkoext2.Categoria.services.CategoriaService;
import jaime.funkoext2.funko.service.FunkoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/funkos")
public class FunkosWebController {
    private final FunkoService funkosService;
    private final CategoriaService categoriaService;
    private final MessageSource messageSource;
    @Autowired
    public FunkosWebController(FunkoService funkosService, CategoriaService categoriaService, MessageSource messageSource) {
        this.funkosService = funkosService;
        this.categoriaService = categoriaService;
        this.messageSource = messageSource;
    }
    @GetMapping(path = {"", "/", "/index", "/list"})
    public String index(HttpSession session,
                        Model model,
                        @RequestParam(value = "search", required = false) Optional<String> search,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "id") String sortBy,
                        @RequestParam(defaultValue = "asc") String direction,
                        Locale locale
    ) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // Creamos cómo va a ser la paginación
        Pageable pageable = PageRequest.of(page, size, sort);
        // Obtenemos la página de productos
        var funkosPage = funkosService.findall(search, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

        // Mensaje de bienvenida o de encabezado -- Localización
        String welcomeMessage = messageSource.getMessage("welcome.message", null, locale);
        // Devolvemos la página
        model.addAttribute("funkosPage", funkosPage);
        model.addAttribute("search", search.orElse(""));
        model.addAttribute("welcomeMessage", welcomeMessage);
        return "funkos/index";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Long id, Model model, HttpSession session) {

        Funko producto = funkosService.findById(id);
        model.addAttribute("producto", producto);
        return "funkos/details";
    }

    @GetMapping("/create")
    public String createForm(Model model, HttpSession session) {

        var categorias = categoriaService.findall(Optional.empty(),  PageRequest.of(0, 1000))
                .get()
                .map(Categoria::getCategoria);
        var producto = Funkodto.builder()
                .imagen("https://via.placeholder.com/150")
                .precio(0.0)
                .cantidad(0)
                .build();
        model.addAttribute("funko", producto);
        model.addAttribute("categorias", categorias);
        return "funkos/create";
    }
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("producto") Funkodto productoDto,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            var categorias = categoriaService.findall(Optional.empty(), PageRequest.of(0, 1000))
                    .get()
                    .map(Categoria::getCategoria);
            model.addAttribute("categorias", categorias);
            return "funkos/create";
        }
        // Salvamos el producto
        var producto = funkosService.save(productoDto);
        return "redirect:/funkos/index";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model, HttpSession session) {

        var categorias = categoriaService.findall(Optional.empty(),  PageRequest.of(0, 1000))
                .get()
                .map(Categoria::getCategoria);
        Funko producto = funkosService.findById(id);
        FunkodtoUpdated productoUpdateRequest = FunkodtoUpdated.builder()
                .nombre(producto.getNombre())
                .imagen(producto.getImagen())
                .precio(producto.getPrecio())
                .cantidad(producto.getCantidad())
                .categoria(producto.getCategoria().getCategoria())
                .build();
        model.addAttribute("producto", productoUpdateRequest);
        model.addAttribute("categorias", categorias);
        return "funkos/update";
    }
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, @Valid @ModelAttribute("producto") FunkodtoUpdated productoUpdateRequest, BindingResult result, Model model) {
        if (result.hasErrors()) {
            var categorias = categoriaService.findall(Optional.empty(),  PageRequest.of(0, 1000))
                    .get()
                    .map(Categoria::getCategoria);
            model.addAttribute("categorias", categorias);
            return "funkos/update";
        }
        System.out.println(id);
        System.out.println(productoUpdateRequest);
        var res = funkosService.update(id, productoUpdateRequest);
        System.out.println(res);
        return "redirect:/funkos/index";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, HttpSession session) {

        funkosService.DeleteById(id);
        return "redirect:/funkos/index";
    }

    @GetMapping("/update-image/{id}")
    public String showUpdateImageForm(@PathVariable("id") Long productId, Model model, HttpSession session) {
        Funko producto = funkosService.findById(productId);
        model.addAttribute("producto", producto);
        return "funkos/update-image";
    }

    @PostMapping("/update-image/{id}")
    public String updateProductImage(@PathVariable("id") Long productId, @RequestParam("imagen") MultipartFile imagen) {
        funkosService.updateImage(productId, imagen, true);
        return "redirect:/funkos/index";
    }
    private String getLocalizedDate(Date date, Locale locale) {
        // Convertir la fecha a LocalDateTime
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        // Obtener el formatter localizado para el Locale correspondiente a España
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withLocale(locale);
        // Formatear la fecha y la hora localizadas
        return localDateTime.format(formatter);
    }
}
