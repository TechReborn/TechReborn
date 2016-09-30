package techreborn.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import techreborn.api.recipe.IRecipeCompact;

public final class TechRebornAPI {

	/**
	 * Use this to get the instance of IRecipeCompat
	 */
	public static IRecipeCompact recipeCompact;

	public static ISubItemRetriever subItemRetriever;

	public static void addRollingOreMachinceRecipe(ItemStack output, Object... components) {
		RollingMachineRecipe.instance.addShapedOreRecipe(output, components);
	}

	public static void addShapelessOreRollingMachinceRecipe(ItemStack output, Object... components) {
		RollingMachineRecipe.instance.addShapelessOreRecipe(output, components);
	}

	public static void addRollingMachinceRecipe(ItemStack output, Object... components) {
		RollingMachineRecipe.instance.addRecipe(output, components);
	}

	public static void addShapelessRollingMachinceRecipe(ItemStack output, Object... components) {
		RollingMachineRecipe.instance.addShapelessRecipe(output, components);
	}

	/**
	 * Use this to get an item from techrebonrn, @see <a href=
	 * "https://github.com/TechReborn/TechReborn/blob/1.9/src/main/java/techreborn/init/ModItems.java">
	 * ModItems.java</a> for the full list
	 *
	 * @param name
	 * @return
	 */
	public static Item getItem(String name) {
		try {
			Object e = Class.forName("techreborn.init.ModItems").getField(name).get(null);
			return e instanceof Item ? (Item) e : null;
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Use this to get an block from techrebonrn, @see <a href=
	 * "https://github.com/TechReborn/TechReborn/blob/1.9/src/main/java/techreborn/init/ModBlocks.java">
	 * ModBlocks.java</a> for the full list
	 *
	 * @param name
	 * @return
	 */
	public static Block getBlock(String name) {
		try {
			Object e = Class.forName("techreborn.init.ModBlocks").getField(name).get(null);
			return e instanceof Block ? (Block) e : null;
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

}
