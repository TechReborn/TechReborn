package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import techreborn.api.RollingMachineRecipe;
import techreborn.client.SlotFake;
import techreborn.client.SlotOutput;
import techreborn.tiles.TileRollingMachine;

public class ContainerRollingMachine extends TechRebornContainer {

	EntityPlayer player;
	TileRollingMachine tile;

	public ContainerRollingMachine(TileRollingMachine tileRollingmachine,
			EntityPlayer player)
	{
		tile = tileRollingmachine;
		this.player = player;

		for (int l = 0; l < 3; l++)
		{
			for (int k1 = 0; k1 < 3; k1++)
			{
				this.addSlotToContainer(new Slot(
						tileRollingmachine.craftMatrix, k1 + l * 3,
						30 + k1 * 18, 17 + l * 18));
			}
		}

		// output
		this.addSlotToContainer(new SlotOutput(tileRollingmachine.inventory, 0,
				124, 35));

		// fakeOutput
		this.addSlotToContainer(new SlotFake(tileRollingmachine.inventory, 1,
				124, 10, false, false, 1));

		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9
						+ 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18,
					142));
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
		ItemStack output = RollingMachineRecipe.instance.findMatchingRecipe(
				tile.craftMatrix, tile.getWorldObj());
		tile.inventory.setInventorySlotContents(1, output);
	}

}
