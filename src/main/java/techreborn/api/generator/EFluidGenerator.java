package techreborn.api.generator;

import javax.annotation.Nonnull;

public enum EFluidGenerator {
	THERMAL("TechReborn.ThermalGenerator"), GAS("TechReborn.GasGenerator"), DIESEL(
			"TechReborn.DieselGenerator"), SEMIFLUID("TechReborn.SemifluidGenerator");

	@Nonnull
	private final String recipeID;

	private EFluidGenerator(@Nonnull String recipeID) {
		this.recipeID = recipeID;
	}

	@Nonnull
	public String getRecipeID() {
		return recipeID;
	}
}
