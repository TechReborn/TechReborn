package techreborn.init;

import net.minecraft.item.ItemStack;
import techreborn.compat.CompatManager;
import techreborn.config.ConfigTechReborn;
import techreborn.items.ItemIngots;
import techreborn.items.ItemParts;
import techreborn.items.ItemUpgrades;
import techreborn.parts.powerCables.EnumStandaloneCableType;
import techreborn.parts.powerCables.ItemStandaloneCables;

/**
 * Created by Mark on 18/12/2016.
 */
public enum IC2Duplicates {

	GRINDER(new ItemStack(ModBlocks.Grinder)),
	ELECTRICAL_FURNACE(new ItemStack(ModBlocks.ElectricFurnace)),
	IRON_FURNACE(new ItemStack(ModBlocks.ironFurnace)),
	GENERATOR(new ItemStack(ModBlocks.Generator)),
	EXTRACTOR(new ItemStack(ModBlocks.Extractor)),
	SOLAR_PANEL(new ItemStack(ModBlocks.solarPanel)),
	RECYCLER(new ItemStack(ModBlocks.recycler)),
	COMPRESSOR(new ItemStack(ModBlocks.Compressor)),
	BAT_BOX(new ItemStack(ModBlocks.batBox)),
	MFE(new ItemStack(ModBlocks.mfe)),
	MFSU(new ItemStack(ModBlocks.mfsu)),
	LVT(new ItemStack(ModBlocks.lvt)),
	MVT(new ItemStack(ModBlocks.mvt)),
	HVT(new ItemStack(ModBlocks.hvt)),
	CABLE_COPPER(EnumStandaloneCableType.COPPER.getStack()),
	CABLE_GLASSFIBER(EnumStandaloneCableType.GLASSFIBER.getStack()),
	CABLE_GOLD(EnumStandaloneCableType.GOLD.getStack()),
	CABLE_HV(EnumStandaloneCableType.HV.getStack()),
	CABLE_ICOPPER(EnumStandaloneCableType.ICOPPER.getStack()),
	CABLE_IGOLD(EnumStandaloneCableType.IGOLD.getStack()),
	CABLE_IHV(EnumStandaloneCableType.IHV.getStack()),
	CABLE_IIHV(EnumStandaloneCableType.TIN.getStack()),
	UPGRADE_OVERCLOCKER(ItemUpgrades.getUpgradeByName("Overclock")),
	UPGRADE_TRANSFORMER(ItemUpgrades.getUpgradeByName("Transformer")),
	UPGRADE_STORAGE(ItemUpgrades.getUpgradeByName("EnergyStorage")),
	MIXED_METAL(ItemIngots.getIngotByName("mixedMetal")),
	CARBON_FIBER(ItemParts.getPartByName("carbonfiber")),
	CARBON_MESH(ItemParts.getPartByName("carbonmesh"));

	ItemStack ic2Stack;
	ItemStack trStack;

	IC2Duplicates(ItemStack trStack) {
		this.trStack = trStack;
	}

	IC2Duplicates(ItemStack ic2Stack, ItemStack trStack) {
		this.ic2Stack = ic2Stack;
		this.trStack = trStack;
	}

	public ItemStack getIc2Stack() {
		if(!CompatManager.isIC2Loaded){
			throw new RuntimeException("IC2 isnt loaded");
		}
		if(ic2Stack == null){
			throw new RuntimeException("IC2 stack wasnt set ");
		}
		return ic2Stack;
	}

	public void setIc2Stack(ItemStack ic2Stack) {
		this.ic2Stack = ic2Stack;
	}

	public boolean hasIC2Stack(){
		return ic2Stack != null;
	}

	public ItemStack getTrStack() {
		return trStack;
	}

	public ItemStack getStackBasedOnConfig() {
		if(deduplicate()){
			return getIc2Stack();
		}
		return getTrStack();
	}

	public static boolean deduplicate(){
		if(!CompatManager.isIC2Loaded){
			return false;
		}
		return ConfigTechReborn.removeDuplices;
	}

}
