package praktikum.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private List<String> ingredients = new ArrayList<>();
    public void addIngredients(String[] ingredientsArray) {
        Collections.addAll(ingredients, ingredientsArray);
    }
}
