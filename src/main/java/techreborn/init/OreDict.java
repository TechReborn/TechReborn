package techreborn.init;

import ic2.core.block.BlockTexGlass;
import ic2.core.block.type.ResourceBlock;
import ic2.core.block.wiring.CableType;
import ic2.core.item.type.*;
import ic2.core.ref.BlockName;
import ic2.core.ref.ItemName;
import ic2.core.ref.TeBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.blocks.BlockMachineFrame;
import techreborn.items.ItemIngots;
import techreborn.items.ItemParts;
import techreborn.items.ItemPlates;
import techreborn.parts.powerCables.ItemStandaloneCables;

public class OreDict {

    public static void init() {
        if(Loader.isModLoaded("IC2")) {
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

            //OreDictionary.registerOre("ic2Generator", ModBlocks.Generator);
            //OreDictionary.registerOre("ic2SolarPanel", ModBlocks.solarPanel);
            //OreDictionary.registerOre("ic2Macerator", ModBlocks.Grinder);
            //OreDictionary.registerOre("ic2Extractor", ModBlocks.Extractor);
            //OreDictionary.registerOre("ic2Windmill", ModBlocks.windMill);
            //OreDictionary.registerOre("ic2Watermill", ModBlocks.waterMill);

            OreDictionary.registerOre("uran235", ItemName.nuclear.getItemStack(NuclearResourceType.uranium_235));
            OreDictionary.registerOre("uran238", ItemName.nuclear.getItemStack(NuclearResourceType.uranium_238));
            OreDictionary.registerOre("smallUran238", ItemName.nuclear.getItemStack(NuclearResourceType.small_uranium_238));
            OreDictionary.registerOre("smallUran235", ItemName.nuclear.getItemStack(NuclearResourceType.small_uranium_235));

            OreDictionary.registerOre("rubberWood", BlockName.rubber_wood.getItemStack());
            OreDictionary.registerOre("glassReinforced", BlockName.glass.getItemStack(BlockTexGlass.GlassType.reinforced));
            OreDictionary.registerOre("oreIridium", ItemName.misc_resource.getItemStack(MiscResourceType.iridium_ore));
        }

        OreDictionary.registerOre("reBattery", ModItems.reBattery);

        OreDictionary.registerOre("circuitBasic", ItemParts.getPartByName("electronicCircuit"));
        OreDictionary.registerOre("circuitAdvanced", ItemParts.getPartByName("advancedCircuit"));
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

        OreDictionary.registerOre("ic2Generator", ModBlocks.Generator);
        OreDictionary.registerOre("ic2SolarPanel", ModBlocks.solarPanel);
        OreDictionary.registerOre("ic2Macerator", ModBlocks.Grinder);
        OreDictionary.registerOre("ic2Extractor", ModBlocks.Extractor);
        OreDictionary.registerOre("ic2Windmill", ModBlocks.windMill);
        OreDictionary.registerOre("ic2Watermill", ModBlocks.waterMill);

        //OreDictionary.registerOre("uran235", );
        //OreDictionary.registerOre("uran238", );
        //OreDictionary.registerOre("smallUran235", );

        OreDictionary.registerOre("woodRubber", ModBlocks.rubberLog);
        OreDictionary.registerOre("glassReinforced", ModBlocks.reinforcedglass);

        for(String type : ItemPlates.types) {
            String oreDictName = "plate" + Character.toUpperCase(type.charAt(0)) + type.substring(1);
            System.out.println(oreDictName);
            OreDictionary.registerOre(oreDictName, ItemPlates.getPlateByName(type));
        }

        for(String type : ItemIngots.types) {
            String oreDictName = "ingot" + Character.toUpperCase(type.charAt(0)) + type.substring(1);
            System.out.println(oreDictName);
            OreDictionary.registerOre(oreDictName, ItemIngots.getIngotByName(type));
        }


    }

}
