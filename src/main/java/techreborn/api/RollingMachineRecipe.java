package techreborn.api;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RollingMachineRecipe {

	public static final RollingMachineRecipe instance = new RollingMachineRecipe();
	private final List<IRecipe> recipes = new ArrayList<>();

	public void addShapedOreRecipe(ItemStack outputItemStack, Object... objectInputs) {
		Validate.notNull(outputItemStack);
		Validate.notNull(outputItemStack.getItem());
		if (objectInputs.length == 0) {
			Validate.notNull(null); //Quick way to crash
		}
		recipes.add(new ShapedOreRecipe(outputItemStack, objectInputs));
	}

	public void addShapelessOreRecipe(ItemStack outputItemStack, Object... objectInputs) {
		Validate.notNull(outputItemStack);
		Validate.notNull(outputItemStack.getItem());
		if (objectInputs.length == 0) {
			Validate.notNull(null); //Quick way to crash
		}
		recipes.add(new ShapelessOreRecipe(outputItemStack, objectInputs));
	}

	public void addRecipe(ItemStack output, Object... components) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		if (components[i] instanceof String[]) {
			String as[] = (String[]) components[i++];
			for (int l = 0; l < as.length; l++) {
				String s2 = as[l];
				k++;
				j = s2.length();
				s = (new StringBuilder()).append(s).append(s2).toString();
			}
		} else {
			while (components[i] instanceof String) {
				String s1 = (String) components[i++];
				k++;
				j = s1.length();
				s = (new StringBuilder()).append(s).append(s1).toString();
			}
		}
		HashMap hashmap = new HashMap();
		for (; i < components.length; i += 2) {
			Character character = (Character) components[i];
			ItemStack itemstack1 = null;
			if (components[i + 1] instanceof Item) {
				itemstack1 = new ItemStack((Item) components[i + 1]);
			} else if (components[i + 1] instanceof Block) {
				itemstack1 = new ItemStack((Block) components[i + 1], 1, -1);
			} else if (components[i + 1] instanceof ItemStack) {
				itemstack1 = (ItemStack) components[i + 1];
			}
			hashmap.put(character, itemstack1);
		}

		ItemStack recipeArray[] = new ItemStack[j * k];
		for (int i1 = 0; i1 < j * k; i1++) {
			char c = s.charAt(i1);
			if (hashmap.containsKey(c)) {
				recipeArray[i1] = ((ItemStack) hashmap.get(c)).copy();
			} else {
				recipeArray[i1] = null;
			}
		}

		recipes.add(new ShapedRecipes(j, k, recipeArray, output));
	}

	public void addShapelessRecipe(ItemStack output, Object... components) {
		List<ItemStack> ingredients = new ArrayList<>();
		for (int j = 0; j < components.length; j++) {
			Object obj = components[j];
			if (obj instanceof ItemStack) {
				ingredients.add(((ItemStack) obj).copy());
				continue;
			}
			if (obj instanceof Item) {
				ingredients.add(new ItemStack((Item) obj));
				continue;
			}
			if (obj instanceof Block) {
				ingredients.add(new ItemStack((Block) obj));
			} else {
				throw new RuntimeException("Invalid shapeless recipe!");
			}
		}

		recipes.add(new ShapelessRecipes(output, ingredients));
	}

	public ItemStack findMatchingRecipe(InventoryCrafting inv, World world) {
		for (int k = 0; k < recipes.size(); k++) {
			IRecipe irecipe = recipes.get(k);
			if (irecipe.matches(inv, world)) {
				return irecipe.getCraftingResult(inv);
			}
		}

		return null;
	}

	public List<IRecipe> getRecipeList() {
		return recipes;
	}

}
