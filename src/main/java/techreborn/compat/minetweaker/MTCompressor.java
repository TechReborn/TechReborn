package techreborn.compat.minetweaker;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.Reference;
import techreborn.api.recipe.machines.CompressorRecipe;

/*
 * mods.techreborn.compressor.addRecipe(<minecraft:clay>,<minecraft:clay_ball>, 40,100);
 */

@ZenClass("mods.techreborn.compressor")
public class MTCompressor extends MTGeneric {

	@ZenMethod
	public static void addRecipe(IItemStack output1, IIngredient input1, int ticktime, int euTick) {
		ItemStack oInput1 = (ItemStack) MinetweakerCompat.toObject(input1);

		//public CompressorRecipe(ItemStack input1, ItemStack output1, int tickTime, int euPerTick) {
		CompressorRecipe r = new CompressorRecipe(oInput1, MinetweakerCompat.toStack(output1), ticktime, euTick);

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
		return Reference.compressorRecipe;
	}
}
