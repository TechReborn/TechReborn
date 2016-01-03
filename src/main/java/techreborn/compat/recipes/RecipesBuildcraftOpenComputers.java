package techreborn.compat.recipes;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import ic2.core.block.machine.BlockMachine;
import ic2.core.item.resources.ItemIngot;
import li.cil.oc.api.driver.Item;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import reborncore.common.util.CraftingHelper;
import reborncore.common.util.OreUtil;
import techreborn.blocks.BlockMachineFrame;
import techreborn.blocks.BlockOre;
import techreborn.blocks.BlockStorage;
import techreborn.compat.ICompatModule;
import techreborn.events.OreUnifier;
import techreborn.init.ModItems;
import techreborn.items.ItemDusts;
import techreborn.items.ItemIngots;
import techreborn.items.ItemPlates;

/**
 * The recipes in here are taken from https://gist.github.com/BBoldt/a29cc5e745856225423c
 *
 * Thanks @BBoldt
 */
public class RecipesBuildcraftOpenComputers implements ICompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {
        for(String plate : ItemPlates.types){
            if(OreUtil.hasBlock(plate)){
                CraftingHelper.addShapelessOreRecipe(ItemPlates.getPlateByName(plate, 16), Items.diamond_axe, "block" + OreUtil.capitalizeFirstLetter(plate));
            }
        }
        GameRegistry.addSmelting(BlockOre.getOreByName("iridium"), ItemIngots.getIngotByName("iridium"), 0F);
        GameRegistry.addSmelting(BlockOre.getOreByName("platinum"), ItemDusts.getDustByName("iridium"), 0F);

        CraftingHelper.addShapelessOreRecipe(BlockMachineFrame.getFrameByName("iron", 1), "plateIron", "plateIron", "plateIron", "plateIron", "plateIron", "plateIron", "plateIron", "plateIron", "plateIron");

//        CraftingHelper.addShapedOreRecipe(BlockMachineFrame.getFrameByName("steel", 1),
//                "SCS", "CFC", "SCS",
//                "S", "plateSteel",
//                "C", "plateCoal",
//                "F", BlockMachineFrame.getFrameByName("iron", 1));

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }
}
