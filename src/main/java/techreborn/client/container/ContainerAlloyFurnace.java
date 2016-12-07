package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotFurnaceFuel;
import reborncore.client.gui.slots.BaseSlot;
import reborncore.client.gui.slots.SlotOutput;
import net.minecraft.item.ItemStack;
import reborncore.api.recipe.IBaseRecipeType;
import reborncore.api.recipe.RecipeHandler;
import reborncore.common.container.RebornContainer;
import reborncore.common.util.ItemUtils;
import techreborn.api.recipe.machines.AlloySmelterRecipe;
import techreborn.tiles.TileAlloyFurnace;

import javax.annotation.Nullable;

public class ContainerAlloyFurnace extends RebornContainer {

	public int tickTime;
	EntityPlayer player;
	TileAlloyFurnace tile;
	int currentItemBurnTime;
	int burnTime;
	int cookTime;

	public ContainerAlloyFurnace(TileAlloyFurnace tileAlloyfurnace, EntityPlayer player) {
		tile = tileAlloyfurnace;
		this.player = player;

		// input
		this.addSlotToContainer(new SlotInputCustom(tileAlloyfurnace.inventory, 0, 47, 17, 0));
		this.addSlotToContainer(new SlotInputCustom(tileAlloyfurnace.inventory, 1, 65, 17, 1));
		// outputs
		this.addSlotToContainer(new SlotOutput(tileAlloyfurnace.inventory, 2, 116, 35));
		// Fuel
		this.addSlotToContainer(new SlotFurnaceFuel(tileAlloyfurnace.inventory, 3, 56, 53));

		int i;

		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new BaseSlot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new BaseSlot(player.inventory, i, 8 + i * 18, 142));
		}
	}

	public static class SlotInputCustom extends BaseSlot {

		int recipeSlot = 0;

		public SlotInputCustom(IInventory inventoryIn, int index, int xPosition, int yPosition, int recipeSlot) {
			super(inventoryIn, index, xPosition, yPosition);
			this.recipeSlot = recipeSlot;
		}

		@Override
		public boolean isItemValid(
			@Nullable
				ItemStack stack) {
			for(IBaseRecipeType recipe : RecipeHandler.recipeList){
				if(recipe instanceof AlloySmelterRecipe){
					if(ItemUtils.isItemEqual(recipe.getInputs().get(recipeSlot), stack, true, true, true)){
						return true;
					}
				}
			}
			return false;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public void addListener(IContainerListener crafting) {
		super.addListener(crafting);
		crafting.sendProgressBarUpdate(this, 0, tile.currentItemBurnTime);
		crafting.sendProgressBarUpdate(this, 1, tile.burnTime);
		crafting.sendProgressBarUpdate(this, 2, tile.cookTime);
	}

	@Override
	public void detectAndSendChanges() {
		for (int i = 0; i < this.listeners.size(); i++) {
			IContainerListener crafting = this.listeners.get(i);
			if (this.currentItemBurnTime != tile.currentItemBurnTime) {
				crafting.sendProgressBarUpdate(this, 0, tile.currentItemBurnTime);
			}
			if (this.burnTime != tile.burnTime) {
				crafting.sendProgressBarUpdate(this, 1, tile.burnTime);
			}
			if (this.cookTime != tile.cookTime) {
				crafting.sendProgressBarUpdate(this, 2, tile.cookTime);
			}
		}
		super.detectAndSendChanges();
	}

	@Override
	public void updateProgressBar(int id, int value) {
		super.updateProgressBar(id, value);
		if (id == 0) {
			this.currentItemBurnTime = value;
		} else if (id == 1) {
			this.burnTime = value;
		} else if (id == 2) {
			this.cookTime = value;
		}
		this.tile.currentItemBurnTime = this.currentItemBurnTime;
		this.tile.burnTime = this.burnTime;
		this.tile.cookTime = this.cookTime;
	}
}
