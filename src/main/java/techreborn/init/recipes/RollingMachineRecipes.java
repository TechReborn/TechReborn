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
		if (oresExist("ingotAluminium")) {
			register(getMaterial("kanthal_heating_coil", 3, Type.PART), "RRR", "CAA", "CCA", 'R', "ingotRefinedIron", 'C', "ingotChrome", 'A', "ingotAluminium");
		}
		register(getStack(Blocks.RAIL, 24), "I I", "ISI", "I I", 'I', "ingotIron", 'S', "stickWood");
		register(getStack(Blocks.GOLDEN_RAIL, 8), "I I", "ISI", "IRI", 'I', "ingotGold", 'S', "stickWood", 'R', "dustRedstone");
		register(getStack(Blocks.DETECTOR_RAIL, 8), "I I", "IPI", "IRI", 'I', "ingotIron", 'P', getStack(Blocks.STONE_PRESSURE_PLATE), 'R', "dustRedstone");
		register(getStack(Blocks.ACTIVATOR_RAIL, 8), "ISI", "IRI", "ISI", 'I', "ingotIron", 'S', "stickWood", 'R', getStack(Blocks.REDSTONE_TORCH));
		register(getStack(Blocks.IRON_BARS, 24), "III", "III", 'I', "ingotIron");
		register(getStack(Items.IRON_DOOR, 4), "II ", "II ", "II ", 'I', "ingotIron");
		register(getStack(Items.MINECART, 2), "I I", "III", 'I', "ingotIron");
		register(getStack(Items.BUCKET, 2), "I I", "I I", " I ", 'I', "ingotIron");
		register(getStack(Blocks.TRIPWIRE_HOOK, 4), " I ", " S ", " W ", 'I', "ingotIron", 'S', "stickWood", 'W', "plankWood");
		register(getStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 2), "II ", 'I', "ingotIron");
		register(getStack(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, 2), "GG ", 'G', "ingotGold");
	}

	static void register(ItemStack output, Object... componentsObjects) {
		TechRebornAPI.addRollingOreMachinceRecipe(output, componentsObjects);
	}
}
