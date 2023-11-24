package jaime.funkoext2.funko.mapper;

import jaime.funkoext2.Categoria.models.Categoria;
import jaime.funkoext2.funko.dto.Funkodto;
import jaime.funkoext2.funko.dto.FunkodtoUpdated;
import jaime.funkoext2.funko.models.Funko;

import java.time.LocalDate;

public class FunkoMapeador {
    public Funko toFunkoNew(Funkodto funko, Categoria categoria) {
        return Funko.builder()
                .nombre(funko.getNombre())
                .precio(funko.getPrecio())
                .cantidad(funko.getCantidad())
                .imagen(funko.getImagen())
                .fecha_cre(LocalDate.now())
                .fecha_act(LocalDate.now())
                .categoria(categoria)
                .build();
    }
    public Funko toFunkoUpdated(FunkodtoUpdated funko, Funko funko1, Categoria categoria) {
        return Funko.builder()
                .id(funko1.getId())
                .nombre(funko.getNombre())
                .precio(funko.getPrecio())
                .cantidad(funko.getCantidad())
                .imagen(funko.getImagen())
                .fecha_cre(funko1.getFecha_cre())
                .fecha_act(LocalDate.now())
                .categoria(categoria)
                .build();
    }
}
