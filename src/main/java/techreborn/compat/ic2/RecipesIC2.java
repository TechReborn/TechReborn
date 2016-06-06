package techreborn.compat.ic2;

import ic2.api.item.IC2Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import reborncore.common.util.CraftingHelper;
import techreborn.compat.ICompatModule;
import techreborn.init.ModBlocks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 06/06/2016.
 */
public class RecipesIC2 implements ICompatModule {

    List<RecipeDuplicate> recipeDuplicateList = new ArrayList<>();

    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {
        recipeDuplicateList.add(new RecipeDuplicate(new ItemStack(ModBlocks.machineframe, 0, 1), IC2Items.getItem("resource","machine")));

        for(RecipeDuplicate duplicate : recipeDuplicateList){
            duplicate.add();
        }
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }

    public class RecipeDuplicate {

        ItemStack stack1;

        ItemStack stack2;

        public RecipeDuplicate(ItemStack stack1, ItemStack stack2) {
            this.stack1 = stack1;
            this.stack2 = stack2;
        }

        public void add(){

            CraftingHelper.addShapelessRecipe(stack2, stack1);
            CraftingHelper.addShapelessRecipe(stack1, stack2);

        }

    }

}
