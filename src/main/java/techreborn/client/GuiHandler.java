package techreborn.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import reborncore.api.recipe.RecipeHandler;
import reborncore.api.tile.IContainerLayout;
import reborncore.common.container.RebornContainer;
import reborncore.common.util.ItemUtils;

import techreborn.api.RollingMachineRecipe;
import techreborn.api.recipe.machines.AlloySmelterRecipe;
import techreborn.client.container.*;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.client.gui.*;
import techreborn.init.ModItems;
import techreborn.manual.GuiManual;
import techreborn.tiles.*;
import techreborn.tiles.fusionReactor.TileEntityFusionController;
import techreborn.tiles.generator.*;
import techreborn.tiles.idsu.TileIDSU;
import techreborn.tiles.lesu.TileLesu;
import techreborn.tiles.multiblock.*;
import techreborn.tiles.storage.TileBatBox;
import techreborn.tiles.storage.TileMFE;
import techreborn.tiles.storage.TileMFSU;
import techreborn.tiles.teir1.*;

public class GuiHandler implements IGuiHandler {

	public static final int thermalGeneratorID = 0;
	public static final int quantumTankID = 1;
	public static final int quantumChestID = 2;
	public static final int centrifugeID = 3;
	public static final int rollingMachineID = 4;
	public static final int blastFurnaceID = 5;
	public static final int alloySmelterID = 6;
	public static final int industrialGrinderID = 7;
	public static final int implosionCompresserID = 8;
	public static final int matterfabID = 9;
	public static final int manuelID = 10;
	public static final int chunkloaderID = 11;
	public static final int assemblingmachineID = 12;
	public static final int dieselGeneratorID = 15;
	public static final int industrialElectrolyzerID = 16;
	public static final int aesuID = 17;
	public static final int alloyFurnaceID = 18;
	public static final int sawMillID = 19;
	public static final int chemicalReactorID = 20;
	public static final int semifluidGeneratorID = 21;
	public static final int gasTurbineID = 22;
	public static final int digitalChestID = 23;
	public static final int destructoPackID = 25;
	public static final int lesuID = 26;
	public static final int idsuID = 27;
	public static final int chargeBench = 28;
	public static final int fusionID = 29;
	public static final int vacuumFreezerID = 30;
	public static final int grinderID = 31;
	public static final int generatorID = 32;
	public static final int extractorID = 33;
	public static final int compressorID = 34;
	public static final int electricFurnaceID = 35;
	public static final int ironFurnace = 36;
	public static final int recyclerID = 37;
	public static final int scrapboxinatorID = 38;
	public static final int batboxID = 39;
	public static final int mfsuID = 40;
	public static final int mfeID = 41;

	@Override
	public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x,
			final int y, final int z) {
		final RebornContainer container = null;
		if (ID == GuiHandler.gasTurbineID || ID == GuiHandler.semifluidGeneratorID
				|| ID == GuiHandler.thermalGeneratorID || ID == GuiHandler.dieselGeneratorID) {
			return new ContainerBuilder("fluidgenerator").player(player.inventory).inventory(8, 84).hotbar(8, 142)
					.addInventory().tile((IInventory) world.getTileEntity(new BlockPos(x, y, z))).slot(0, 80, 17)
					.outputSlot(1, 80, 53).fakeSlot(2, 59, 42).syncEnergyValue().addInventory().create();
		} else if (ID == GuiHandler.quantumTankID) {
			return new ContainerQuantumTank((TileQuantumTank) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == GuiHandler.digitalChestID || ID == GuiHandler.quantumChestID) {
			return new ContainerBuilder("digitalchest").player(player.inventory).inventory().hotbar().addInventory()
					.tile((IInventory) world.getTileEntity(new BlockPos(x, y, z))).slot(0, 80, 17).outputSlot(1, 80, 53)
					.fakeSlot(2, 59, 42).addInventory().create();
		} else if (ID == GuiHandler.centrifugeID) {
			return new ContainerBuilder("centrifuge").player(player.inventory).inventory(8, 84).hotbar(8, 142)
					.addInventory().tile((IInventory) world.getTileEntity(new BlockPos(x, y, z))).slot(0, 80, 35)
					.slot(1, 50, 5).outputSlot(2, 80, 5).outputSlot(3, 110, 35).outputSlot(4, 80, 65)
					.outputSlot(5, 50, 35).energySlot(6, 8, 51).upgradeSlot(7, 152, 8).upgradeSlot(8, 152, 26)
					.upgradeSlot(9, 152, 44).upgradeSlot(10, 152, 62).syncEnergyValue().syncCrafterValue()
					.addInventory().create();
		} else if (ID == GuiHandler.rollingMachineID) {
			return new ContainerBuilder("rollingmachine").player(player.inventory).inventory(8, 84).hotbar(8, 142)
					.addInventory().tile(((TileRollingMachine) world.getTileEntity(new BlockPos(x, y, z))).craftMatrix)
					.slot(0, 30, 17).slot(1, 48, 17).slot(2, 66, 17).slot(3, 30, 35).slot(4, 48, 35).slot(5, 66, 35)
					.slot(6, 30, 53).slot(7, 48, 53).slot(8, 66, 53)
					.onCraft(inv -> ((TileRollingMachine) world.getTileEntity(new BlockPos(x, y, z))).inventory
							.setInventorySlotContents(1, RollingMachineRecipe.instance.findMatchingRecipe(inv, world)))
					.addInventory().tile((IInventory) world.getTileEntity(new BlockPos(x, y, z))).outputSlot(0, 124, 35)
					.energySlot(2, 8, 51).syncEnergyValue()
					.syncIntegerValue(((TileRollingMachine) world.getTileEntity(new BlockPos(x, y, z)))::getBurnTime,
							((TileRollingMachine) world.getTileEntity(new BlockPos(x, y, z)))::setBurnTime)
					.addInventory().create();
		} else if (ID == GuiHandler.blastFurnaceID) {
			return new ContainerBlastFurnace((TileBlastFurnace) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == GuiHandler.alloySmelterID) {
			return new ContainerBuilder("alloysmelter").player(player.inventory).inventory(8, 84).hotbar(8, 142)
					.addInventory().tile((IInventory) world.getTileEntity(new BlockPos(x, y, z)))
					.filterSlot(0, 47, 17, stack -> RecipeHandler.recipeList.stream()
							.anyMatch(recipe -> recipe instanceof AlloySmelterRecipe
									&& ItemUtils.isItemEqual(recipe.getInputs().get(0), stack, true, true, true)))
					.filterSlot(1, 65, 17, stack -> RecipeHandler.recipeList.stream()
							.anyMatch(recipe -> recipe instanceof AlloySmelterRecipe
									&& ItemUtils.isItemEqual(recipe.getInputs().get(1), stack, true, true, true)))
					.outputSlot(2, 116, 35).energySlot(3, 56, 53).upgradeSlot(4, 152, 8).upgradeSlot(5, 152, 26)
					.upgradeSlot(6, 152, 44).upgradeSlot(7, 152, 62).syncEnergyValue().syncCrafterValue().addInventory()
					.create();
		} else if (ID == GuiHandler.industrialGrinderID) {
			return new ContainerIndustrialGrinder((TileIndustrialGrinder) world.getTileEntity(new BlockPos(x, y, z)),
					player);
		} else if (ID == GuiHandler.implosionCompresserID) {
			return new ContainerImplosionCompressor(
					(TileImplosionCompressor) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == GuiHandler.matterfabID) {
			return new ContainerBuilder("matterfabricator").player(player.inventory).inventory(8, 84).hotbar(8, 142)
					.addInventory().tile((IInventory) world.getTileEntity(new BlockPos(x, y, z))).slot(0, 33, 17)
					.slot(1, 33, 35).slot(2, 33, 53).slot(3, 51, 17).slot(4, 51, 35).slot(5, 51, 53)
					.outputSlot(6, 116, 35).syncEnergyValue()
					.syncIntegerValue(((TileMatterFabricator) world.getTileEntity(new BlockPos(x, y, z)))::getProgress,
							((TileMatterFabricator) world.getTileEntity(new BlockPos(x, y, z)))::setProgress)
					.addInventory().create();
		} else if (ID == GuiHandler.assemblingmachineID) {
			return new ContainerBuilder("assemblingmachine").player(player.inventory).inventory(8, 84).hotbar(8, 142)
					.addInventory().tile((IInventory) world.getTileEntity(new BlockPos(x, y, z))).slot(0, 47, 17)
					.slot(1, 65, 17).outputSlot(2, 116, 35).energySlot(3, 56, 53).upgradeSlot(4, 152, 8)
					.upgradeSlot(5, 152, 26).upgradeSlot(6, 152, 44).upgradeSlot(7, 152, 62).syncEnergyValue()
					.syncCrafterValue().addInventory().create();
		} else if (ID == GuiHandler.chunkloaderID) {
			return new ContainerBuilder("chunkloader").player(player.inventory).inventory().hotbar().addInventory()
					.create();
		} else if (ID == GuiHandler.industrialElectrolyzerID) {
			return new ContainerIndustrialElectrolyzer(
					(TileIndustrialElectrolyzer) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == GuiHandler.aesuID) {
			return new ContainerAESU((TileAesu) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == GuiHandler.alloyFurnaceID) {
			return new ContainerBuilder("alloyfurnace").player(player.inventory).inventory(8, 84).hotbar(8, 142)
					.addInventory().tile((IInventory) world.getTileEntity(new BlockPos(x, y, z)))
					.filterSlot(0, 47, 17, stack -> RecipeHandler.recipeList.stream()
							.anyMatch(recipe -> recipe instanceof AlloySmelterRecipe
									&& ItemUtils.isItemEqual(recipe.getInputs().get(0), stack, true, true, true)))
					.filterSlot(1, 65, 17, stack -> RecipeHandler.recipeList.stream()
							.anyMatch(recipe -> recipe instanceof AlloySmelterRecipe
									&& ItemUtils.isItemEqual(recipe.getInputs().get(1), stack, true, true, true)))
					.outputSlot(2, 116, 35).fuelSlot(3, 56, 53)
					.syncIntegerValue(((TileAlloyFurnace) world.getTileEntity(new BlockPos(x, y, z)))::getBurnTime,
							((TileAlloyFurnace) world.getTileEntity(new BlockPos(x, y, z)))::setBurnTime)
					.syncIntegerValue(((TileAlloyFurnace) world.getTileEntity(new BlockPos(x, y, z)))::getCookTime,
							((TileAlloyFurnace) world.getTileEntity(new BlockPos(x, y, z)))::setCookTime)
					.syncIntegerValue(
							((TileAlloyFurnace) world.getTileEntity(new BlockPos(x, y, z)))::getCurrentItemBurnTime,
							((TileAlloyFurnace) world.getTileEntity(new BlockPos(x, y, z)))::setCurrentItemBurnTime)
					.addInventory().create();
		} else if (ID == GuiHandler.sawMillID) {
			return new ContainerIndustrialSawmill((TileIndustrialSawmill) world.getTileEntity(new BlockPos(x, y, z)),
					player);
		} else if (ID == GuiHandler.chemicalReactorID) {
			return new ContainerBuilder("chemicalreactor").player(player.inventory).inventory(8, 84).hotbar(8, 142)
					.addInventory().tile((IInventory) world.getTileEntity(new BlockPos(x, y, z))).slot(0, 70, 21)
					.slot(1, 90, 21).outputSlot(2, 80, 51).energySlot(3, 8, 51).upgradeSlot(4, 152, 8)
					.upgradeSlot(5, 152, 26).upgradeSlot(6, 152, 44).upgradeSlot(7, 152, 62).syncEnergyValue()
					.syncCrafterValue().addInventory().create();
		} else if (ID == GuiHandler.manuelID) {
			return null;
		} else if (ID == GuiHandler.destructoPackID) {
			return new ContainerDestructoPack(player);
		} else if (ID == GuiHandler.lesuID) {
			return new ContainerLESU((TileLesu) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == GuiHandler.idsuID) {
			return new ContainerIDSU((TileIDSU) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == GuiHandler.fusionID) {
			return new ContainerFusionReactor((TileEntityFusionController) world.getTileEntity(new BlockPos(x, y, z)),
					player);
		} else if (ID == GuiHandler.chargeBench) {
			return new ContainerBuilder("chargebench").player(player.inventory).inventory().hotbar(8, 142)
					.addInventory().tile((IInventory) world.getTileEntity(new BlockPos(x, y, z))).energySlot(0, 62, 21)
					.energySlot(1, 80, 21).energySlot(2, 98, 21).energySlot(3, 62, 39).energySlot(4, 80, 39)
					.energySlot(5, 98, 39).syncEnergyValue().addInventory().create();
		} else if (ID == GuiHandler.vacuumFreezerID) {
			return new ContainerVacuumFreezer((TileVacuumFreezer) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == GuiHandler.generatorID) {
			return new ContainerBuilder("generator").player(player.inventory).inventory(8, 84).hotbar(8, 142)
					.addInventory().tile((IInventory) world.getTileEntity(new BlockPos(x, y, z))).fuelSlot(0, 80, 53)
					.energySlot(1, 80, 17).syncEnergyValue()
					.syncIntegerValue(((TileGenerator) world.getTileEntity(new BlockPos(x, y, z)))::getBurnTime,
							((TileGenerator) world.getTileEntity(new BlockPos(x, y, z)))::setBurnTime)
					.syncIntegerValue(((TileGenerator) world.getTileEntity(new BlockPos(x, y, z)))::getTotalBurnTime,
							((TileGenerator) world.getTileEntity(new BlockPos(x, y, z)))::setTotalBurnTime)
					.addInventory().create();
		} else if (ID == GuiHandler.compressorID) {
			return new ContainerBuilder("compressor").player(player.inventory).inventory(8, 84).hotbar(8, 142)
					.addInventory().tile((IInventory) world.getTileEntity(new BlockPos(x, y, z))).slot(0, 56, 34)
					.outputSlot(1, 116, 34).upgradeSlot(2, 152, 8).upgradeSlot(3, 152, 26).upgradeSlot(4, 152, 44)
					.upgradeSlot(5, 152, 62).syncEnergyValue().syncCrafterValue().addInventory().create();
		} else if (ID == GuiHandler.extractorID) {
			return new ContainerBuilder("extractor").player(player.inventory).inventory(8, 84).hotbar(8, 142)
					.addInventory().tile((IInventory) world.getTileEntity(new BlockPos(x, y, z))).slot(0, 56, 34)
					.outputSlot(1, 116, 34).upgradeSlot(2, 152, 8).upgradeSlot(3, 152, 26).upgradeSlot(4, 152, 44)
					.upgradeSlot(5, 152, 62).syncEnergyValue().syncCrafterValue().addInventory().create();
		} else if (ID == GuiHandler.grinderID) {
			return new ContainerBuilder("grinder").player(player.inventory).inventory(8, 84).hotbar(8, 142)
					.addInventory().tile((IInventory) world.getTileEntity(new BlockPos(x, y, z))).slot(0, 56, 34)
					.outputSlot(1, 116, 34).upgradeSlot(2, 152, 8).upgradeSlot(3, 152, 26).upgradeSlot(4, 152, 44)
					.upgradeSlot(5, 152, 62).syncEnergyValue().syncCrafterValue().addInventory().create();
		} else if (ID == GuiHandler.electricFurnaceID) {
			return new ContainerBuilder("electricfurnace").player(player.inventory).inventory(8, 84).hotbar(8, 142)
					.addInventory().tile((IInventory) world.getTileEntity(new BlockPos(x, y, z))).slot(0, 56, 34)
					.outputSlot(1, 116, 34).upgradeSlot(2, 152, 8).upgradeSlot(3, 152, 26).upgradeSlot(4, 152, 44)
					.upgradeSlot(5, 152, 62).syncEnergyValue()
					.syncIntegerValue(((TileElectricFurnace) world.getTileEntity(new BlockPos(x, y, z)))::getBurnTime,
							((TileElectricFurnace) world.getTileEntity(new BlockPos(x, y, z)))::setBurnTime)
					.addInventory().create();
		} else if (ID == GuiHandler.ironFurnace) {
			return new ContainerBuilder("ironfurnace").player(player.inventory).inventory(8, 84).hotbar(8, 142)
					.addInventory().tile((IInventory) world.getTileEntity(new BlockPos(x, y, z))).slot(0, 56, 17)
					.outputSlot(1, 116, 34).fuelSlot(2, 56, 53)
					.syncIntegerValue(((TileIronFurnace) world.getTileEntity(new BlockPos(x, y, z)))::getBurnTime,
							((TileIronFurnace) world.getTileEntity(new BlockPos(x, y, z)))::setBurnTime)
					.syncIntegerValue(((TileIronFurnace) world.getTileEntity(new BlockPos(x, y, z)))::getTotalBurnTime,
							((TileIronFurnace) world.getTileEntity(new BlockPos(x, y, z)))::setTotalBurnTime)
					.addInventory().create();
		} else if (ID == GuiHandler.recyclerID) {
			return new ContainerBuilder("recycler").player(player.inventory).inventory(8, 84).hotbar(8, 142)
					.addInventory().tile((IInventory) world.getTileEntity(new BlockPos(x, y, z))).slot(0, 56, 34)
					.slot(1, 116, 34).upgradeSlot(2, 152, 8).upgradeSlot(3, 152, 26).upgradeSlot(4, 152, 44)
					.upgradeSlot(5, 152, 62).syncEnergyValue()
					.syncIntegerValue(((TileRecycler) world.getTileEntity(new BlockPos(x, y, z)))::getProgress,
							((TileRecycler) world.getTileEntity(new BlockPos(x, y, z)))::setProgress)
					.addInventory().create();
		} else if (ID == GuiHandler.scrapboxinatorID) {
			return new ContainerBuilder("scrapboxinator").player(player.inventory).inventory(8, 84).hotbar(8, 142)
					.addInventory().tile((IInventory) world.getTileEntity(new BlockPos(x, y, z)))
					.filterSlot(0, 56, 34, stack -> stack.getItem() == ModItems.SCRAP_BOX).outputSlot(1, 116, 34)
					.upgradeSlot(2, 152, 8).upgradeSlot(3, 152, 26).upgradeSlot(4, 152, 44).upgradeSlot(5, 152, 62)
					.syncEnergyValue()
					.syncIntegerValue(((TileScrapboxinator) world.getTileEntity(new BlockPos(x, y, z)))::getProgress,
							((TileScrapboxinator) world.getTileEntity(new BlockPos(x, y, z)))::setProgress)
					.addInventory().create();
		} else if (ID == GuiHandler.batboxID) {
			return new ContainerBuilder("batbox").player(player.inventory).inventory(8, 84).hotbar(8, 142)
					.addInventory().tile((IInventory) world.getTileEntity(new BlockPos(x, y, z))).energySlot(0, 80, 17)
					.energySlot(1, 80, 53).syncEnergyValue().addInventory().create();
		} else if (ID == GuiHandler.mfeID) {
			return new ContainerBuilder("mfe").player(player.inventory).inventory(8, 84).hotbar(8, 142).armor()
					.complete(44, 6).addArmor().addInventory()
					.tile((IInventory) world.getTileEntity(new BlockPos(x, y, z))).energySlot(0, 80, 17)
					.energySlot(1, 80, 53).syncEnergyValue().addInventory().create();
		} else if (ID == GuiHandler.mfsuID) {
			return new ContainerBuilder("mfsu").player(player.inventory).inventory(8, 84).hotbar(8, 142).armor()
					.complete(44, 6).addArmor().addInventory()
					.tile((IInventory) world.getTileEntity(new BlockPos(x, y, z))).energySlot(0, 80, 17)
					.energySlot(1, 80, 53).syncEnergyValue().addInventory().create();
		}
		if (container != null) {
			if (container instanceof IContainerLayout) {
				final IContainerLayout layout = (IContainerLayout) container;
				layout.setTile(world.getTileEntity(new BlockPos(x, y, z)));
				layout.setPlayer(player);
				layout.addInventorySlots();
				layout.addPlayerSlots();
			}
			return container;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x,
			final int y, final int z) {
		if (ID == GuiHandler.thermalGeneratorID) {
			return new GuiThermalGenerator(player, (TileThermalGenerator) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.semifluidGeneratorID) {
			return new GuiSemifluidGenerator(player,
					(TileSemifluidGenerator) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.gasTurbineID) {
			return new GuiGasTurbine(player, (TileGasTurbine) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.quantumTankID) {
			return new GuiQuantumTank(player, (TileQuantumTank) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.digitalChestID) {
			return new GuiDigitalChest(player, (TileDigitalChest) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.quantumChestID) {
			return new GuiQuantumChest(player, (TileQuantumChest) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.centrifugeID) {
			return new GuiCentrifuge(player, (TileCentrifuge) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.rollingMachineID) {
			return new GuiRollingMachine(player, (TileRollingMachine) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.blastFurnaceID) {
			return new GuiBlastFurnace(player, (TileBlastFurnace) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.alloySmelterID) {
			return new GuiAlloySmelter(player, (TileAlloySmelter) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.industrialGrinderID) {
			return new GuiIndustrialGrinder(player, (TileIndustrialGrinder) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.implosionCompresserID) {
			return new GuiImplosionCompressor(player,
					(TileImplosionCompressor) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.matterfabID) {
			return new GuiMatterFabricator(player, (TileMatterFabricator) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.chunkloaderID) {
			return new GuiChunkLoader(player, (TileChunkLoader) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.assemblingmachineID) {
			return new GuiAssemblingMachine(player, (TileAssemblingMachine) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.dieselGeneratorID) {
			return new GuiDieselGenerator(player, (TileDieselGenerator) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.industrialElectrolyzerID) {
			return new GuiIndustrialElectrolyzer(player,
					(TileIndustrialElectrolyzer) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.aesuID) {
			return new GuiAESU(player, (TileAesu) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.alloyFurnaceID) {
			return new GuiAlloyFurnace(player, (TileAlloyFurnace) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.sawMillID) {
			return new GuiIndustrialSawmill(player, (TileIndustrialSawmill) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.chemicalReactorID) {
			return new GuiChemicalReactor(player, (TileChemicalReactor) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.manuelID) {
			return new GuiManual();
		} else if (ID == GuiHandler.destructoPackID) {
			return new GuiDestructoPack(new ContainerDestructoPack(player));
		} else if (ID == GuiHandler.lesuID) {
			return new GuiLESU(player, (TileLesu) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.idsuID) {
			return new GuiIDSU(player, (TileIDSU) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.chargeBench) {
			return new GuiChargeBench(player, (TileChargeBench) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.fusionID) {
			return new GuiFusionReactor(player,
					(TileEntityFusionController) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.vacuumFreezerID) {
			return new GuiVacuumFreezer(player, (TileVacuumFreezer) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.grinderID) {
			return new GuiGrinder(player, (TileGrinder) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.generatorID) {
			return new GuiGenerator(player, (TileGenerator) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.extractorID) {
			return new GuiExtractor(player, (TileExtractor) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.compressorID) {
			return new GuiCompressor(player, (TileCompressor) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.electricFurnaceID) {
			return new GuiElectricFurnace(player, (TileElectricFurnace) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.ironFurnace) {
			return new GuiIronFurnace(player, (TileIronFurnace) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.recyclerID) {
			return new GuiRecycler(player, (TileRecycler) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.scrapboxinatorID) {
			return new GuiScrapboxinator(player, (TileScrapboxinator) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.batboxID) {
			return new GuiBatbox(player, (TileBatBox) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.mfsuID) {
			return new GuiMFSU(player, (TileMFSU) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == GuiHandler.mfeID) {
			return new GuiMFE(player, (TileMFE) world.getTileEntity(new BlockPos(x, y, z)));
		}
		return null;
	}
}
