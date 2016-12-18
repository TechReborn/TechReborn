package techreborn.init;

import ic2.core.ref.BlockName;
import ic2.core.ref.TeBlock;
import net.minecraft.item.ItemStack;
import techreborn.compat.CompatManager;
import techreborn.config.ConfigTechReborn;

/**
 * Created by Mark on 18/12/2016.
 */
public enum IC2Duplicates {

	GRINDER(BlockName.te.getItemStack(TeBlock.macerator.getName()), new ItemStack(ModBlocks.Grinder)),
	ELECTRICAL_FURNACE(BlockName.te.getItemStack(TeBlock.electric_furnace.getName()), new ItemStack(ModBlocks.ElectricFurnace)),
	IRON_FURNACE(BlockName.te.getItemStack(TeBlock.iron_furnace.getName()), new ItemStack(ModBlocks.ironFurnace)),
	GENERATOR(BlockName.te.getItemStack(TeBlock.generator.getName()), new ItemStack(ModBlocks.Generator)),
	EXTRACTOR(BlockName.te.getItemStack(TeBlock.extractor.getName()), new ItemStack(ModBlocks.Extractor)),
	SOLAR_PANEL(BlockName.te.getItemStack(TeBlock.solar_generator.getName()), new ItemStack(ModBlocks.solarPanel)),
	RECYCLER(BlockName.te.getItemStack(TeBlock.recycler.getName()), new ItemStack(ModBlocks.recycler)),
	COMPRESSOR(BlockName.te.getItemStack(TeBlock.compressor.getName()), new ItemStack(ModBlocks.Compressor));

	ItemStack ic2Stack;
	ItemStack trStack;

	IC2Duplicates(ItemStack ic2Stack, ItemStack trStack) {
		this.ic2Stack = ic2Stack;
		this.trStack = trStack;
	}

	public ItemStack getIc2Stack() {
		if(!CompatManager.isIC2Loaded){
			throw new RuntimeException("IC2 isnt loaded");
		}
		return ic2Stack;
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
