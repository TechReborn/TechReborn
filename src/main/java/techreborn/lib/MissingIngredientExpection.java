package techreborn.lib;

/**
 * Created by Mark on 05/01/2017.
 */
public class MissingIngredientExpection extends RuntimeException {

	public MissingIngredientExpection() {
	}

	public MissingIngredientExpection(String message) {
		super(message);
	}
}
