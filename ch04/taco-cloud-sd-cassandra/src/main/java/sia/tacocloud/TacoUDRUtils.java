package sia.tacocloud;

public class TacoUDRUtils {


    public static TacoUDT toTacoUDT(Taco taco) {
        return new TacoUDT(taco.getName(), taco.getIngredients());
    }



    public static IngredientUDT toIngredientUDT(Ingredient ingredient) {
        return new IngredientUDT(ingredient.getName(), ingredient.getType());
    }
}
