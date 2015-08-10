package techreborn.client;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import techreborn.client.container.*;
import techreborn.client.gui.*;
import techreborn.pda.GuiManual;
import techreborn.pda.pages.BasePage;
import techreborn.tiles.*;
import techreborn.tiles.idsu.TileIDSU;
import techreborn.tiles.lesu.TileLesu;

public class GuiHandler implements IGuiHandler {

    public static final int thermalGeneratorID = 0;
    public static final int quantumTankID = 1;
    public static final int quantumChestID = 2;
    public static final int centrifugeID = 3;
    public static final int rollingMachineID = 4;
    public static final int blastFurnaceID = 5;
    public static final int alloySmelterID = 6;
    public static final int grinderID = 7;
    public static final int compresserID = 8;
    public static final int matterfabID = 9;
    public static final int pdaID = 10;
    public static final int chunkloaderID = 11;
    public static final int assemblingmachineID = 12;
    public static final int latheID = 13;
    public static final int platecuttingmachineID = 14;
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
    public static final int farmID = 29;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {
        if (ID == thermalGeneratorID) {
            return new ContainerThermalGenerator(
                    (TileThermalGenerator) world.getTileEntity(x, y, z), player);
        } else if (ID == semifluidGeneratorID) {
            return new ContainerSemifluidGenerator(
                    (TileSemifluidGenerator) world.getTileEntity(x, y, z), player);
        } else if (ID == gasTurbineID) {
            return new ContainerGasTurbine(
                    (TileGasTurbine) world.getTileEntity(x, y, z), player);
        } else if (ID == quantumTankID) {
            return new ContainerQuantumTank(
                    (TileQuantumTank) world.getTileEntity(x, y, z), player);
        } else if (ID == digitalChestID) {
            return new ContainerDigitalChest(
                    (TileDigitalChest) world.getTileEntity(x, y, z), player);
        } else if (ID == quantumChestID) {
            return new ContainerQuantumChest(
                    (TileQuantumChest) world.getTileEntity(x, y, z), player);
        } else if (ID == centrifugeID) {
            return new ContainerCentrifuge(
                    (TileCentrifuge) world.getTileEntity(x, y, z), player);
        } else if (ID == rollingMachineID) {
            return new ContainerRollingMachine(
                    (TileRollingMachine) world.getTileEntity(x, y, z), player);
        } else if (ID == blastFurnaceID) {
            return new ContainerBlastFurnace(
                    (TileBlastFurnace) world.getTileEntity(x, y, z), player);
        } else if (ID == alloySmelterID) {
            return new ContainerAlloySmelter(
                    (TileAlloySmelter) world.getTileEntity(x, y, z), player);
        } else if (ID == grinderID) {
            return new ContainerGrinder(
                    (TileGrinder) world.getTileEntity(x, y, z), player);
        } else if (ID == compresserID) {
            return new ContainerImplosionCompressor(
                    (TileImplosionCompressor) world.getTileEntity(x, y, z), player);
        } else if (ID == matterfabID) {
            return new ContainerMatterFabricator(
                    (TileMatterFabricator) world.getTileEntity(x, y, z), player);
        } else if (ID == chunkloaderID) {
            return new ContainerChunkloader(
                    (TileChunkLoader) world.getTileEntity(x, y, z), player);
        } else if (ID == assemblingmachineID) {
            return new ContainerAssemblingMachine(
                    (TileAssemblingMachine) world.getTileEntity(x, y, z), player);
        } else if (ID == latheID) {
            return new ContainerLathe(
                    (TileLathe) world.getTileEntity(x, y, z), player);
        } else if (ID == platecuttingmachineID) {
            return new ContainerPlateCuttingMachine(
                    (TilePlateCuttingMachine) world.getTileEntity(x, y, z), player);
        } else if (ID == dieselGeneratorID) {
            return new ContainerDieselGenerator(
                    (TileDieselGenerator) world.getTileEntity(x, y, z), player);
        } else if (ID == industrialElectrolyzerID) {
            return new ContainerIndustrialElectrolyzer(
                    (TileIndustrialElectrolyzer) world.getTileEntity(x, y, z), player);
        } else if (ID == aesuID) {
            return new ContainerAesu(
                    (TileAesu) world.getTileEntity(x, y, z), player);
        } else if (ID == alloyFurnaceID) {
            return new ContainerAlloyFurnace(
                    (TileAlloyFurnace) world.getTileEntity(x, y, z), player);
        } else if (ID == sawMillID) {
            return new ContainerIndustrialSawmill(
                    (TileIndustrialSawmill) world.getTileEntity(x, y, z), player);
        } else if (ID == chemicalReactorID) {
            return new ContainerChemicalReactor(
                    (TileChemicalReactor) world.getTileEntity(x, y, z), player);
        } else if (ID == pdaID) {
            return null;
        } else if (ID == destructoPackID) {
            return new ContainerDestructoPack(player);
        } else if (ID == lesuID) {
            return new ContainerLesu((TileLesu) world.getTileEntity(x, y, z), player);
        } else if (ID == idsuID) {
            return new ContainerIDSU((TileIDSU) world.getTileEntity(x, y, z), player);
        } else if (ID == chargeBench) {
            return new ContainerChargeBench((TileChargeBench) world.getTileEntity(x, y, z), player);
        } else if (ID == farmID) {
            return new ContainerFarm((TileFarm) world.getTileEntity(x, y, z), player);
        }


        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {
        if (ID == thermalGeneratorID) {
            return new GuiThermalGenerator(player,
                    (TileThermalGenerator) world.getTileEntity(x, y, z));
        } else if (ID == semifluidGeneratorID) {
            return new GuiSemifluidGenerator(player,
                    (TileSemifluidGenerator) world.getTileEntity(x, y, z));
        } else if (ID == gasTurbineID) {
            return new GuiGasTurbine(player,
                    (TileGasTurbine) world.getTileEntity(x, y, z));
        } else if (ID == quantumTankID) {
            return new GuiQuantumTank(player,
                    (TileQuantumTank) world.getTileEntity(x, y, z));
        } else if (ID == digitalChestID) {
            return new GuiDigitalChest(player,
                    (TileDigitalChest) world.getTileEntity(x, y, z));
        } else if (ID == quantumChestID) {
            return new GuiQuantumChest(player,
                    (TileQuantumChest) world.getTileEntity(x, y, z));
        } else if (ID == centrifugeID) {
            return new GuiCentrifuge(player,
                    (TileCentrifuge) world.getTileEntity(x, y, z));
        } else if (ID == rollingMachineID) {
            return new GuiRollingMachine(player,
                    (TileRollingMachine) world.getTileEntity(x, y, z));
        } else if (ID == blastFurnaceID) {
            return new GuiBlastFurnace(player,
                    (TileBlastFurnace) world.getTileEntity(x, y, z));
        } else if (ID == alloySmelterID) {
            return new GuiAlloySmelter(player,
                    (TileAlloySmelter) world.getTileEntity(x, y, z));
        } else if (ID == grinderID) {
            return new GuiGrinder(player,
                    (TileGrinder) world.getTileEntity(x, y, z));
        } else if (ID == compresserID) {
            return new GuiImplosionCompressor(player,
                    (TileImplosionCompressor) world.getTileEntity(x, y, z));
        } else if (ID == matterfabID) {
            return new GuiMatterFabricator(player,
                    (TileMatterFabricator) world.getTileEntity(x, y, z));
        } else if (ID == chunkloaderID) {
            return new GuiChunkLoader(player,
                    (TileChunkLoader) world.getTileEntity(x, y, z));
        } else if (ID == assemblingmachineID) {
            return new GuiAssemblingMachine(player,
                    (TileAssemblingMachine) world.getTileEntity(x, y, z));
        } else if (ID == latheID) {
            return new GuiLathe(player,
                    (TileLathe) world.getTileEntity(x, y, z));
        } else if (ID == platecuttingmachineID) {
            return new GuiPlateCuttingMachine(player,
                    (TilePlateCuttingMachine) world.getTileEntity(x, y, z));
        } else if (ID == dieselGeneratorID) {
            return new GuiDieselGenerator(player,
                    (TileDieselGenerator) world.getTileEntity(x, y, z));
        } else if (ID == industrialElectrolyzerID) {
            return new GuiIndustrialElectrolyzer(player,
                    (TileIndustrialElectrolyzer) world.getTileEntity(x, y, z));
        } else if (ID == aesuID) {
            return new GuiAesu(player,
                    (TileAesu) world.getTileEntity(x, y, z));
        } else if (ID == alloyFurnaceID) {
            return new GuiAlloyFurnace(player,
                    (TileAlloyFurnace) world.getTileEntity(x, y, z));
        } else if (ID == sawMillID) {
            return new GuiIndustrialSawmill(player,
                    (TileIndustrialSawmill) world.getTileEntity(x, y, z));
        } else if (ID == chemicalReactorID) {
            return new GuiChemicalReactor(player,
                    (TileChemicalReactor) world.getTileEntity(x, y, z));
        } else if (ID == pdaID) {
            return new GuiManual();
        } else if (ID == destructoPackID) {
            return new GuiDestructoPack(new ContainerDestructoPack(player));
        } else if (ID == lesuID) {
            return new GuiLesu(player, (TileLesu) world.getTileEntity(x, y, z));
        } else if (ID == idsuID) {
            return new GuiIDSU(player, (TileIDSU) world.getTileEntity(x, y, z));
        } else if (ID == chargeBench) {
            return new GuiChargeBench(player, (TileChargeBench) world.getTileEntity(x, y, z));
        } else if (ID == farmID) {
            return new GuiFarm(new ContainerFarm((TileFarm) world.getTileEntity(x, y, z), player));
        }
        return null;
    }
}
