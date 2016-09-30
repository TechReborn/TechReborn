package techreborn.compat.minetweaker;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.Reference;
import techreborn.api.recipe.machines.IndustrialSawmillRecipe;

@ZenClass("mods.techreborn.industrialSawmill")
public class MTIndustrialSawmill extends MTGeneric {

	@ZenMethod
	public static void addRecipe(IItemStack output1, IItemStack output2, IItemStack output3, IIngredient input1, IIngredient input2, ILiquidStack fluid, int ticktime, int euTick) {
		addRecipe(output1, output2, output3, input1, input2, fluid, ticktime, euTick, true);
	}

	@ZenMethod
	public static void addRecipe(IItemStack output1, IItemStack output2, IItemStack output3, IIngredient input1, IIngredient input2, int ticktime, int euTick) {
		addRecipe(output1, output2, output3, input1, input2, null, ticktime, euTick, true);
	}

	@ZenMethod
	public static void addRecipe(IItemStack output1, IItemStack output2, IItemStack output3, IIngredient input1, IIngredient input2, int ticktime, int euTick, boolean useOreDic) {
		addRecipe(output1, output2, output3, input1, input2, null, ticktime, euTick, useOreDic);
	}

	@ZenMethod
	public static void addRecipe(IItemStack output1, IItemStack output2, IItemStack output3, IIngredient input1, IIngredient input2, ILiquidStack fluid, int ticktime, int euTick, boolean useOreDic) {
		ItemStack oInput1 = (ItemStack) MinetweakerCompat.toObject(input1);

		ItemStack oInput2 = (ItemStack) MinetweakerCompat.toObject(input2);

		FluidStack fluidStack = null;
		if (fluid != null) {
			fluidStack = MinetweakerCompat.toFluidStack(fluid);
		}

		IndustrialSawmillRecipe r = new IndustrialSawmillRecipe(oInput1, oInput2, fluidStack, MinetweakerCompat.toStack(output1), MinetweakerCompat.toStack(output2), MinetweakerCompat.toStack(output3), ticktime, euTick, useOreDic);

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
		return Reference.industrialSawmillRecipe;
	}
}
