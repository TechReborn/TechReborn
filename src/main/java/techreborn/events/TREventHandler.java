package techreborn.events;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import techreborn.lib.ModInfo;

import java.util.ArrayList;
import java.util.List;

public class TREventHandler {

	@SubscribeEvent
	public void pickupEvent(EntityItemPickupEvent entityItemPickupEvent){
		if(entityItemPickupEvent.getEntityPlayer() instanceof EntityPlayerMP){
			if(entityItemPickupEvent.getItem().getItem().getItem().getRegistryName().getResourceDomain().equals(ModInfo.MOD_ID)){
				List<IRecipe> recipeList = new ArrayList<>();
				for(IRecipe recipe : CraftingManager.REGISTRY){
					if(recipe.getRegistryName() != null && recipe.getRegistryName().getResourceDomain().equals(ModInfo.MOD_ID)){
						if(recipe.getRecipeOutput().getItem().getRegistryName().getResourceDomain().equals(ModInfo.MOD_ID)){
							recipeList.add(recipe);
						}
					}
				}
				entityItemPickupEvent.getEntityPlayer().unlockRecipes(recipeList);
			}
		}
	}

}
