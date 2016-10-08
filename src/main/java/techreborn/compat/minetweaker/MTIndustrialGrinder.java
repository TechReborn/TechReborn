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

@ZenClass("mods.techreborn.grinder")
public class MTIndustrialGrinder extends MTGeneric {

	@ZenMethod
	public static void addRecipe(IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, IIngredient input1, IIngredient input2, int ticktime, int euTick) {
		addRecipe(output1, output2, output3, output4, input1, input2, null, ticktime, euTick);
	}

	@ZenMethod
	public static void addRecipe(IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, IIngredient input1, IIngredient input2, ILiquidStack fluid, int ticktime, int euTick) {
		ItemStack oInput1 = (ItemStack) MinetweakerCompat.toObject(input1);

		ItemStack oInput2 = (ItemStack) MinetweakerCompat.toObject(input2);

		FluidStack fluidStack = null;
		if (fluid != null) {
			fluidStack = MinetweakerCompat.toFluidStack(fluid);
		}

		//FIXME 25.07.2016
		//IndustrialGrinderRecipe r = new IndustrialGrinderRecipe(oInput1, oInput2, fluidStack, MinetweakerCompat.toStack(output1), MinetweakerCompat.toStack(output3), MinetweakerCompat.toStack(output4), ticktime, euTick);
		//addRecipe(r);
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
		return Reference.industrialGrinderRecipe;
	}
}
