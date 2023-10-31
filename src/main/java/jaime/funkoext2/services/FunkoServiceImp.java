package jaime.funkoext2.services;

import jaime.funkoext2.Exceptions.FunkoNoEncontrado;
import jaime.funkoext2.dto.Funkodto;
import jaime.funkoext2.dto.FunkodtoUpdated;
import jaime.funkoext2.mapper.mapeador;
import jaime.funkoext2.models.Funko;
import jaime.funkoext2.repository.FunkoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = {"funkos"})
public class FunkoServiceImp implements FunkoService {
    FunkoRepository funkoRepository;
    mapeador map = new mapeador();
    @Autowired
    public FunkoServiceImp(FunkoRepository funkoRepository) {
        this.funkoRepository = funkoRepository;
    }
    @Override
    public List<Funko> findall() {
        return funkoRepository.findAll();
    }

    @Override
    @Cacheable
    public List<Funko> findByName(String name) {
        return funkoRepository.findByNombre(name);
    }

    @Override
    @Cacheable
    public Funko findById(Long id) {
        return funkoRepository.findById(id).orElseThrow(() -> new FunkoNoEncontrado(id));
    }

    @Override
    @CachePut
    public Funko save(Funkodto funkodto) {
        Funko funko1 = map.toFunkoNew(funkodto);
        return funkoRepository.save(funko1);
    }

    @Override
    @CachePut
    public Funko update(Long id, FunkodtoUpdated funko) {
        Optional<Funko> existingFunko = funkoRepository.findById(id);
        if (existingFunko.isPresent()) {
            Funko funko1 =map.toFunkoUpdated(funko, existingFunko.get());
            return funkoRepository.save(funko1);
        } else {
            throw new FunkoNoEncontrado(id);
        }
    }

    @Override
    @CacheEvict
    public boolean DeleteById(Long id) {
        Optional<Funko> existingFunko = funkoRepository.findById(id);
        if (existingFunko.isPresent()) {
            funkoRepository.deleteById(id);
            return true;
        }else {
            throw new FunkoNoEncontrado(id);
        }
    }
}
