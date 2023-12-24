package sia.tacocloud;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table
public class IngredientRef {
    // "Ingredient_Ref" table has no identity column,
    // so there is no need to annotate anything in IngredientRef with @Id
    private final String ingredient;
}
