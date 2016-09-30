package techreborn.compat.minetweaker;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.Reference;
import techreborn.api.recipe.machines.AlloySmelterRecipe;

/**
 * mods.techreborn.alloySmelter.addRecipe(<minecraft:gold_ingot>, <minecraft:iron_ingot>, <minecraft:diamond>, 20, 100);
 */

@ZenClass("mods.techreborn.alloySmelter")
public class MTAlloySmelter extends MTGeneric {

	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient input1, IIngredient input2, int ticktime, int euTick) {
		ItemStack oInput1 = (ItemStack) MinetweakerCompat.toObject(input1);
		ItemStack oInput2 = (ItemStack) MinetweakerCompat.toObject(input2);

		AlloySmelterRecipe r = new AlloySmelterRecipe(oInput1, oInput2, MinetweakerCompat.toStack(output), ticktime, euTick);

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
		return Reference.alloySmelteRecipe;
	}
}
