/**
 * 
 */
package techreborn.events;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemSlab;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import reborncore.RebornRegistry;
import techreborn.TechReborn;
import techreborn.blocks.BlockComputerCube;
import techreborn.blocks.BlockNuke;
import techreborn.blocks.BlockRefinedIronFence;
import techreborn.blocks.BlockReinforcedGlass;
import techreborn.blocks.BlockRubberLeaves;
import techreborn.blocks.BlockRubberLog;
import techreborn.blocks.BlockRubberPlank;
import techreborn.blocks.BlockRubberPlankSlab;
import techreborn.blocks.BlockRubberPlankStair;
import techreborn.blocks.BlockRubberSapling;
import techreborn.init.TRContent;
import techreborn.init.TRContent.Cables;
import techreborn.init.TRContent.Machine;
import techreborn.init.TRContent.MachineBlocks;
import techreborn.init.TRContent.Ores;
import techreborn.init.TRContent.SolarPanels;
import techreborn.init.TRContent.StorageBlocks;
import techreborn.itemblocks.ItemBlockRubberSapling;
import techreborn.utils.InitUtils;

/**
 * @author drcrazy
 *
 */

@Mod.EventBusSubscriber(modid = TechReborn.MOD_ID)
public class RegistryEventHandler {

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {

		Arrays.stream(Ores.values()).forEach(value -> RebornRegistry.registerBlock(value.block));
		Arrays.stream(StorageBlocks.values()).forEach(value -> RebornRegistry.registerBlock(value.block));
		Arrays.stream(MachineBlocks.values()).forEach(value -> {
			RebornRegistry.registerBlock(value.frame);
			RebornRegistry.registerBlock(value.casing);
		});
		Arrays.stream(SolarPanels.values()).forEach(value -> RebornRegistry.registerBlock(value.block));
		Arrays.stream(Cables.values()).forEach(value -> RebornRegistry.registerBlock(value.block));
		Arrays.stream(Machine.values()).forEach(value -> RebornRegistry.registerBlock(value.block));

		// Misc. blocks
		RebornRegistry.registerBlock(TRContent.COMPUTER_CUBE = InitUtils.setup(new BlockComputerCube(), "computer_cube"));
		RebornRegistry.registerBlock(TRContent.NUKE = InitUtils.setup(new BlockNuke(), "nuke"));
		RebornRegistry.registerBlock(TRContent.REFINED_IRON_FENCE = InitUtils.setup(new BlockRefinedIronFence(), "refined_iron_fence"));
		RebornRegistry.registerBlock(TRContent.REINFORCED_GLASS = InitUtils.setup(new BlockReinforcedGlass(), "reinforced_glass"));
		RebornRegistry.registerBlock(TRContent.RUBBER_LEAVES = InitUtils.setup(new BlockRubberLeaves(), "rubber_leaves"));
		RebornRegistry.registerBlock(TRContent.RUBBER_LOG = InitUtils.setup(new BlockRubberLog(), "rubber_log"));
		RebornRegistry.registerBlock(TRContent.RUBBER_PLANKS = InitUtils.setup(new BlockRubberPlank(), "rubber_planks"));
		RebornRegistry.registerBlockNoItem(TRContent.RUBBER_LOG_SLAB_HALF = InitUtils.setup(new BlockRubberPlankSlab.BlockHalf("rubber_plank"), "rubber_plank_slab"));
		RebornRegistry.registerBlock(
				TRContent.RUBBER_SAPLING = InitUtils.setup(new BlockRubberSapling(), "rubber_sapling"),
					ItemBlockRubberSapling.class, 
					"rubber_sapling");
		RebornRegistry.registerBlock(
				TRContent.RUBBER_LOG_SLAB_DOUBLE = InitUtils.setup(new BlockRubberPlankSlab.BlockDouble("rubber_plank", TRContent.RUBBER_LOG_SLAB_HALF), "rubber_plank_double_slab"),
					new ItemSlab(TRContent.RUBBER_LOG_SLAB_HALF, (BlockSlab) TRContent.RUBBER_LOG_SLAB_HALF, (BlockSlab) TRContent.RUBBER_LOG_SLAB_DOUBLE),
					"rubber_plank_double_slab");
		RebornRegistry.registerBlock(TRContent.RUBBER_LOG_STAIR = InitUtils.setup(new BlockRubberPlankStair(TRContent.RUBBER_LOG.getDefaultState(), "rubber_plank"),
					"rubber_plank_stair"));

		TechReborn.LOGGER.debug("TechReborns Blocks Loaded");
	}

}
