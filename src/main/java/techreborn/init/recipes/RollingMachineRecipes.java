package techreborn.init.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import techreborn.api.TechRebornAPI;

/**
 * Created by Prospector
 */
public class RollingMachineRecipes extends RecipeMethods {
	public static void init() {
		register(getMaterial("cupronickel_heating_coil", 3, Type.PART), "NCN", "C C", "NCN", 'N', "ingotNickel", 'C', "ingotCopper");
		register(getMaterial("nichrome_heating_coil", 2, Type.PART), " N ", "NCN", " N ", 'N', "ingotNickel", 'C', "ingotChrome");
		if (oresExist("ingotAluminum")) {
			register(getMaterial("kanthal_heating_coil", 3, Type.PART), "RRR", "CAA", "CCA", 'R', "ingotRefinedIron", 'C', "ingotChrome", 'A', "ingotAluminum");
		}
		if (oresExist("ingotAluminum")) {
			register(getMaterial("kanthal_heating_coil", 3, Type.PART), "RRR", "CAA", "CCA", 'R', "ingotRefinedIron", 'C', "ingotChrome", 'A', "ingotAluminium");
		}
		register(new ItemStack(Blocks.RAIL, 24), "I I", "ISI", "I I", 'I', "ingotIron", 'S', "stickWood");
		register(new ItemStack(Blocks.GOLDEN_RAIL, 8), "I I", "ISI", "IRI", 'I', "ingotIron", 'S', "stickWood", 'R', "dustRedstone");
		register(new ItemStack(Blocks.DETECTOR_RAIL, 8), "I I", "IPI", "IRI", 'I', "ingotIron", 'P', new ItemStack(Blocks.STONE_PRESSURE_PLATE), 'R', "dustRedstone");
		register(new ItemStack(Blocks.ACTIVATOR_RAIL, 8), "ISI", "IRI", "ISI", 'I', "ingotIron", 'S', "stickWood", 'R', new ItemStack(Blocks.REDSTONE_TORCH));
		register(new ItemStack(Blocks.IRON_BARS, 24), "III", "III", 'I', "ingotIron");
		register(new ItemStack(Items.IRON_DOOR, 4), "II ", "II ", "II ", 'I', "ingotIron");
		register(new ItemStack(Items.MINECART, 2), "I I", "III", 'I', "ingotIron");
		register(new ItemStack(Items.BUCKET, 2), "I I", "I I", " I ", 'I', "ingotIron");
		register(new ItemStack(Blocks.TRIPWIRE_HOOK, 4), " I ", " S ", " W ", 'I', "ingotIron", 'S', "stickWood", 'W', "plankWood");
		register(new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 2), "II ", 'I', "ingotIron");
		register(new ItemStack(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, 2), "GG ", 'G', "ingotGold");
	}

	static void register(ItemStack output, Object... componentsObjects) {
		TechRebornAPI.addRollingOreMachinceRecipe(output, componentsObjects);
	}
}
