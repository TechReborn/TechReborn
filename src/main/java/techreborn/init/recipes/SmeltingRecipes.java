package techreborn.init.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import reborncore.common.util.CraftingHelper;
import techreborn.blocks.BlockOre;
import techreborn.blocks.BlockOre2;
import techreborn.init.IC2Duplicates;

/**
 * Created by Prospector
 */
public class SmeltingRecipes extends RecipeMethods {
	public static void init() {
		register(getMaterial("iron", Type.DUST), new ItemStack(Items.IRON_INGOT));
		register(getMaterial("gold", Type.DUST), new ItemStack(Items.GOLD_INGOT));
		register(getMaterial("sap", Type.PART), getMaterial("rubber", Type.PART));
		if (!IC2Duplicates.deduplicate()) {
			register(new ItemStack(Items.IRON_INGOT), getMaterial("refined_iron", Type.INGOT));
		}
		register(BlockOre2.getOreByName("copper"), getMaterial("copper", Type.INGOT));
		register(BlockOre2.getOreByName("tin"), getMaterial("tin", Type.INGOT));
		register(BlockOre.getOreByName("silver"), getMaterial("silver", Type.INGOT));
		register(BlockOre.getOreByName("lead"), getMaterial("lead", Type.INGOT));
		register(BlockOre.getOreByName("sheldonite"), getMaterial("platinum", Type.INGOT));
		register(IC2Duplicates.MIXED_METAL.getStackBasedOnConfig(), getMaterial("advanced_alloy", Type.INGOT));
		register(getMaterial("nickel", Type.DUST), getMaterial("nickel", Type.INGOT));
		register(getMaterial("platinum", Type.DUST), getMaterial("platinum", Type.INGOT));
		register(getMaterial("zinc", Type.DUST), getMaterial("zinc", Type.INGOT));
	}

	static void register(ItemStack input, ItemStack output) {
		CraftingHelper.addSmelting(input, output);
	}
}
