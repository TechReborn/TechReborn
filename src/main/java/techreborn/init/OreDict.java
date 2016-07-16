package techreborn.init;

import com.google.common.collect.ImmutableList;
import ic2.core.block.BlockIC2Fence;
import ic2.core.block.BlockTexGlass;
import ic2.core.block.type.ResourceBlock;
import ic2.core.item.type.CraftingItemType;
import ic2.core.item.type.MiscResourceType;
import ic2.core.item.type.NuclearResourceType;
import ic2.core.ref.BlockName;
import ic2.core.ref.ItemName;
import ic2.core.ref.TeBlock;
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
            "mixedMetal", //Mixed metal has own version of plate
            "iridiumAlloy", //Iridium alloy is plate itself
            ModItems.META_PLACEHOLDER //...
    );

    public static void init() {
        if(Loader.isModLoaded("IC2")) {
            Core.logHelper.info("IC2 installed, enabling integration");
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

        OreDictionary.registerOre("ic2Generator", ModBlocks.Generator);
        OreDictionary.registerOre("ic2SolarPanel", ModBlocks.solarPanel);
        OreDictionary.registerOre("ic2Macerator", ModBlocks.Grinder);
        OreDictionary.registerOre("ic2Extractor", ModBlocks.Extractor);
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
        OreDictionary.registerOre("pulpWood", ItemDusts.getDustByName("sawDust"));

        //Gems registration
        for(String type : ItemGems.types) {
            if(type.equals(ModItems.META_PLACEHOLDER)) continue; //Aware of placeholders!
            String upper = OreDictUtils.toFirstUpper(type);
            OreDictionary.registerOre("gem" + upper, ItemGems.getGemByName(type));
            boolean ignoreIt = false;
            for(String ignore : plateGenIgnores)
                if(type.startsWith(ignore)) ignoreIt = true;
            if(!ignoreIt) ItemPlates.registerType(upper);
        }
        
        //Ingots registration
        for(String type : ItemIngots.types) {
            if(type.equals(ModItems.META_PLACEHOLDER)) continue; //Aware of placeholders!
            String upperName = OreDictUtils.toFirstUpper(type);
            OreDictionary.registerOre("ingot" + upperName, ItemIngots.getIngotByName(type));
            boolean ignoreIt = false;
            for(String ignore : plateGenIgnores)
                if(type.startsWith(ignore)) ignoreIt = true;
            if(!ignoreIt) ItemPlates.registerType(type);
        }


        //Dusts, nuggets and plates auto-registration

        for(String type : ItemPlates.types) {
            if(type.equals(ModItems.META_PLACEHOLDER)) continue; //Aware of placeholders!
            String oreDictName = "plate" + OreDictUtils.toFirstUpper(type);
            OreDictionary.registerOre(oreDictName, ItemPlates.getPlateByName(type));
        }
        
        for(String type : ItemDusts.types) {
            if(type.equals(ModItems.META_PLACEHOLDER)) continue; //Aware of placeholders!
            String oreDictName = "dust" + OreDictUtils.toFirstUpper(type);
            OreDictionary.registerOre(oreDictName, ItemDusts.getDustByName(type));
        }
        
        for(String type : ItemNuggets.types) {
            if(type.equals(ModItems.META_PLACEHOLDER)) continue; //Aware of placeholders!
            String oreDictName = "nugget" + OreDictUtils.toFirstUpper(type);
            OreDictionary.registerOre(oreDictName, ItemNuggets.getNuggetByName(type));
        }
        
    }

}
