package sia.tacocloud.data;

import org.springframework.data.repository.CrudRepository;
import sia.tacocloud.TacoOrder;

import java.util.UUID;

public interface OrderRepository
        extends CrudRepository<TacoOrder, UUID> {

}
