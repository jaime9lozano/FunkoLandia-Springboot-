package jaime.funkoext2.ventas.repository;

import jaime.funkoext2.ventas.models.Pedido;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidosRepository extends MongoRepository<Pedido, ObjectId> {
    Page<Pedido> findByIdUsuario(Long idUsuario, Pageable pageable);

    // Solo queremos el id del pedido dado el id del usuario
    List<Pedido> findPedidosIdsByIdUsuario(Long idUsuario);

    // existe un producto con el mismo id de Usuario
    boolean existsByIdUsuario(Long idUsuario);
}
