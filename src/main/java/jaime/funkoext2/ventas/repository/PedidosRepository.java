package jaime.funkoext2.ventas.repository;

import jaime.funkoext2.ventas.models.Pedido;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidosRepository extends MongoRepository<Pedido, ObjectId> {
}
