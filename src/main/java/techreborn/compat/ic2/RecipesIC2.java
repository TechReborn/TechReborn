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
import techreborn.init.ModItems;
import techreborn.items.ItemParts;

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
		recipeDuplicateList.add(new RecipeDuplicate(new ItemStack(ModBlocks.machineframe, 0, 1), IC2Items.getItem("resource", "machine")));

		for (RecipeDuplicate duplicate : recipeDuplicateList) {
			duplicate.add();
		}
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		CraftingHelper.addShapelessRecipe(ItemParts.getPartByName("rubber"), IC2Items.getItem("crafting", "rubber"));
		CraftingHelper.addShapelessRecipe(IC2Items.getItem("crafting", "rubber"), ItemParts.getPartByName("rubber"));

		CraftingHelper.addShapelessRecipe(IC2Items.getItem("electric_wrench"), new ItemStack(ModItems.wrench), IC2Items.getItem("crafting", "small_power_unit"));
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

		public void add() {

			CraftingHelper.addShapelessRecipe(stack2, stack1);
			CraftingHelper.addShapelessRecipe(stack1, stack2);

		}

	}

}
