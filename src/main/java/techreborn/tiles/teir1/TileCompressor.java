package techreborn.tiles.teir1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
import techreborn.utils.upgrade.UpgradeHandler;

public class TileCompressor extends TilePowerAcceptor
implements IWrenchable, IInventoryProvider, IRecipeCrafterProvider, IContainerProvider {

	public Inventory inventory = new Inventory(6, "TileCompressor", 64, this);

	public UpgradeHandler upgradeHandler;
	public RecipeCrafter crafter;

	public int capacity = 1000;

	public TileCompressor() {
		super(1);
		final int[] inputs = new int[] { 0 };
		final int[] outputs = new int[] { 1 };
		this.crafter = new RecipeCrafter(Reference.compressorRecipe, this, 2, 1, this.inventory, inputs, outputs);
		this.upgradeHandler = new UpgradeHandler(this.crafter, this.inventory, 2, 3, 4, 5);
	}

	@Override
	public void update() {
		if (!this.world.isRemote) {
			super.update();
			this.crafter.updateEntity();
			this.upgradeHandler.tick();
			this.charge(3);
		}
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
		return new ItemStack(ModBlocks.COMPRESSOR, 1);
	}

	public boolean isComplete() {
		return false;
	}

	// ISidedInventory
	@Override
	public int[] getSlotsForFace(final EnumFacing side) {
		if (side.equals(EnumFacing.UP))
			return new int[] { 0 };
		else if (side.equals(EnumFacing.DOWN))
			return new int[] { 1 };
		return new int[0];
	}

	@Override
	public boolean canInsertItem(final int Index, final ItemStack itemStack, final EnumFacing side) {
		return Index == 0;
	}

	@Override
	public boolean canExtractItem(final int Index, final ItemStack itemStack, final EnumFacing side) {
		return Index == 1;
	}

	public int getProgressScaled(final int scale) {
		if (this.crafter.currentTickTime != 0) {
			return this.crafter.currentTickTime * scale / this.crafter.currentNeededTicks;
		}
		return 0;
	}

	@Override
	public double getMaxPower() {
		return this.capacity;
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
		return 32;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.LOW;
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
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("compressor").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().tile(this).slot(0, 56, 34).outputSlot(1, 116, 34).upgradeSlot(2, 152, 8)
				.upgradeSlot(3, 152, 26).upgradeSlot(4, 152, 44).upgradeSlot(5, 152, 62).syncEnergyValue()
				.syncCrafterValue().addInventory().create();
	}
}
