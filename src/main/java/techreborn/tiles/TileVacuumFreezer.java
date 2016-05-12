package techreborn.tiles;

import reborncore.common.IWrenchable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.Inventory;
import techreborn.api.Reference;
import techreborn.api.recipe.ITileRecipeHandler;
import techreborn.api.recipe.machines.VacuumFreezerRecipe;
import techreborn.blocks.BlockMachineCasing;
import techreborn.init.ModBlocks;

public class TileVacuumFreezer extends TilePowerAcceptor implements IWrenchable,IInventoryProvider, ITileRecipeHandler<VacuumFreezerRecipe>, IRecipeCrafterProvider
{

	public int tickTime;
	public Inventory inventory = new Inventory(3, "TileVacuumFreezer", 64, this);
	public RecipeCrafter crafter;
	public int multiBlockStatus = 0;

	public TileVacuumFreezer()
	{
		super(2);
		// Input slots
		int[] inputs = new int[1];
		inputs[0] = 0;
		int[] outputs = new int[1];
		outputs[0] = 1;
		crafter = new RecipeCrafter(Reference.vacuumFreezerRecipe, this, 2, 1, inventory, inputs, outputs);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		crafter.updateEntity();

		if (worldObj.getTotalWorldTime() % 20 == 0)
		{
			multiBlockStatus = checkMachine() ? 1 : 0;
		}
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
		return 128;
	}

	@Override
	public EnumPowerTier getTier()
	{
		return EnumPowerTier.MEDIUM;
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
		return new ItemStack(ModBlocks.AlloySmelter, 1);
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

	public int getProgressScaled(int scale)
	{
		if (crafter.currentTickTime != 0)
		{
			return crafter.currentTickTime * scale / crafter.currentNeededTicks;
		}
		return 0;
	}

	public boolean checkMachine()
	{
		int xDir = EnumFacing.UP.getFrontOffsetX() * 2;
		int yDir = EnumFacing.UP.getFrontOffsetY() * 2;
		int zDir = EnumFacing.UP.getFrontOffsetZ() * 2;
		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				for (int k = -1; k < 2; k++)
				{
					if ((i != 0) || (j != 0) || (k != 0))
					{
						if (worldObj.getBlockState(new BlockPos(getPos().getX() - xDir + i, getPos().getY() - yDir + j,
								getPos().getZ() - zDir + k)).getBlock() != ModBlocks.MachineCasing)
						{
							return false;
						}
						IBlockState BlockStateContainer = worldObj.getBlockState(new BlockPos(
								getPos().getX() - xDir + i, getPos().getY() - yDir + j, getPos().getZ() - zDir + k));
						BlockMachineCasing blockMachineCasing = (BlockMachineCasing) BlockStateContainer.getBlock();
						if (blockMachineCasing
								.getMetaFromState(BlockStateContainer) != (((i == 0) && (j == 0) && (k != 0))
										|| ((i == 0) && (j != 0) && (k == 0)) || ((i != 0) && (j == 0) && (k == 0)) ? 2
												: 1))
						{
							return false;
						}
					} else if (!worldObj.isAirBlock(new BlockPos(getPos().getX() - xDir + i, getPos().getY() - yDir + j,
							getPos().getZ() - zDir + k)))
					{
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean canCraft(TileEntity tile, VacuumFreezerRecipe recipe) {
		if (tile instanceof TileVacuumFreezer) {
			if (((TileVacuumFreezer) tile).multiBlockStatus == 1) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onCraft(TileEntity tile, VacuumFreezerRecipe recipe) {
		return true;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public RecipeCrafter getRecipeCrafter() {
		return crafter;
	}
}
