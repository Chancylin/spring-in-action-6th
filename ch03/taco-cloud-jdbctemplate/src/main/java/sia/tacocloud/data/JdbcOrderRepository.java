package sia.tacocloud.data;

import org.springframework.asm.Type;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import sia.tacocloud.Ingredient;
import sia.tacocloud.IngredientRef;
import sia.tacocloud.Taco;
import sia.tacocloud.TacoOrder;

import java.util.Arrays;
import java.util.Date;
import java.sql.Types;
import java.util.List;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private JdbcOperations jdbcOperations;

    public JdbcOrderRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public TacoOrder save(TacoOrder order) {

        // describe the insert query
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
            "insert into Taco_Order "
                + "(delivery_name, delivery_street, delivery_city, "
                + "delivery_state, delivery_zip, cc_number, "
                + "cc_expiration, cc_cvv, placed_at) "
                + "values (?,?,?,?,?,?,?,?,?)",
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
        );
        pscf.setReturnGeneratedKeys(true);  // since we later need to fetch the saved order’s ID

        order.setPlacedAt(new Date());
        /*
            have a PreparedStatementCreator in hand,
            you’re ready to actually save the order data by calling the update() method on JdbcTemplate,
         */

        PreparedStatementCreator psc =
                pscf.newPreparedStatementCreator(
                        Arrays.asList(
                            order.getDeliveryName(),
                            order.getDeliveryStreet(),
                            order.getDeliveryCity(),
                            order.getDeliveryState(),
                            order.getDeliveryZip(),
                            order.getCcNumber(),
                            order.getCcExpiration(),
                            order.getCcCVV(),
                            order.getPlacedAt()
                        )
                );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        // GeneratedKeyHolder will contain the value of the id field as assigned by the database
        jdbcOperations.update(psc, keyHolder);

        long orderId = keyHolder.getKey().longValue();
        order.setId(orderId);

        // we also need to save the Taco objects associated with the order
        List<Taco> tacos = order.getTacos();
        int i = 0;
        for (Taco taco : tacos) {
            saveTaco(orderId, i++, taco);
        }

        return order;

    }

    private long saveTaco(Long orderId, int orderKey, Taco taco) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into Taco "
                + "(name, created_at, taco_order, taco_order_key) "
                + "values (?, ?, ?, ?)",
                Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG
        );
        pscf.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc =
                pscf.newPreparedStatementCreator(
                        Arrays.asList(
                                taco.getName(),
                                taco.getCreatedAt(),
                                orderId,
                                orderKey
                        )
                );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long tacoId = keyHolder.getKey().longValue();
        taco.setId(tacoId);
        
        saveIngredientRefs(tacoId, taco.getIngredients());
        
        return tacoId;
    }

    private void saveIngredientRefs(long tacoId, List<IngredientRef> ingredientRefs) {
        int key = 0;
        for (IngredientRef ingredientRef : ingredientRefs) {
            jdbcOperations.update(
                    "insert into Ingredient_Ref (ingredient, taco, taco_key) "
                    + "values (?, ?, ?)",
                    ingredientRef.getIngredient(), tacoId, key++
            );
        }
    }
}
