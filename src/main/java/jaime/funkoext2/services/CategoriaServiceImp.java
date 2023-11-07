package jaime.funkoext2.services;

import jaime.funkoext2.Exceptions.CategoriaNoEncontrada;
import jaime.funkoext2.dto.Categoriadto;
import jaime.funkoext2.dto.CategoriadtoUpdated;
import jaime.funkoext2.mapper.mapeador;
import jaime.funkoext2.models.Categoria;
import jaime.funkoext2.models.Funko;
import jaime.funkoext2.repository.CategoriaRepository;
import jaime.funkoext2.repository.FunkoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = {"categorias"})
public class CategoriaServiceImp implements CategoriaService {
    CategoriaRepository categoriaRepository;
    FunkoRepository funkoRepository;
    mapeador map = new mapeador();
    @Autowired
    public CategoriaServiceImp(CategoriaRepository categoriaRepository, FunkoRepository funkoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.funkoRepository = funkoRepository;
    }
    @Override
    public List<Categoria> findall() {
        return categoriaRepository.findAll();
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
    public boolean DeleteById(Long id) {
        Categoria categoria = findById(id);
        categoriaRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean categoriaNull(Long categoryId) {
        List<Funko> funkosToUpdate = funkoRepository.findByCategoriaId(categoryId);
        for (Funko funko : funkosToUpdate) {
            funko.setCategoria(null);
            funkoRepository.save(funko);
        }
        return true;
    }
}
