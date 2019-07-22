/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.blockentity.storage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.blockentity.IUpgrade;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.config.ConfigRegistry;
import reborncore.common.util.RebornInventory;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import techreborn.init.TRBlockEntities;

@RebornRegister(TechReborn.MOD_ID)
public class AdjustableSUBlockEntity extends EnergyStorageBlockEntity implements IContainerProvider {

	@ConfigRegistry(config = "machines", category = "aesu", key = "AesuMaxInput", comment = "AESU Max Input (Value in EU)")
	public static int maxInput = 16192;
	@ConfigRegistry(config = "machines", category = "aesu", key = "AesuMaxOutput", comment = "AESU Max Output (Value in EU)")
	public static int maxOutput = 16192;
	@ConfigRegistry(config = "machines", category = "aesu", key = "AesuMaxEnergy", comment = "AESU Max Energy (Value in EU)")
	public static int maxEnergy = 100_000_000;

	public RebornInventory<AdjustableSUBlockEntity> inventory = new RebornInventory<>(4, "AdjustableSUBlockEntity", 64, this).withConfiguredAccess();
	private int OUTPUT = 64; // The current output
	public int superconductors = 0;

	public AdjustableSUBlockEntity() {
		super(TRBlockEntities.ADJUSTABLE_SU, "ADJUSTABLE_SU", 4, TRContent.Machine.ADJUSTABLE_SU.block, EnumPowerTier.INSANE, maxInput, maxOutput, maxEnergy);
	}



	@Override
	public void tick() {
		super.tick();

		if (OUTPUT > getMaxConfigOutput()) {
			OUTPUT = getMaxConfigOutput();
		}
		if(world.getTime() % 20 == 0){
			checkTier();
		}
	}

	public int getMaxConfigOutput(){
		int extra = 0;
		if(superconductors > 0){
			extra = (int) Math.pow(2, (superconductors + 2)) * maxOutput;
		}
		return maxOutput + extra;
	}

	public void handleGuiInputFromClient(int id, boolean shift, boolean ctrl) {
		if (id == 300) {
			OUTPUT += shift ? 4096 : 256;
			if(ctrl){
				//Set to max, limited to the max later
				OUTPUT = Integer.MAX_VALUE;
			}
		}
		if (id == 301) {
			OUTPUT += shift ? 512 : 64;
		}
		if (id == 302) {
			OUTPUT -= shift ? 512 : 64;
		}
		if (id == 303) {
			OUTPUT -= shift ? 4096 : 256;
			if(ctrl){
				OUTPUT = 1;
			}
		}
		if (OUTPUT > getMaxConfigOutput()) {
			OUTPUT = getMaxConfigOutput();
		}
		if (OUTPUT <= -1) {
			OUTPUT = 0;
		}
	}

	public ItemStack getDropWithNBT() {
		CompoundTag blockEntity = new CompoundTag();
		ItemStack dropStack = TRContent.Machine.ADJUSTABLE_SU.getStack();
		toTag(blockEntity);
		dropStack.setTag(new CompoundTag());
		dropStack.getTag().put("blockEntity", blockEntity);
		return dropStack;
	}
	
	public int getCurrentOutput() {
		return OUTPUT;
	}
	
	public void setCurentOutput(int output) {
		this.OUTPUT = output;
	}
	
	// TileEnergyStorage
	@Override
	public ItemStack getToolDrop(PlayerEntity entityPlayer) {
		return getDropWithNBT();
	}
	
	@Override
	public double getBaseMaxOutput() {
		return OUTPUT;
	}

	@Override
	public double getMaxOutput() {
		return OUTPUT;
	}

	@Override
	public double getBaseMaxInput() {
		//If we have super conductors increase the max input of the machine
		if(getMaxConfigOutput() > maxOutput){
			return getMaxConfigOutput();
		}
		return maxInput;
	}

	// TilePowerAcceptor
	@Override
	public CompoundTag toTag(CompoundTag tagCompound) {
		super.toTag(tagCompound);
		tagCompound.putInt("output", OUTPUT);
		inventory.write(tagCompound);
		return tagCompound;
	}

	@Override
	public void fromTag(CompoundTag nbttagcompound) {
		super.fromTag(nbttagcompound);
		this.OUTPUT = nbttagcompound.getInt("output");
		inventory.read(nbttagcompound);
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(int syncID, PlayerEntity player) {
		return new ContainerBuilder("aesu").player(player.inventory).inventory().hotbar().armor()
				.complete(8, 18).addArmor().addInventory().blockEntity(this).energySlot(0, 62, 45).energySlot(1, 98, 45)
				.syncEnergyValue().syncIntegerValue(this::getCurrentOutput, this::setCurentOutput).addInventory().create(this, syncID);
	}

	@Override
	public boolean canBeUpgraded() {
		return true;
	}

	@Override
	public boolean isUpgradeValid(IUpgrade upgrade, ItemStack stack) {
		return stack.isItemEqual(new ItemStack(TRContent.Upgrades.SUPERCONDUCTOR.item));
	}
}
