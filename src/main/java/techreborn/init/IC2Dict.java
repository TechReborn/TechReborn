package techreborn.init;

import ic2.core.block.BlockIC2Fence;
import ic2.core.block.BlockTexGlass;
import ic2.core.block.type.ResourceBlock;
import ic2.core.item.type.CraftingItemType;
import ic2.core.item.type.MiscResourceType;
import ic2.core.item.type.NuclearResourceType;
import ic2.core.ref.BlockName;
import ic2.core.ref.ItemName;
import ic2.core.ref.TeBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.Core;

/**
 * Created by modmuss50 on 16/07/2016.
 */
public class IC2Dict {

	public static void init() {
		try {
			CraftingItemType.circuit.getName();

			OreDictionary.registerOre("reBattery", ItemName.re_battery.getItemStack());

			OreDictionary.registerOre("circuitBasic", ItemName.crafting.getItemStack(CraftingItemType.circuit));
			OreDictionary.registerOre("circuitAdvanced", ItemName.crafting.getItemStack(CraftingItemType.advanced_circuit));

			OreDictionary.registerOre("machineBlockBasic", BlockName.resource.getItemStack(ResourceBlock.machine));
			OreDictionary.registerOre("machineBlockAdvanced", BlockName.resource.getItemStack(ResourceBlock.advanced_machine));

			OreDictionary.registerOre("lapotronCrystal", ItemName.lapotron_crystal.getItemStack());
			OreDictionary.registerOre("energyCrystal", ItemName.lapotron_crystal.getItemStack());

			OreDictionary.registerOre("drillBasic", ItemName.drill.getItemStack());
			OreDictionary.registerOre("drillDiamond", ItemName.diamond_drill.getItemStack());
			OreDictionary.registerOre("drillAdvanced", ItemName.iridium_drill.getItemStack());

			ItemStack industrialTnt = BlockName.te.getItemStack(TeBlock.itnt);
			industrialTnt.setItemDamage(1);
			OreDictionary.registerOre("industrialTnt", industrialTnt);

			OreDictionary.registerOre("craftingIndustrialDiamond", ItemName.crafting.getItemStack(CraftingItemType.industrial_diamond));
			OreDictionary.registerOre("fertilizer", ItemName.crafting.getItemStack(CraftingItemType.bio_chaff));
			OreDictionary.registerOre("hvTransformer", BlockName.te.getItemStack(TeBlock.hv_transformer));

			//TODO:

			//OreDictionary.registerOre("insulatedGoldCableItem", BlockName.te.getItemStack(CableType.gold));

			//OreDictionary.registerOre("ic2Generator", ModBlocks.generator);
			//OreDictionary.registerOre("ic2SolarPanel", ModBlocks.solarPanel);
			//OreDictionary.registerOre("ic2Macerator", ModBlocks.grinder);
			//OreDictionary.registerOre("ic2Extractor", ModBlocks.extractor);
			//OreDictionary.registerOre("ic2Windmill", ModBlocks.windMill);
			//OreDictionary.registerOre("ic2Watermill", ModBlocks.waterMill);

			OreDictionary.registerOre("uran235", ItemName.nuclear.getItemStack(NuclearResourceType.uranium_235));
			OreDictionary.registerOre("uran238", ItemName.nuclear.getItemStack(NuclearResourceType.uranium_238));
			OreDictionary.registerOre("smallUran238", ItemName.nuclear.getItemStack(NuclearResourceType.small_uranium_238));
			OreDictionary.registerOre("smallUran235", ItemName.nuclear.getItemStack(NuclearResourceType.small_uranium_235));

			OreDictionary.registerOre("fenceIron", BlockName.fence.getItemStack(BlockIC2Fence.IC2FenceType.iron));
			OreDictionary.registerOre("rubberWood", BlockName.rubber_wood.getItemStack());
			OreDictionary.registerOre("glassReinforced", BlockName.glass.getItemStack(BlockTexGlass.GlassType.reinforced));

			OreDictionary.registerOre("oreIridium", ItemName.misc_resource.getItemStack(MiscResourceType.iridium_ore));
		} catch (NoClassDefFoundError notFound) {
			Core.logHelper.warn(
				"Can't enable integration: IC2 installed but cannot be hooked\n" +
					"Do you use incompatible IC2 version?\n" +
					"Please create issue on github and provide FULL LOG and mod list");
		} catch (Throwable error) {
			Core.logHelper.warn(
				"Exception thrown during IC2 integration init\n" +
					"Do you use incompatible IC2 version?\n" +
					"Please create issue on github and provide FULL LOG and mod list.\n" +
					"Error stack trace: ");
			error.printStackTrace();
		}
	}
}
