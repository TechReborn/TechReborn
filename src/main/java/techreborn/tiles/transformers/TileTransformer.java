/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.tiles.transformers;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.TilePowerAcceptor;
import techreborn.blocks.transformers.BlockTransformer;

/**
 * Created by Rushmead
 */
public class TileTransformer extends TilePowerAcceptor implements IWrenchable, ITickable {

	public String name;
	public Block wrenchDrop;
	public EnumPowerTier tier;
	public int maxInput;
	public int maxOutput;
	public int maxStorage;

	public TileTransformer(String name, Block wrenchDrop, EnumPowerTier tier, int maxInput, int maxOuput, int maxStorage) {
		super(1);
		this.wrenchDrop = wrenchDrop;
		this.tier = tier;
		this.name = name;
		this.maxInput = maxInput;
		this.maxOutput = maxOuput;
		this.maxStorage = maxStorage;
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer p0, EnumFacing p1) {
		return true;
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
	public ItemStack getWrenchDrop(EntityPlayer p0) {
		return new ItemStack(wrenchDrop);
	}

	@Override
	public double getMaxPower() {
		return maxStorage;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction) {

		return getFacingEnum() != direction;
	}

	@Override
	public EnumFacing getFacingEnum() {
		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof BlockTransformer) {
			return ((BlockTransformer) block).getFacing(world.getBlockState(pos));
		}
		return null;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction) {
		return getFacing() == direction;
	}

	@Override
	public double getMaxOutput() {
		return maxOutput;
	}

	@Override
	public double getMaxInput() {
		return maxInput;
	}

	@Override
	public EnumPowerTier getTier() {
		return tier;
	}
}
