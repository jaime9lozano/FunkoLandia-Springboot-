package jaime.funkoext2.funko.WebSocket;

public record FunkoNotificacionResponse(Long id, String nombre, Double precio, int cantidad, String imagen, String categoria, String fecha_cre, String fecha_act) {
}
