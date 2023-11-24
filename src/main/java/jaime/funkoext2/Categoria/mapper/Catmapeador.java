package jaime.funkoext2.Categoria.mapper;


import jaime.funkoext2.Categoria.dto.Categoriadto;
import jaime.funkoext2.Categoria.dto.CategoriadtoUpdated;
import jaime.funkoext2.Categoria.models.Categoria;

import java.time.LocalDate;

public class Catmapeador {
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