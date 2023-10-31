package jaime.funkoext2.mapper;


import jaime.funkoext2.dto.Funkodto;
import jaime.funkoext2.dto.FunkodtoUpdated;
import jaime.funkoext2.models.Funko;

import java.time.LocalDate;

public class mapeador {

    public Funko toFunkoNew(Funkodto funko) {
        return Funko.builder()
                .nombre(funko.getName())
                .precio(funko.getPrecio())
                .cantidad(funko.getCantidad())
                .imagen(funko.getImagen())
                .fecha_cre(LocalDate.now())
                .fecha_act(LocalDate.now())
                .build();
    }
    public Funko toFunkoUpdated(FunkodtoUpdated funko, Funko funko1){
        return Funko.builder()
                .id(funko1.getId())
                .nombre(funko.getName())
                .precio(funko.getPrecio())
                .cantidad(funko.getCantidad())
                .imagen(funko.getImagen())
                .fecha_cre(funko1.getFecha_cre())
                .fecha_act(LocalDate.now())
                .build();
    }
}