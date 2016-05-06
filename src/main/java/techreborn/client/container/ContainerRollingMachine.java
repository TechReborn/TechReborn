package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import reborncore.client.gui.BaseSlot;
import net.minecraft.item.ItemStack;
import reborncore.client.gui.SlotOutput;
import reborncore.common.container.RebornContainer;
import techreborn.api.RollingMachineRecipe;
import techreborn.tiles.TileRollingMachine;

public class ContainerRollingMachine extends RebornContainer
{

	EntityPlayer player;
	TileRollingMachine tile;
	int currentItemBurnTime;
	int burnTime;
	int energy;

	public ContainerRollingMachine(TileRollingMachine tileRollingmachine, EntityPlayer player)
	{
		tile = tileRollingmachine;
		this.player = player;

		for (int l = 0; l < 3; l++)
		{
			for (int k1 = 0; k1 < 3; k1++)
			{
				this.addSlotToContainer(
						new BaseSlot(tileRollingmachine.craftMatrix, k1 + l * 3, 30 + k1 * 18, 17 + l * 18));
			}
		}

		// output
		this.addSlotToContainer(new SlotOutput(tileRollingmachine.inventory, 0, 124, 35));
		// battery
		this.addSlotToContainer(new BaseSlot(tileRollingmachine.inventory, 2, 8, 51));

		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new BaseSlot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new BaseSlot(player.inventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	@Override
	public final void onCraftMatrixChanged(IInventory inv)
	{
		ItemStack output = RollingMachineRecipe.instance.findMatchingRecipe(tile.craftMatrix, tile.getWorld());
		tile.inventory.setInventorySlotContents(1, output);
	}

	@Override
	public void onCraftGuiOpened(ICrafting crafting)
	{
		super.onCraftGuiOpened(crafting);
		crafting.sendProgressBarUpdate(this, 0, tile.runTime);
		crafting.sendProgressBarUpdate(this, 1, tile.tickTime);
		crafting.sendProgressBarUpdate(this, 2, (int) tile.getEnergy());
	}

	@Override
	public void detectAndSendChanges()
	{
		for (int i = 0; i < this.listeners.size(); i++)
		{
			ICrafting crafting = this.listeners.get(i);
			if (this.currentItemBurnTime != tile.runTime)
			{
				crafting.sendProgressBarUpdate(this, 0, tile.runTime);
			}
			if (this.burnTime != tile.tickTime || tile.tickTime == -1)
			{
				crafting.sendProgressBarUpdate(this, 1, tile.tickTime);
			}
			if (this.energy != (int) tile.getEnergy())
			{
				crafting.sendProgressBarUpdate(this, 2, (int) tile.getEnergy());
			}
		}
		super.detectAndSendChanges();
	}

	@Override
	public void updateProgressBar(int id, int value)
	{
		super.updateProgressBar(id, value);
		if (id == 0)
		{
			this.currentItemBurnTime = value;
		} else if (id == 1)
		{
			this.burnTime = value;
		} else if (id == 2)
		{
			this.energy = value;
		}
		this.tile.runTime = this.currentItemBurnTime;
		if (this.burnTime == -1)
		{
			this.burnTime = 0;
		}
		this.tile.tickTime = this.burnTime;
		this.tile.setEnergy(this.energy);
	}

	public int getBurnTimeRemainingScaled(int scale)
	{
		if (burnTime == 0 || this.currentItemBurnTime == 0)
		{
			return 0;
		}
		return this.burnTime * scale / this.currentItemBurnTime;
	}

}
