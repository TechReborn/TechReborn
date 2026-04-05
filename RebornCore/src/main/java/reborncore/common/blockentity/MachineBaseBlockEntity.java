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

import org.apache.commons.lang3.Validate;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import reborncore.api.IListInfoProvider;
import reborncore.api.blockentity.IUpgrade;
import reborncore.api.blockentity.IUpgradeable;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.fluid.FluidValue;
import reborncore.common.misc.world.ChunkEventListener;
import reborncore.common.misc.world.ChunkEventListeners;
import reborncore.common.network.NetworkManager;
import reborncore.common.network.clientbound.CustomDescriptionPayload;
import reborncore.common.recipes.IUpgradeHandler;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;

import static reborncore.RebornCore.LOGGER;

import java.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * Created by modmuss50 on 04/11/2016.
 */
public class MachineBaseBlockEntity extends BlockEntity implements BlockEntityTicker<MachineBaseBlockEntity>, IUpgradeable, IUpgradeHandler, IListInfoProvider, Container, WorldlyContainer, RedstoneConfigurable, ChunkEventListener {

	public RebornInventory<MachineBaseBlockEntity> upgradeInventory = new RebornInventory<>(getUpgradeSlotCount(), "upgrades", 1, this, (slotID, stack, face, direction, blockEntity) -> true);
	private SlotConfiguration slotConfiguration;
	public FluidConfiguration fluidConfiguration;
	private RedstoneConfiguration redstoneConfiguration;
	private final List<RedstoneConfiguration.Element> redstoneElements;

	public boolean renderMultiblock = false;

	private boolean shapeValid = false;
	private boolean needsRematch = false;
	@Nullable
	private Set<BlockPos> shape = null;

	private final static int syncCoolDown = 20;
	private boolean markSync = false;
	private int tickTime = 0;

	public static double SPEED_CAP = 0.99;

	/**
	 * <p>
	 *  This is used to change the speed of the crafting operation.
	 * <p/>
	 *
	 * <ul>
	 * 	<li>0 = Normal speed</li>
	 * 	<li>0.2 = 20% increase</li>
	 * 	<li>0.75 = 75% increase</li>
	 * </ul>
	 */
	double speedMultiplier = 0;
	/**
	 * <p>
	 *  This is used to change the power of the crafting operation.
	 * <p/>
	 *
	 * <ul>
	 *  <li>1 = Normal power</li>
	 *  <li>1.2 = 20% increase</li>
	 *  <li>1.75 = 75% increase</li>
	 *  <li>5 = 500% increase</li>
	 * </ul>
	 */
	double powerMultiplier = 1;
	/**
	 * <p>
	 *  This is used to change the sound of the crafting operation.
	 * <p/>
	 */
	boolean muffled = false;

	public MachineBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		redstoneConfiguration = new RedstoneConfiguration();
		redstoneElements = RedstoneConfiguration.getValidElements(this);
	}

	public void rematch() {
		MultiblockWriter.MultiblockVerifier verifier = new MultiblockWriter.MultiblockVerifier(getBlockPos(), getLevel());
		writeMultiblock(verifier.rotate(getFacing().getOpposite()));
		shapeValid = verifier.isValid();
		needsRematch = false;
	}

	public boolean isShapeValid() {
		return shapeValid;
	}

	public void setShapeValid(boolean shapeValid) {
		this.shapeValid = shapeValid;
	}

	public void link() {
		if (needsRematch) {
			rematch();
			syncWithAll();
		}
	}

	public void unlink() {
		if (shape != null) {
			unregisterListeners(level);
			shape = null;
		}
	}

	public void registerMultiblockVerify() {
		MultiblockWriter.MultiblockShapeFormer writer = new MultiblockWriter.MultiblockShapeFormer(getBlockPos());
		writeMultiblock(writer.rotate(getFacing().getOpposite()));

		shape = writer.getPos();
		registerListeners(level);
		needsRematch = true;
	}

	public Set<ChunkPos> getSpannedChunks() {
		Set<ChunkPos> spannedChunks = new HashSet<>();

		assert shape != null;
		for (BlockPos pos : shape) {
			spannedChunks.add(ChunkPos.containing(pos));
		}

		return spannedChunks;
	}

	public void registerListeners(Level world) {
		for (ChunkPos chunkPos : getSpannedChunks()) {
			ChunkEventListeners.listeners.add(world, chunkPos, this);
		}
	}

	public void unregisterListeners(Level world) {
		for (ChunkPos chunkPos : getSpannedChunks()) {
			ChunkEventListeners.listeners.remove(world, chunkPos, this);
		}
	}

	@Override
	public void onBlockUpdate(BlockPos pos) {
		if (shape != null && shape.contains(pos)) {
			needsRematch = true;
		}
	}

	@Override
	public void onUnloadChunk() {
		needsRematch = true;
	}

	@Override
	public void onLoadChunk() {
		needsRematch = true;
	}

	@Override
	public final void setRemoved() {
		super.setRemoved();

		if (!level.isClientSide()) {
			unlink();
		}
	}

	private void syncIfNecessary(){
		if (this.markSync && this.tickTime % syncCoolDown == 0) {
			this.markSync = false;
			if (level == null || level.isClientSide()) { return; }
			NetworkManager.sendToTracking(new CustomDescriptionPayload(this.worldPosition, this.saveWithoutMetadata(level.registryAccess())), this);
		}
	}

	public void writeMultiblock(MultiblockWriter writer) {}

	public boolean hasMultiblock() {
		return false;
	}

	public void syncWithAll() {
		this.markSync = true;
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
		if (hasMultiblock() && level != null && !level.isClientSide()) {
			registerMultiblockVerify();
		}
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
		CompoundTag compound;
		try (ProblemReporter.ScopedCollector logging = new ProblemReporter.ScopedCollector(problemPath(), LOGGER)) {
			TagValueOutput view = TagValueOutput.createWithContext(logging, registryLookup);
			super.saveAdditional(view);
			saveAdditional(view);
			compound = view.buildResult();
		}
		return compound;
	}

	@Override
	public void tick(Level world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		if (tickTime == 0) {
			onLoad();
		}
		tickTime++;
		@Nullable
		RecipeCrafter crafter = null;
		if (getOptionalCrafter().isPresent()) {
			crafter = getOptionalCrafter().get();
		}
		if (canBeUpgraded()) {
			resetUpgrades();
			for (int i = 0; i < getUpgradeSlotCount(); i++) {
				ItemStack stack = getUpgradeInventory().getItem(i);
				if (!stack.isEmpty() && stack.getItem() instanceof IUpgrade) {
					((IUpgrade) stack.getItem()).process(this, this, stack);
				}
			}
			afterUpgradesApplication();
		}
		if (world == null || world.isClientSide()) {
			return;
		}

		link();

		if (crafter != null && isActive(RedstoneConfiguration.Element.RECIPE_PROCESSING)) {
			crafter.updateEntity();
		}
		if (slotConfiguration != null && isActive(RedstoneConfiguration.Element.ITEM_IO)) {
			slotConfiguration.update(this);
		}
		if (fluidConfiguration != null && isActive(RedstoneConfiguration.Element.FLUID_IO)) {
			fluidConfiguration.update(this);
		}
		syncIfNecessary();
	}

	public void resetUpgrades() {
		resetPowerMultiplier();
		resetSpeedMultiplier();
		resetMuffler();
	}

	protected void afterUpgradesApplication() {
	}

	public int getFacingInt() {
		Block block = level.getBlockState(worldPosition).getBlock();
		if (block instanceof BlockMachineBase) {
			return ((BlockMachineBase) block).getFacing(level.getBlockState(worldPosition)).get3DDataValue();
		}
		return 0;
	}

	public Direction getFacingEnum() {
		Block block = level.getBlockState(worldPosition).getBlock();
		if (block instanceof BlockMachineBase) {
			return ((BlockMachineBase) block).getFacing(level.getBlockState(worldPosition));
		}
		return Direction.NORTH;
	}

	public void setFacing(Direction enumFacing) {
		Block block = level.getBlockState(worldPosition).getBlock();
		if (block instanceof BlockMachineBase) {
			((BlockMachineBase) block).setFacing(enumFacing, level, worldPosition);
		}
	}

	public boolean isActive() {
		Block block = level.getBlockState(worldPosition).getBlock();
		if (block instanceof BlockMachineBase) {
			return level.getBlockState(worldPosition).getValue(BlockMachineBase.ACTIVE);
		}
		return false;
	}

	public Optional<RebornInventory<?>> getOptionalInventory() {
		if (this instanceof InventoryProvider inventory) {
			if (inventory.getInventory() == null) {
				return Optional.empty();
			}
			return Optional.of((RebornInventory<?>) inventory.getInventory());
		}
		return Optional.empty();
	}

	protected Optional<RecipeCrafter> getOptionalCrafter() {
		if (this instanceof IRecipeCrafterProvider crafterProvider) {
			if (crafterProvider.getRecipeCrafter() == null) {
				return Optional.empty();
			}
			return Optional.of(crafterProvider.getRecipeCrafter());
		}
		return Optional.empty();
	}

	@Override
	public void loadAdditional(ValueInput view) {
		super.loadAdditional(view);
		if (getOptionalInventory().isPresent()) {
			getOptionalInventory().get().read(view);
		}
		if (getOptionalCrafter().isPresent()) {
			getOptionalCrafter().get().read(view);
		}
		view.child("slotConfig").ifPresentOrElse(config -> {
			slotConfiguration = new SlotConfiguration(config);
		}, () -> {
			if (getOptionalInventory().isPresent()) {
				slotConfiguration = new SlotConfiguration(getOptionalInventory().get());
			}
		});
		view.child("fluidConfig").ifPresent(config -> {
			fluidConfiguration = new FluidConfiguration(config);
		});
		redstoneConfiguration = view.read("redstoneConfig", RedstoneConfiguration.CODEC.codec()).orElseGet(RedstoneConfiguration::new);
		upgradeInventory.read(view, "Upgrades");
	}

	@Override
	public void saveAdditional(ValueOutput view) {
		super.saveAdditional(view);
		if (getOptionalInventory().isPresent()) {
			getOptionalInventory().get().write(view);
		}
		if (getOptionalCrafter().isPresent()) {
			getOptionalCrafter().get().write(view);
		}
		if (slotConfiguration != null) {
			slotConfiguration.write(view.child("slotConfig"));
		}
		if (fluidConfiguration != null) {
			fluidConfiguration.write(view.child("fluidConfig"));
		}
		upgradeInventory.write(view, "Upgrades");
		view.store("redstoneConfig", RedstoneConfiguration.CODEC.codec(), redstoneConfiguration);
	}

	// Inventory end

	@Override
	public Container getUpgradeInventory() {
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
	public void resetSpeedMultiplier() {
		speedMultiplier = 0;
	}

	@Override
	public double getSpeedMultiplier() {
		return speedMultiplier;
	}

	@Override
	public void addPowerMultiplier(double amount) {
		powerMultiplier = powerMultiplier * (1f + amount);
	}

	@Override
	public void resetPowerMultiplier() {
		powerMultiplier = 1;
	}

	@Override
	public double getPowerMultiplier() {
		return powerMultiplier;
	}

	@Override
	public long getEuPerTick(long baseEu) {
		return (long) (baseEu * powerMultiplier);
	}

	@Override
	public void addSpeedMultiplier(double amount) {
		if (speedMultiplier + amount <= SPEED_CAP) {
			speedMultiplier += amount;
		} else {
			speedMultiplier = SPEED_CAP;
		}
	}

	@Override
	public void muffle() {
		muffled = true;
	}

	@Override
	public void resetMuffler() {
		muffled = false;
	}

	@Override
	public boolean isMuffled() {
		return muffled;
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

	//The amount of ticks between a slot transfer attempt, less is faster
	public int slotTransferSpeed() {
		return 4;
	}

	//The amount of fluid transferred each tick buy the fluid config
	public FluidValue fluidTransferAmount() {
		return FluidValue.BUCKET_QUARTER;
	}

	@Override
	public void addInfo(List<Component> info, boolean isReal, boolean hasData) {
		if (hasData) {
			if (getOptionalInventory().isPresent()) {
				info.add(Component.literal(ChatFormatting.GOLD + "" + getOptionalInventory().get().getContents() + ChatFormatting.GRAY + " items"));
			}
			if (!upgradeInventory.isEmpty()) {
				info.add(Component.literal(ChatFormatting .GOLD + "" + upgradeInventory.getContents() + ChatFormatting .GRAY + " upgrades"));
			}
		}
	}

	public Block getBlockType(){
		return level.getBlockState(worldPosition).getBlock();
	}

	@Override
	public int getContainerSize() {
		if(getOptionalInventory().isPresent()){
			return getOptionalInventory().get().getContainerSize();
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
	public ItemStack getItem(int i) {
		if(getOptionalInventory().isPresent()){
			return getOptionalInventory().get().getItem(i);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItem(int i, int i1) {
		if(getOptionalInventory().isPresent()){
			return getOptionalInventory().get().removeItem(i, i1);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItemNoUpdate(int i) {
		if(getOptionalInventory().isPresent()){
			return getOptionalInventory().get().removeItemNoUpdate(i);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void setItem(int i, ItemStack itemStack) {
		if(getOptionalInventory().isPresent()){
			getOptionalInventory().get().setItem(i, itemStack);
		}
	}

	@Override
	public boolean stillValid(Player playerEntity) {
		if(getOptionalInventory().isPresent()){
			return getOptionalInventory().get().stillValid(playerEntity);
		}
		return false;
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		if (slotConfiguration == null) {
			return false;
		}
		SlotConfiguration.SlotConfigHolder slotConfigHolder = slotConfiguration.getSlotDetails(slot);
		if (slotConfigHolder.filter() && getOptionalCrafter().isPresent()) {
			RecipeCrafter crafter = getOptionalCrafter().get();
			if (!crafter.isStackValidInput(stack)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void clearContent() {
		if(getOptionalInventory().isPresent()){
			getOptionalInventory().get().clearContent();
		}
	}

	@NonNull
	public SlotConfiguration getSlotConfiguration() {
		Validate.notNull(slotConfiguration, "slotConfiguration cannot be null");
		return slotConfiguration;
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		if(slotConfiguration == null){
			return new int[]{}; // I think should be ok, if needed this can return all the slots
		}
		return slotConfiguration.getSlotsForSide(side).stream()
			.filter(Objects::nonNull)
			.filter(slotConfig -> slotConfig.getSlotIO().ioConfig != SlotConfiguration.ExtractConfig.NONE)
			.mapToInt(SlotConfiguration.SlotConfig::getSlotID).toArray();
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
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
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
		if (slotConfiguration == null) {
			return false;
		}
		SlotConfiguration.SlotConfigHolder slotConfigHolder = slotConfiguration.getSlotDetails(index);
		SlotConfiguration.SlotConfig slotConfig = slotConfigHolder.getSideDetail(direction);
		return slotConfig.getSlotIO().ioConfig.isExtract();
	}

	public void onBreak(Level world, Player playerEntity, BlockPos blockPos, BlockState blockState){

	}

	public void onPlace(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack){

	}

	public RedstoneConfiguration getRedstoneConfiguration() {
		return redstoneConfiguration;
	}

	public void setRedstoneConfiguration(RedstoneConfiguration redstoneConfiguration) {
		this.redstoneConfiguration = redstoneConfiguration;
	}

	@Override
	public boolean isActive(RedstoneConfiguration.Element element) {
		return redstoneConfiguration.isActive(element, this);
	}

	public List<RedstoneConfiguration.Element> getRedstoneElements() {
		return redstoneElements;
	}
}
