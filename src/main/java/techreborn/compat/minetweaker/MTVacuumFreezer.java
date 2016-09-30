package techreborn.compat.minetweaker;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.Reference;
import techreborn.api.recipe.machines.VacuumFreezerRecipe;

@ZenClass("mods.techreborn.vacuumFreezer")
public class MTVacuumFreezer extends MTGeneric {

	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient input, int ticktime, int euTick) {
		ItemStack oInput1 = (ItemStack) MinetweakerCompat.toObject(input);

		VacuumFreezerRecipe r = new VacuumFreezerRecipe(oInput1, MinetweakerCompat.toStack(output), ticktime, euTick);
		addRecipe(r);
	}

	@ZenMethod
	public static void removeInputRecipe(IIngredient iIngredient) {
		MineTweakerAPI.apply(new RemoveInput(iIngredient, getMachineName()));
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		MineTweakerAPI.apply(new Remove(MinetweakerCompat.toStack(output), getMachineName()));
	}

	public static String getMachineName() {
		return Reference.vacuumFreezerRecipe;
	}
}
