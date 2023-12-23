package sia.tacocloud;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
public class Ingredient
        implements Persistable<String> {

    // clin: because now we create Ingredient objects via CommandLineRunner()
    // in TacoCloudApplication(), now these attributes cannot be "final"
    @Id
    private String id;

    private String name;
    private Type type;

    @Override
    public boolean isNew() {
        // clin: what is the purpose of this?
        return true;
    }

    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
