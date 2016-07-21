package techreborn.tiles.teir1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IContainerProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.container.RebornContainer;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.tile.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.api.Reference;
import techreborn.client.container.ContainerExtractor;
import techreborn.init.ModBlocks;

public class TileExtractor extends TilePowerAcceptor implements IWrenchable,IInventoryProvider, IRecipeCrafterProvider, IContainerProvider {

	public Inventory inventory = new Inventory(6, "TileExtractor", 64, this);
	public RecipeCrafter crafter;
	public int capacity = 1000;

	public TileExtractor() {
		int[] inputs = new int[1];
		inputs[0] = 0;
		int[] outputs = new int[1];
		outputs[0] = 1;
		crafter = new RecipeCrafter(Reference.extractorRecipe, this, 2, 1, inventory, inputs, outputs);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		crafter.updateEntity();
		// upgrades.tick();
		//charge(3); TODO
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing side) {
		return false;
	}

	@Override
	public EnumFacing getFacing() {
		return getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return entityPlayer.isSneaking();
	}

	@Override
	public float getWrenchDropRate() {
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.Extractor, 1);
	}

	public boolean isComplete() {
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		crafter.readFromNBT(tagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		crafter.writeToNBT(tagCompound);
		return tagCompound;
	}

	public int getProgressScaled(int scale) {
		if (crafter.currentTickTime != 0) {
			return crafter.currentTickTime * scale / crafter.currentNeededTicks;
		}
		return 0;
	}

	@Override
	public double getMaxPower() {
		return capacity;
	}

	public EnumPowerTier getTier() {
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
		return RebornContainer.getContainerFromClass(ContainerExtractor.class, this);
	}
}
