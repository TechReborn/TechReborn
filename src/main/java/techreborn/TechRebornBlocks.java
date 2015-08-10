package techreborn;


import net.minecraft.block.Block;

public class TechRebornBlocks {

    public static Block getBlock(String name){
        try {
            Object e = Class.forName("techreborn.init.ModBlocks").getField(name).get(null);
            return e instanceof Block ?(Block)e:null;
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**

     Full list of blocks.

     thermalGenerator
     quantumTank
     quantumChest
     digitalChest
     centrifuge
     RollingMachine
     MachineCasing
     BlastFurnace
     AlloySmelter
     Grinder
     ImplosionCompressor
     MatterFabricator
     ChunkLoader
     HighAdvancedMachineBlock
     Dragoneggenergysiphoner
     Magicenergeyconverter
     AssemblyMachine
     DieselGenerator
     IndustrialElectrolyzer
     MagicalAbsorber
     Semifluidgenerator
     Gasturbine
     AlloyFurnace
     ChemicalReactor
     lathe
     platecuttingmachine
     Idsu
     Aesu
     Lesu
     Supercondensator
     Woodenshelf
     Metalshelf
     LesuStorage
     Distillationtower
     ElectricCraftingTable
     VacuumFreezer
     PlasmaGenerator
     FusionControlComputer
     ComputerCube
     FusionCoil
     LightningRod
     heatGenerator
     industrialSawmill
     chargeBench
     farm

     ore
     storage
     storage2
     machineframe


     */

}
