package sia.tacocloud;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force=true)
public class Ingredient {

    // clin: because now we create Ingredient objects via CommandLineRunner()
    // in TacoCloudApplication(), now these attributes cannot be "final"
    @Id
    private String id;

    private String name;
    private Type type;

    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
