/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package reborncore.common.blockentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;
import reborncore.api.IListInfoProvider;
import reborncore.api.blockentity.IUpgrade;
import reborncore.api.blockentity.IUpgradeable;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.fluid.FluidValue;
import reborncore.common.network.ClientBoundPackets;
import reborncore.common.network.NetworkManager;
import reborncore.common.recipes.IUpgradeHandler;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by modmuss50 on 04/11/2016.
 */
public class MachineBaseBlockEntity extends BlockEntity implements Tickable, IUpgradeable, IUpgradeHandler, IListInfoProvider, Inventory, SidedInventory {

	public RebornInventory<MachineBaseBlockEntity> upgradeInventory = new RebornInventory<>(getUpgradeSlotCount(), "upgrades", 1, this, (slotID, stack, face, direction, blockEntity) -> true);
	private SlotConfiguration slotConfiguration;
	public FluidConfiguration fluidConfiguration;
	private RedstoneConfiguration redstoneConfiguration;

	public boolean renderMultiblock = false;

	private int ticktime = 0;

	/**
	 * This is used to change the speed of the crafting operation.
	 * <p/>
	 * 0 = none; 0.2 = 20% speed increase 0.75 = 75% increase
	 */
	double speedMultiplier = 0;
	/**
	 * This is used to change the power of the crafting operation.
	 * <p/>
	 * 1 = none; 1.2 = 20% speed increase 1.75 = 75% increase 5 = uses 5 times
	 * more power
	 */
	double powerMultiplier = 1;

	public MachineBaseBlockEntity(BlockEntityType<?> blockEntityTypeIn) {
		super(blockEntityTypeIn);
		redstoneConfiguration = new RedstoneConfiguration(this);
	}

	public boolean isMultiblockValid() {
		MultiblockWriter.MultiblockVerifier verifier = new MultiblockWriter.MultiblockVerifier(getPos(), getWorld());
		writeMultiblock(verifier.rotate(getFacing().getOpposite()));
		return verifier.isValid();
	}

	public void writeMultiblock(MultiblockWriter writer) {}

	public void syncWithAll() {
		if (world == null || world.isClient) { return; }
		NetworkManager.sendToTracking(ClientBoundPackets.createCustomDescriptionPacket(this), this);
	}

	public void onLoad() {
		if (slotConfiguration == null) {
			if (getOptionalInventory().isPresent()) {
				slotConfiguration = new SlotConfiguration(getOptionalInventory().get());
			}
		}
		if (getTank() != null) {
			if (fluidConfiguration == null) {
				fluidConfiguration = new FluidConfiguration();
			}
		}
		redstoneConfiguration.refreshCache();
	}

	@Nullable
	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return new BlockEntityUpdateS2CPacket(getPos(), 0, toInitialChunkDataTag());
	}

	@Override
	public CompoundTag toInitialChunkDataTag() {
		CompoundTag compound = super.toTag(new CompoundTag());
		toTag(compound);
		return compound;
	}

	@Override
	public void tick() {
		if (ticktime == 0) {
			onLoad();
		}
		ticktime++;
		@Nullable
		RecipeCrafter crafter = null;
		if (getOptionalCrafter().isPresent()) {
			crafter = getOptionalCrafter().get();
		}
		if (canBeUpgraded()) {
			resetUpgrades();
			for (int i = 0; i < getUpgradeSlotCount(); i++) {
				ItemStack stack = getUpgradeInvetory().getStack(i);
				if (!stack.isEmpty() && stack.getItem() instanceof IUpgrade) {
					((IUpgrade) stack.getItem()).process(this, this, stack);
				}
			}
		}
		if (world == null || world.isClient) {
			return;
		}
		if (crafter != null && isActive(RedstoneConfiguration.RECIPE_PROCESSING)) {
			crafter.updateEntity();
		}
		if (slotConfiguration != null && isActive(RedstoneConfiguration.ITEM_IO)) {
			slotConfiguration.update(this);
		}
		if (fluidConfiguration != null && isActive(RedstoneConfiguration.FLUID_IO)) {
			fluidConfiguration.update(this);
		}
	}

	public void resetUpgrades() {
		resetPowerMulti();
		resetSpeedMulti();
	}

	public int getFacingInt() {
		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof BlockMachineBase) {
			return ((BlockMachineBase) block).getFacing(world.getBlockState(pos)).getId();
		}
		return 0;
	}

	public Direction getFacingEnum() {
		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof BlockMachineBase) {
			return ((BlockMachineBase) block).getFacing(world.getBlockState(pos));
		}
		return Direction.NORTH;
	}

	public void setFacing(Direction enumFacing) {
		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof BlockMachineBase) {
			((BlockMachineBase) block).setFacing(enumFacing, world, pos);
		}
	}

	public boolean isActive() {
		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof BlockMachineBase) {
			return world.getBlockState(pos).get(BlockMachineBase.ACTIVE);
		}
		return false;
	}

	public Optional<RebornInventory<?>> getOptionalInventory() {
		if (this instanceof InventoryProvider) {
			InventoryProvider inventory = (InventoryProvider) this;
			if (inventory.getInventory() == null) {
				return Optional.empty();
			}
			return Optional.of((RebornInventory<?>) inventory.getInventory());
		}
		return Optional.empty();
	}

	protected Optional<RecipeCrafter> getOptionalCrafter() {
		if (this instanceof IRecipeCrafterProvider) {
			IRecipeCrafterProvider crafterProvider = (IRecipeCrafterProvider) this;
			if (crafterProvider.getRecipeCrafter() == null) {
				return Optional.empty();
			}
			return Optional.of(crafterProvider.getRecipeCrafter());
		}
		return Optional.empty();
	}

	@Override
	public void fromTag(BlockState blockState, CompoundTag tagCompound) {
		super.fromTag(blockState, tagCompound);
		if (getOptionalInventory().isPresent()) {
			getOptionalInventory().get().read(tagCompound);
		}
		if (getOptionalCrafter().isPresent()) {
			getOptionalCrafter().get().read(tagCompound);
		}
		if (tagCompound.contains("slotConfig")) {
			slotConfiguration = new SlotConfiguration(tagCompound.getCompound("slotConfig"));
		} else {
			if (getOptionalInventory().isPresent()) {
				slotConfiguration = new SlotConfiguration(getOptionalInventory().get());
			}
		}
		if (tagCompound.contains("fluidConfig")) {
			fluidConfiguration = new FluidConfiguration(tagCompound.getCompound("fluidConfig"));
		}
		if (tagCompound.contains("redstoneConfig")) {
			redstoneConfiguration.refreshCache();
			redstoneConfiguration.read(tagCompound.getCompound("redstoneConfig"));
		}
		upgradeInventory.read(tagCompound, "Upgrades");
	}

	@Override
	public CompoundTag toTag(CompoundTag tagCompound) {
		super.toTag(tagCompound);
		if (getOptionalInventory().isPresent()) {
			getOptionalInventory().get().write(tagCompound);
		}
		if (getOptionalCrafter().isPresent()) {
			getOptionalCrafter().get().write(tagCompound);
		}
		if (slotConfiguration != null) {
			tagCompound.put("slotConfig", slotConfiguration.write());
		}
		if (fluidConfiguration != null) {
			tagCompound.put("fluidConfig", fluidConfiguration.write());
		}
		upgradeInventory.write(tagCompound, "Upgrades");
		tagCompound.put("redstoneConfig", redstoneConfiguration.write());
		return tagCompound;
	}

	private boolean isItemValidForSlot(int index, ItemStack stack) {
		if (slotConfiguration == null) {
			return false;
		}
		SlotConfiguration.SlotConfigHolder slotConfigHolder = slotConfiguration.getSlotDetails(index);
		if (slotConfigHolder.filter() && getOptionalCrafter().isPresent()) {
			RecipeCrafter crafter = getOptionalCrafter().get();
			if (!crafter.isStackValidInput(stack)) {
				return false;
			}
		}
		return true;
	}
	//Inventory end

	@Override
	public Inventory getUpgradeInvetory() {
		return upgradeInventory;
	}

	@Override
	public int getUpgradeSlotCount() {
		return 4;
	}

	public Direction getFacing() {
		return getFacingEnum();
	}

	@Override
	public void applyRotation(BlockRotation rotationIn) {
		setFacing(rotationIn.rotate(getFacing()));
	}

	@Override
	public void resetSpeedMulti() {
		speedMultiplier = 0;
	}

	@Override
	public double getSpeedMultiplier() {
		return speedMultiplier;
	}

	@Override
	public void addPowerMulti(double amount) {
		powerMultiplier = powerMultiplier * (1f + amount);
	}

	@Override
	public void resetPowerMulti() {
		powerMultiplier = 1;
	}

	@Override
	public double getPowerMultiplier() {
		return powerMultiplier;
	}

	@Override
	public double getEuPerTick(double baseEu) {
		return baseEu * powerMultiplier;
	}

	@Override
	public void addSpeedMulti(double amount) {
		if (speedMultiplier + amount <= 0.99) {
			speedMultiplier += amount;
		} else {
			speedMultiplier = 0.99;
		}
	}

	public boolean hasSlotConfig() {
		return true;
	}

	@Nullable
	public Tank getTank() {
		return null;
	}

	public boolean showTankConfig() {
		return getTank() != null;
	}

	//The amount of ticks between a slot tranfer atempt, less is faster
	public int slotTransferSpeed() {
		return 4;
	}

	//The amount of fluid transfured each tick buy the fluid config
	public FluidValue fluidTransferAmount() {
		return FluidValue.BUCKET_QUARTER;
	}

	@Override
	public void addInfo(List<Text> info, boolean isReal, boolean hasData) {
		if (hasData) {
			if (getOptionalInventory().isPresent()) {
				info.add(new LiteralText(Formatting.GOLD + "" + getOptionalInventory().get().getContents() + Formatting.GRAY + " items"));
			}
			if (!upgradeInventory.isEmpty()) {
				info.add(new LiteralText(Formatting .GOLD + "" + upgradeInventory.getContents() + Formatting .GRAY + " upgrades"));
			}
		}
	}

	public Block getBlockType(){
		return world.getBlockState(pos).getBlock();
	}

	@Override
	public int size() {
		if(getOptionalInventory().isPresent()){
			return getOptionalInventory().get().size();
		}
		return 0;
	}

	@Override
	public boolean isEmpty() {
		if(getOptionalInventory().isPresent()){
			return getOptionalInventory().get().isEmpty();
		}
		return true;
	}

	@Override
	public ItemStack getStack(int i) {
		if(getOptionalInventory().isPresent()){
			return getOptionalInventory().get().getStack(i);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStack(int i, int i1) {
		if(getOptionalInventory().isPresent()){
			return getOptionalInventory().get().removeStack(i, i1);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStack(int i) {
		if(getOptionalInventory().isPresent()){
			return getOptionalInventory().get().removeStack(i);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void setStack(int i, ItemStack itemStack) {
		if(getOptionalInventory().isPresent()){
			getOptionalInventory().get().setStack(i, itemStack);
		}
	}

	@Override
	public boolean canPlayerUse(PlayerEntity playerEntity) {
		if(getOptionalInventory().isPresent()){
			return getOptionalInventory().get().canPlayerUse(playerEntity);
		}
		return false;
	}
	
	@Override
	public boolean isValid(int slot, ItemStack stack) {
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public void clear() {
		if(getOptionalInventory().isPresent()){
			getOptionalInventory().get().clear();
		}
	}

	@NotNull
	public SlotConfiguration getSlotConfiguration() {
		Validate.notNull(slotConfiguration, "slotConfiguration cannot be null");
		return slotConfiguration;
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		if(slotConfiguration == null){
			return new int[]{}; //I think should be ok, if needed this can return all the slots
		}
		return slotConfiguration.getSlotsForSide(side).stream()
			.filter(Objects::nonNull)
			.filter(slotConfig -> slotConfig.getSlotIO().ioConfig != SlotConfiguration.ExtractConfig.NONE)
			.mapToInt(SlotConfiguration.SlotConfig::getSlotID).toArray();
	}

	@Override
	public boolean canInsert(int index, ItemStack stack, @Nullable Direction direction) {
		if(direction == null || slotConfiguration == null){
			return false;
		}
		SlotConfiguration.SlotConfigHolder slotConfigHolder = slotConfiguration.getSlotDetails(index);
		SlotConfiguration.SlotConfig slotConfig = slotConfigHolder.getSideDetail(direction);
		if (slotConfig.getSlotIO().ioConfig.isInsert()) {
			if (slotConfigHolder.filter() && getOptionalCrafter().isPresent()) {
				RecipeCrafter crafter = getOptionalCrafter().get();
				return crafter.isStackValidInput(stack);
			}
			return slotConfig.getSlotIO().getIoConfig().isInsert();
		}
		return false;
	}

	@Override
	public boolean canExtract(int index, ItemStack stack, Direction direction) {
		if (slotConfiguration == null) {
			return false;
		}
		SlotConfiguration.SlotConfigHolder slotConfigHolder = slotConfiguration.getSlotDetails(index);
		SlotConfiguration.SlotConfig slotConfig = slotConfigHolder.getSideDetail(direction);
		return slotConfig.getSlotIO().ioConfig.isExtact();
	}

	public void onBreak(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState blockState){

	}

	public void onPlace(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack){

	}

	public RedstoneConfiguration getRedstoneConfiguration() {
		return redstoneConfiguration;
	}

	public boolean isActive(RedstoneConfiguration.Element element) {
		return redstoneConfiguration.isActive(element);
	}
}