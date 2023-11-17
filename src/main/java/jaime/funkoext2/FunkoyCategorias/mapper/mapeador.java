package jaime.funkoext2.FunkoyCategorias.mapper;


import jaime.funkoext2.FunkoyCategorias.dto.Categoriadto;
import jaime.funkoext2.FunkoyCategorias.dto.CategoriadtoUpdated;
import jaime.funkoext2.FunkoyCategorias.models.Categoria;
import jaime.funkoext2.FunkoyCategorias.models.Funko;
import jaime.funkoext2.FunkoyCategorias.dto.Funkodto;
import jaime.funkoext2.FunkoyCategorias.dto.FunkodtoUpdated;

import java.time.LocalDate;

public class mapeador {

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
    public Funko toFunkoUpdated(FunkodtoUpdated funko, Funko funko1,Categoria categoria) {
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
    public Categoria toCategoriaNew(Categoriadto dto){
        return Categoria.builder()
                .categoria(dto.getCategoria().toUpperCase())
                .fecha_cre(LocalDate.now())
                .fecha_act(LocalDate.now())
                .build();
    }
    public Categoria toCategoriaUpdated(CategoriadtoUpdated dto, Categoria categoria){
        return Categoria.builder()
                .id(categoria.getId())
                .categoria(dto.getCategoria().toUpperCase())
                .fecha_cre(categoria.getFecha_cre())
                .fecha_act(LocalDate.now())
                .build();

    }
}