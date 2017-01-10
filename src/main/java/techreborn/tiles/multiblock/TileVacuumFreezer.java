package techreborn.tiles.multiblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import reborncore.api.power.EnumPowerTier;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.Inventory;

import techreborn.api.Reference;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;

public class TileVacuumFreezer extends TilePowerAcceptor
		implements IWrenchable, IInventoryProvider, IRecipeCrafterProvider, IContainerProvider {

	public Inventory inventory = new Inventory(3, "TileVacuumFreezer", 64, this);
	public MultiblockChecker multiblockChecker;
	public RecipeCrafter crafter;

	public TileVacuumFreezer() {
		super(2);
		final int[] inputs = new int[] { 0 };
		final int[] outputs = new int[] { 1 };
		this.crafter = new RecipeCrafter(Reference.vacuumFreezerRecipe, this, 2, 1, this.inventory, inputs, outputs);
	}

	@Override
	public void validate() {
		super.validate();
		this.multiblockChecker = new MultiblockChecker(this.world, this.getPos().down());
	}

	@Override
	public void update() {
		super.update();
		if (this.getMultiBlock())
			this.crafter.updateEntity();
	}

	public boolean getMultiBlock() {
		return this.multiblockChecker.checkRectY(1, 1, MultiblockChecker.CASING_REINFORCED, MultiblockChecker.ZERO_OFFSET);
	}

	@Override
	public double getMaxPower() {
		return 64000;
	}

	@Override
	public boolean canAcceptEnergy(final EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(final EnumFacing direction) {
		return false;
	}

	@Override
	public double getMaxOutput() {
		return 0;
	}

	@Override
	public double getMaxInput() {
		return 64;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.MEDIUM;
	}

	@Override
	public boolean wrenchCanSetFacing(final EntityPlayer entityPlayer, final EnumFacing side) {
		return false;
	}

	@Override
	public EnumFacing getFacing() {
		return this.getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(final EntityPlayer entityPlayer) {
		return entityPlayer.isSneaking();
	}

	@Override
	public float getWrenchDropRate() {
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(final EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.ALLOY_SMELTER, 1);
	}

	@Override
	public void readFromNBT(final NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		this.crafter.readFromNBT(tagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		this.crafter.writeToNBT(tagCompound);
		return tagCompound;
	}

	public int getProgressScaled(final int scale) {
		if (this.crafter.currentTickTime != 0) {
			return this.crafter.currentTickTime * scale / this.crafter.currentNeededTicks;
		}
		return 0;
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	@Override
	public RecipeCrafter getRecipeCrafter() {
		return this.crafter;
	}

	@Override
	public int[] getSlotsForFace(final EnumFacing side) {
		return new int[] { 0, 1 };
	}

	@Override
	public boolean canInsertItem(final int index, final ItemStack itemStackIn, final EnumFacing direction) {
		return index == 0;
	}

	@Override
	public boolean canExtractItem(final int index, final ItemStack stack, final EnumFacing direction) {
		return index == 1;
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("vacuumfreezer").player(player.inventory).inventory().hotbar().addInventory()
			.tile(this).slot(0, 55, 45).outputSlot(1, 101, 45).syncEnergyValue().syncCrafterValue().addInventory()
			.create();
	}

}
