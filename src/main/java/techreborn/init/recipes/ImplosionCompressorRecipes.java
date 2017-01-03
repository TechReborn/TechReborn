package techreborn.init.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import reborncore.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.ImplosionCompressorRecipe;

import java.security.InvalidParameterException;

/**
 * Created by Prospector
 */
public class ImplosionCompressorRecipes extends RecipeMethods {
	public static void init() {
		register(getOre("ingotIridiumAlloy"), getMaterial("iridium", Type.PLATE), 4);
		register(getOre("dustDiamond", 4), new ItemStack(Items.DIAMOND, 3), 16);
		register(getOre("dustEmerald", 4), new ItemStack(Items.EMERALD, 3), 12);
		register(getOre("dustRuby", 4), getMaterial("ruby", 3, Type.GEM), 12);
		register(getOre("dustSapphire", 4), getMaterial("sapphire", 3, Type.GEM), 12);
		register(getOre("dustPeridot", 4), getMaterial("peridot", 3, Type.GEM), 12);
		register(getOre("dustRedGarnet", 4), getMaterial("red_garnet", 3, Type.GEM), 8);
		register(getOre("dustYellowGarnet", 4), getMaterial("yellow_garnet", 3, Type.GEM), 8);
		if (oresExist("dustApatite", "gemApatite")) {
			register(getOre("dustApatite", 4), getOre("gemApatite", 3), 12);
		}

		if (oresExist("dustCertusQuartz", "crystalCertusQuartz")) {
			register(getOre("dustCertusQuartz", 4), getOre("crystalCertusQuartz", 3), 12);
		}
		if (oresExist("dustAmethyst", "gemAmethyst")) {
			register(getOre("dustAmethyst", 4), getOre("gemAmethyst", 3), 12);
		}

		if (oresExist("dustTopaz", "gemTopaz")) {
			register(getOre("dustTopaz", 4), getOre("gemTopaz", 3), 12);
		}

		if (oresExist("dustTanzanite", "gemTanzanite")) {
			register(getOre("dustTanzanite", 4), getOre("gemTanzanite", 3), 12);
		}

		if (oresExist("dustMalachite", "gemMalachite")) {
			register(getOre("dustMalachite", 4), getOre("gemMalachite", 3), 12);
		}
	}

	static void register(ItemStack input, ItemStack output, int darkAshes) {
		if (darkAshes < 1 || darkAshes > 64) {
			throw new InvalidParameterException("Invalid implosion compressor darkAshes input: " + darkAshes);
		}

		RecipeHandler.addRecipe(new ImplosionCompressorRecipe(input, new ItemStack(Blocks.TNT, 16), output, getMaterial("dark_ashes", darkAshes, Type.DUST), 20, 32));
	}
}
