package jaime.funkoext2.ventas.service;

import jaime.funkoext2.FunkoyCategorias.repository.FunkoRepository;
import jaime.funkoext2.ventas.exception.*;
import jaime.funkoext2.ventas.models.LineaPedido;
import jaime.funkoext2.ventas.models.Pedido;
import jaime.funkoext2.ventas.repository.PedidosRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@CacheConfig(cacheNames = {"pedidos"})
public class PedidosServiceImp implements PedidosService{
    private final PedidosRepository pedidosRepository;
    private final FunkoRepository funkosRepository;
    public PedidosServiceImp(PedidosRepository pedidosRepository, FunkoRepository funkosRepository) {
        this.pedidosRepository = pedidosRepository;
        this.funkosRepository = funkosRepository;
    }
    @Override
    public Page<Pedido> findAll(Pageable pageable) {
        return pedidosRepository.findAll(pageable);
    }

    @Override
    @Cacheable
    public Pedido findById(ObjectId idPedido) {
        return pedidosRepository.findById(idPedido).orElseThrow(() -> new PedidoNotFound(idPedido.toHexString()));
    }

    @Override
    public Page<Pedido> findByIdUsuario(Long idUsuario, Pageable pageable) {
        return pedidosRepository.findByIdUsuario(idUsuario, pageable);
    }

    @Override
    @Transactional
    @CachePut
    public Pedido save(Pedido pedido) {
        checkPedido(pedido);

        var pedidoToSave = reserveStockPedidos(pedido);

        pedidoToSave.setCreatedAt(LocalDateTime.now());
        pedidoToSave.setUpdatedAt(LocalDateTime.now());

        return pedidosRepository.save(pedidoToSave);
    }

    @Override
    @Transactional
    @CacheEvict
    public void delete(ObjectId idPedido) {
        var pedidoToDelete = pedidosRepository.findById(idPedido).orElseThrow(() -> new PedidoNotFound(idPedido.toHexString()));

        // Ahora debemos devolver el stock de los productos
        returnStockPedidos(pedidoToDelete);

        // Borramos el pedido
        pedidosRepository.deleteById(idPedido);
    }

    @Override
    @Transactional
    @CachePut
    public Pedido update(ObjectId idPedido, Pedido pedido) {
        var pedidoToUpdate = pedidosRepository.findById(idPedido).orElseThrow(() -> new PedidoNotFound(idPedido.toHexString()));
        pedido.setId(idPedido);

        // Devolvemos el stock de los productos
        returnStockPedidos(pedido);

        // Comprobamos el pedido y sus datos
        checkPedido(pedido);

        // Actualizamos el stock de los productos
        var pedidoToSave = reserveStockPedidos(pedido);

        // Fecha actualización
        pedidoToSave.setUpdatedAt(LocalDateTime.now());

        // Actualizamos el pedido en la base de datos
        // Si existe lo actualizamos, son cosas que veremos!!!
        return pedidosRepository.save(pedidoToSave);
    }
    Pedido reserveStockPedidos(Pedido pedido) {
        if (pedido.getLineasPedido() == null || pedido.getLineasPedido().isEmpty()) {
            throw new PedidoNoItems(pedido.getId().toHexString());
        }

        pedido.getLineasPedido().forEach(lineaPedido -> {
            var producto = funkosRepository.findById(lineaPedido.getIdProducto()).get(); // Siempre existe porque ha pasado el check
            // Si existe, comprobamos si hay stock
            producto.setCantidad(producto.getCantidad() - lineaPedido.getCantidad());
            // producto.setStock(producto.getStock() - lineaPedido.getCantidad());
            funkosRepository.save(producto);
            // Actualizamos el total de la linea de pedido
            lineaPedido.setTotal(lineaPedido.getCantidad() * lineaPedido.getPrecioProducto());
        });

        // Calculamos el total del pedido
        var total = pedido.getLineasPedido().stream()
                .map(lineaPedido -> lineaPedido.getCantidad() * lineaPedido.getPrecioProducto())
                .reduce(0.0, Double::sum);

        // Calculamos el total de items del pedido
        var totalItems = pedido.getLineasPedido().stream()
                .map(LineaPedido::getCantidad)
                .reduce(0, Integer::sum);

        // Actualizamos el total del pedido y el total de items
        pedido.setTotal(total);
        pedido.setTotalItems(totalItems);

        return pedido;
    }
    Pedido returnStockPedidos(Pedido pedido) {
        if (pedido.getLineasPedido() != null) {
            pedido.getLineasPedido().forEach(lineaPedido -> {
                var producto = funkosRepository.findById(lineaPedido.getIdProducto()).get(); // Siempre existe porque ha pasado el check
                // Si existe, comprobamos si hay stock
                producto.setCantidad(producto.getCantidad() + lineaPedido.getCantidad());
                // producto.setStock(producto.getStock() + lineaPedido.getCantidad());
                funkosRepository.save(producto);
            });
        }
        return pedido;
    }
    void checkPedido(Pedido pedido) {
        if (pedido.getLineasPedido() == null || pedido.getLineasPedido().isEmpty()) {
            throw new PedidoNoItems(pedido.getId().toHexString());
        }
        pedido.getLineasPedido().forEach(lineaPedido -> {
            var producto = funkosRepository.findById(lineaPedido.getIdProducto())
                    .orElseThrow(() -> new FunkoNotFound(lineaPedido.getIdProducto()));
            // Si existe, comprobamos si hay stock
            if (producto.getCantidad() < lineaPedido.getCantidad() && lineaPedido.getCantidad() > 0) {
                throw new FunkoNotStock(lineaPedido.getIdProducto());
            }
            // Podemos comprobar más cosas, como si el precio es el mismo, etc...
            if (!producto.getPrecio().equals(lineaPedido.getPrecioProducto())) {
                throw new FunkoBadPrice(lineaPedido.getIdProducto());
            }
        });
    }
}
