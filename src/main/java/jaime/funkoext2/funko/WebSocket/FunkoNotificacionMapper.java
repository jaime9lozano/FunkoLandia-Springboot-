package jaime.funkoext2.funko.WebSocket;

import jaime.funkoext2.funko.models.Funko;
import org.springframework.stereotype.Component;

@Component
public class FunkoNotificacionMapper {
    public static FunkoNotificacionResponse toResponse(Funko funko){
        return new FunkoNotificacionResponse(
                funko.getId(),
                funko.getNombre(),
                funko.getPrecio(),
                funko.getCantidad(),
                funko.getImagen(),
                funko.getCategoria().getCategoria(),
                funko.getFecha_cre().toString(),
                funko.getFecha_act().toString()
        );
    }
}
