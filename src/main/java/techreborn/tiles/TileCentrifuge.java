package techreborn.tiles;

import reborncore.common.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import reborncore.api.IListInfoProvider;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IContainerProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.container.RebornContainer;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.Inventory;
import techreborn.api.Reference;
import techreborn.client.container.ContainerCentrifuge;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

import java.util.List;

public class TileCentrifuge extends TilePowerAcceptor
		implements IWrenchable,IInventoryProvider,  IListInfoProvider, IRecipeCrafterProvider, IContainerProvider
{

	public int tickTime;
	public Inventory inventory = new Inventory(11, "TileCentrifuge", 64, this);
	public RecipeCrafter crafter;

	public int euTick = ConfigTechReborn.CentrifugeInputTick;

	public TileCentrifuge()
	{
		super(2);
		// Input slots
		int[] inputs = new int[2];
		inputs[0] = 0;
		inputs[1] = 1;
		int[] outputs = new int[4];
		outputs[0] = 2;
		outputs[1] = 3;
		outputs[2] = 4;
		outputs[3] = 5;

		crafter = new RecipeCrafter(Reference.centrifugeRecipe, this, 2, 4, inventory, inputs, outputs);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		crafter.updateEntity();
		charge(6);
		if (inventory.getStackInSlot(6) != null)
		{
			ItemStack stack = inventory.getStackInSlot(6);
			if(stack.getItem() instanceof IEnergyItemInfo){
				IEnergyItemInfo item = (IEnergyItemInfo) stack.getItem();
				if (item.canProvideEnergy(stack))
				{
					if (getEnergy() != getMaxPower())
					{
						addEnergy(item.getMaxTransfer(stack));
						PoweredItem.setEnergy(PoweredItem.getEnergy(stack) - item.getMaxTransfer(stack), stack);
					}
				}
			}
		}
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing side)
	{
		return false;
	}

	@Override
	public EnumFacing getFacing()
	{
		return getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer)
	{
		return entityPlayer.isSneaking();
	}

	@Override
	public float getWrenchDropRate()
	{
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return new ItemStack(ModBlocks.centrifuge, 1);
	}

	public boolean isComplete()
	{
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		crafter.readFromNBT(tagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		crafter.writeToNBT(tagCompound);
	}

	@Override
	public void addInfo(List<String> info, boolean isRealTile)
	{
		super.addInfo(info, isRealTile);
		info.add("Round and round it goes");
	}
	
	public int getProgressScaled(int scale)
	{
		if (crafter.currentTickTime != 0)
		{
			return crafter.currentTickTime * scale / crafter.currentNeededTicks;
		}
		return 0;
	}

	@Override
	public double getMaxPower()
	{
		return 10000;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction)
	{
		return true;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction)
	{
		return false;
	}

	@Override
	public double getMaxOutput()
	{
		return 0;
	}

	@Override
	public double getMaxInput()
	{
		return 32;
	}

	@Override
	public EnumPowerTier getTier()
	{
		return EnumPowerTier.LOW;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public RecipeCrafter getRecipeCrafter() {
		return crafter;
	}

	@Override
	public RebornContainer getContainer() {
		return RebornContainer.getContainerFromClass(ContainerCentrifuge.class, this);
	}
}