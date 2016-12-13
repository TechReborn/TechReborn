package techreborn.init;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.Core;
import techreborn.blocks.BlockMachineFrame;
import techreborn.items.*;
import techreborn.parts.powerCables.ItemStandaloneCables;
import techreborn.utils.OreDictUtils;

public class OreDict {

	private static final ImmutableList<String> plateGenIgnores = ImmutableList.of(
		"hot", //Hot ingots
		"mixed_metal", //Mixed metal has own version of plate
		"iridium_alloy", //Iridium alloy is plate itself
		ModItems.META_PLACEHOLDER //...
	);

	public static void init() {
		if (Loader.isModLoaded("IC2")) {
			Core.logHelper.info("IC2 installed, enabling integration");
			IC2Dict.init();
		}

		OreDictionary.registerOre("reBattery", ModItems.reBattery);

		OreDictionary.registerOre("circuitBasic", ItemParts.getPartByName("electronicCircuit"));
		OreDictionary.registerOre("circuitAdvanced", ItemParts.getPartByName("advancedCircuit"));
		OreDictionary.registerOre("circuitStorage", ItemParts.getPartByName("dataStorageCircuit"));
		OreDictionary.registerOre("circuitElite", ItemParts.getPartByName("dataControlCircuit"));
		OreDictionary.registerOre("circuitMaster", ItemParts.getPartByName("energyFlowCircuit"));

		OreDictionary.registerOre("machineBlockBasic", BlockMachineFrame.getFrameByName("machine", 1));
		OreDictionary.registerOre("machineBlockAdvanced", BlockMachineFrame.getFrameByName("advancedMachine", 1));
		OreDictionary.registerOre("machineBlockElite", BlockMachineFrame.getFrameByName("highlyAdvancedMachine", 1));

		OreDictionary.registerOre("lapotronCrystal", ModItems.lapotronCrystal);
		OreDictionary.registerOre("energyCrystal", ModItems.energyCrystal);

		OreDictionary.registerOre("drillBasic", ModItems.diamondDrill);
		OreDictionary.registerOre("drillDiamond", ModItems.diamondDrill);

		OreDictionary.registerOre("industrialTnt", Blocks.TNT);
		OreDictionary.registerOre("craftingIndustrialDiamond", Items.DIAMOND);
		OreDictionary.registerOre("insulatedGoldCableItem", ItemStandaloneCables.getCableByName("insulatedgold"));
		OreDictionary.registerOre("fertilizer", new ItemStack(Items.DYE, 1, 15));

		OreDictionary.registerOre("ic2Generator", ModBlocks.generator);
		OreDictionary.registerOre("ic2SolarPanel", ModBlocks.solarPanel);
		OreDictionary.registerOre("ic2Macerator", ModBlocks.grinder);
		OreDictionary.registerOre("ic2Extractor", ModBlocks.extractor);
		OreDictionary.registerOre("ic2Windmill", ModBlocks.windMill);
		OreDictionary.registerOre("ic2Watermill", ModBlocks.waterMill);

		//OreDictionary.registerOre("uran235", nothing);
		//OreDictionary.registerOre("uran238", nothing);
		//OreDictionary.registerOre("smallUran235", nothing);

		OreDictionary.registerOre("fenceIron", ModBlocks.ironFence);
		OreDictionary.registerOre("woodRubber", ModBlocks.rubberLog);
		OreDictionary.registerOre("glassReinforced", ModBlocks.reinforcedglass);

		OreDictionary.registerOre("diamondTR", ItemDusts.getDustByName("Diamond"));
		OreDictionary.registerOre("diamondTR", Items.DIAMOND);

		OreDictionary.registerOre("craftingGrinder", ItemParts.getPartByName("diamondGrindingHead"));
		OreDictionary.registerOre("craftingGrinder", ItemParts.getPartByName("tungstenGrindingHead"));
		OreDictionary.registerOre("craftingSuperconductor", ItemParts.getPartByName("superconductor"));
		OreDictionary.registerOre("batteryUltimate", ItemParts.getPartByName("diamondGrindingHead"));

		OreDictionary.registerOre("materialResin", ItemParts.getPartByName("rubberSap"));
		OreDictionary.registerOre("materialRubber", ItemParts.getPartByName("rubber"));
		OreDictionary.registerOre("pulpWood", ItemDusts.getDustByName("saw_dust"));

		for (String type : ItemGems.types) {
			if (type.equals(ModItems.META_PLACEHOLDER))
				continue; //Aware of placeholders!
			OreDictionary.registerOre("gem" + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "gem_" + type), ItemGems.getGemByName(type));
			boolean ignoreIt = false;
			for (String ignore : plateGenIgnores)
				if (type.startsWith(ignore))
					ignoreIt = true;
			if (!ignoreIt)
				ItemPlates.registerType(type);
		}

		for (String type : ItemIngots.types) {
			if (type.equals(ModItems.META_PLACEHOLDER))
				continue; //Aware of placeholders!
			OreDictionary.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "ingot_" + type), ItemIngots.getIngotByName(type));
			boolean ignoreIt = false;
			for (String ignore : plateGenIgnores)
				if (type.startsWith(ignore))
					ignoreIt = true;
			if (!ignoreIt)
				ItemPlates.registerType(type);
		}

		for (String type : ItemPlates.types) {
			if (type.equals(ModItems.META_PLACEHOLDER))
				continue; //Aware of placeholders!
			OreDictionary.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "plate_" + type), ItemPlates.getPlateByName(type));
		}

		for (String type : ItemDusts.types) {
			if (type.equals(ModItems.META_PLACEHOLDER))
				continue; //Aware of placeholders!
			OreDictionary.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "dust_" + type), ItemDusts.getDustByName(type));
		}

		for (String type : ItemDustsSmall.types) {
			if (type.equals(ModItems.META_PLACEHOLDER))
				continue; //Aware of placeholders!
			OreDictionary.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "dust_tiny_" + type), ItemDustsSmall.getSmallDustByName(type));
			OreDictionary.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "dust_small_" + type), ItemDustsSmall.getSmallDustByName(type));
		}

		for (String type : ItemDustsSmall.types) {
			if (type.equals(ModItems.META_PLACEHOLDER))
				continue; //Aware of placeholders!
			String oreDictName = "dustSmall" + OreDictUtils.toFirstUpper(type);
			OreDictionary.registerOre(oreDictName, ItemDusts.getDustByName(type));
		}

		for (String type : ItemNuggets.types) {
			if (type.equals(ModItems.META_PLACEHOLDER))
				continue; //Aware of placeholders!
			OreDictionary.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "nugget_" + type), ItemNuggets.getNuggetByName(type));
		}

	}

}
