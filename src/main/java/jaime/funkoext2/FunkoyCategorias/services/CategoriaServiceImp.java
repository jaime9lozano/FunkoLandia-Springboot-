package jaime.funkoext2.FunkoyCategorias.services;

import jaime.funkoext2.FunkoyCategorias.Exceptions.CategoriaConflict;
import jaime.funkoext2.FunkoyCategorias.Exceptions.CategoriaNoEncontrada;
import jaime.funkoext2.FunkoyCategorias.dto.Categoriadto;
import jaime.funkoext2.FunkoyCategorias.dto.CategoriadtoUpdated;
import jaime.funkoext2.FunkoyCategorias.mapper.mapeador;
import jaime.funkoext2.FunkoyCategorias.models.Categoria;
import jaime.funkoext2.FunkoyCategorias.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@CacheConfig(cacheNames = {"categorias"})
public class CategoriaServiceImp implements CategoriaService {
    CategoriaRepository categoriaRepository;
    mapeador map = new mapeador();
    @Autowired
    public CategoriaServiceImp(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }
    @Override
    public Page<Categoria> findall(Optional<String> categoria, Pageable pageable) {
        Specification<Categoria> specCategoria = (root, query, criteriaBuilder) ->
                categoria.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("categoria")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Categoria> criterio = Specification.where(specCategoria);
        return categoriaRepository.findAll(criterio, pageable);
    }

    @Override
    @Cacheable
    public Categoria findByCategoria(String name) {
        Categoria categoria = categoriaRepository.findByCategoria(name.toUpperCase());
        if (categoria == null) {
            throw new CategoriaNoEncontrada(name.toUpperCase());
        }
        return categoria;
    }

    @Override
    @Cacheable
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id).orElseThrow(() -> new CategoriaNoEncontrada(id));
    }

    @Override
    @CachePut
    public Categoria save(Categoriadto categoriadto) {
        Categoria categoria= map.toCategoriaNew(categoriadto);
        return categoriaRepository.save(categoria);
    }

    @Override
    @CachePut
    public Categoria update(Long id, CategoriadtoUpdated categoriadto) {
        Categoria categoria = findById(id);
        Categoria updated = map.toCategoriaUpdated(categoriadto, categoria);
        return categoriaRepository.save(updated);
    }

    @Override
    @CacheEvict
    public void DeleteById(Long id) {
        Categoria categoria = findById(id);
        if(categoriaRepository.existsProductoById(id)){
            throw new CategoriaConflict("No se puede borrar la categoría con id " + id + " porque tiene productos asociados");
        }else{
            categoriaRepository.deleteById(id);
        }
    }
}
