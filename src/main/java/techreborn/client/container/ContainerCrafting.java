package techreborn.client.container;

import net.minecraft.inventory.ICrafting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.api.tile.IContainerLayout;
import reborncore.common.container.RebornContainer;
import reborncore.common.recipes.RecipeCrafter;

public abstract class ContainerCrafting extends RebornContainer
{

	RecipeCrafter crafter;

	int currentTickTime = 0;
	int currentNeededTicks = 0;
	int energy;

	@Deprecated
	public ContainerCrafting(RecipeCrafter crafter)
	{
		this();
		this.crafter = crafter;
	}

	public ContainerCrafting() {
	}

	public void setCrafter(RecipeCrafter crafter) {
		this.crafter = crafter;
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for (int i = 0; i < this.listeners.size(); i++)
		{
			ICrafting icrafting = this.listeners.get(i);
			if (this.currentTickTime != crafter.currentTickTime || crafter.currentTickTime == -1)
			{
				icrafting.sendProgressBarUpdate(this, 0, crafter.currentTickTime);
			}
			if (this.currentNeededTicks != crafter.currentNeededTicks)
			{
				icrafting.sendProgressBarUpdate(this, 1, crafter.currentNeededTicks);
			}
			if (this.energy != (int) crafter.energy.getEnergy())
			{
				icrafting.sendProgressBarUpdate(this, 2, (int) crafter.energy.getEnergy());
			}
		}
	}

	@Override
	public void onCraftGuiOpened(ICrafting crafting)
	{
		super.onCraftGuiOpened(crafting);
		crafting.sendProgressBarUpdate(this, 0, crafter.currentTickTime);
		crafting.sendProgressBarUpdate(this, 1, crafter.currentNeededTicks);
		crafting.sendProgressBarUpdate(this, 2, (int) crafter.energy.getEnergy());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int value)
	{
		if (id == 0)
		{
			this.currentTickTime = value;
			if (this.currentTickTime == -1)
			{
				this.currentTickTime = 0;
			}
		} else if (id == 1)
		{
			this.currentNeededTicks = value;
		} else if (id == 2)
		{
			this.energy = value;
		}
		this.crafter.currentTickTime = currentTickTime;
		this.crafter.currentNeededTicks = currentNeededTicks;
		this.crafter.energy.setEnergy(energy);
	}
}
