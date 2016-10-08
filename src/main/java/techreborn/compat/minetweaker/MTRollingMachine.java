package techreborn.compat.minetweaker;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import reborncore.common.util.ItemUtils;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.RollingMachineRecipe;
import techreborn.api.TechRebornAPI;

import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.techreborn.rollingMachine")
public class MTRollingMachine {

	@ZenMethod
	public static void addShaped(IItemStack output, IIngredient[][] ingredients) {
		TechRebornAPI.addRollingOreMachinceRecipe(toStack(output), toShapedObjects(ingredients));
	}

	@ZenMethod
	public static void addShapeless(IItemStack output, IIngredient[] ingredients) {
		TechRebornAPI.addShapelessOreRollingMachinceRecipe(toStack(output), toObjects(ingredients));
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		List<IRecipe> toRemove = new ArrayList<>();
		for (IRecipe recipe : RollingMachineRecipe.instance.getRecipeList()) {
			if (ItemUtils.isItemEqual(recipe.getRecipeOutput(), MinetweakerCompat.toStack(output), true, false)) {
				toRemove.add(recipe);
			}
		}
		RollingMachineRecipe.instance.getRecipeList().removeAll(toRemove);
	}

	public static ItemStack toStack(IItemStack iStack) {
		if (iStack == null)
			return null;
		else {
			Object internal = iStack.getInternal();
			if (internal == null || !(internal instanceof ItemStack)) {
				MineTweakerAPI.getLogger().logError("Not a valid item stack: " + iStack);
			}

			return (ItemStack) internal;
		}
	}

	public static ItemStack[] toStacks(IItemStack[] iStack) {
		if (iStack == null)
			return null;
		else {
			ItemStack[] output = new ItemStack[iStack.length];
			for (int i = 0; i < iStack.length; i++) {
				output[i] = toStack(iStack[i]);
			}

			return output;
		}
	}

	public static Object toObject(IIngredient iStack) {
		if (iStack == null)
			return null;
		else {
			if (iStack instanceof IOreDictEntry) {
				return toString((IOreDictEntry) iStack);
			} else if (iStack instanceof IItemStack) {
				return toStack((IItemStack) iStack);
			} else
				return null;
		}
	}

	public static Object[] toObjects(IIngredient[] ingredient) {
		if (ingredient == null)
			return null;
		else {
			Object[] output = new Object[ingredient.length];
			for (int i = 0; i < ingredient.length; i++) {
				if (ingredient[i] != null) {
					output[i] = toObject(ingredient[i]);
				} else
					output[i] = "";
			}

			return output;
		}
	}

	public static Object[] toShapedObjects(IIngredient[][] ingredients) {
		if (ingredients == null)
			return null;
		else {
			ArrayList prep = new ArrayList();
			prep.add("abc");
			prep.add("def");
			prep.add("ghi");
			char[][] map = new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
			for (int x = 0; x < ingredients.length; x++) {
				if (ingredients[x] != null) {
					for (int y = 0; y < ingredients[x].length; y++) {
						if (ingredients[x][y] != null && x < map.length && y < map[x].length) {
							prep.add(map[x][y]);
							prep.add(toObject(ingredients[x][y]));
						}
					}
				}
			}
			return prep.toArray();
		}
	}

	public static String toString(IOreDictEntry entry) {
		return ((IOreDictEntry) entry).getName();
	}
}
