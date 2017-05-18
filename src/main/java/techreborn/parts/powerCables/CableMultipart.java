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

package techreborn.parts.powerCables;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import reborncore.common.RebornCoreConfig;
import reborncore.common.misc.Functions;
import reborncore.common.misc.vecmath.Vecs3dCube;
import reborncore.common.util.WorldUtils;
import reborncore.mcmultipart.MCMultiPartMod;
import reborncore.mcmultipart.microblock.IMicroblock;
import reborncore.mcmultipart.multipart.*;
import reborncore.mcmultipart.raytrace.PartMOP;
import techreborn.parts.TechRebornParts;
import techreborn.parts.walia.IPartWaliaProvider;

import java.util.*;

/**
 * Created by modmuss50 on 02/03/2016.
 */
public abstract class CableMultipart extends Multipart
	implements INormallyOccludingPart, ISlottedPart, ITickable, ICableType, IPartWaliaProvider, IEnergyStorage {

	public static final IUnlistedProperty<Boolean> UP = Properties.toUnlisted(PropertyBool.create("up"));
	public static final IUnlistedProperty<Boolean> DOWN = Properties.toUnlisted(PropertyBool.create("down"));
	public static final IUnlistedProperty<Boolean> NORTH = Properties.toUnlisted(PropertyBool.create("north"));
	public static final IUnlistedProperty<Boolean> EAST = Properties.toUnlisted(PropertyBool.create("east"));
	public static final IUnlistedProperty<Boolean> SOUTH = Properties.toUnlisted(PropertyBool.create("south"));
	public static final IUnlistedProperty<Boolean> WEST = Properties.toUnlisted(PropertyBool.create("west"));
	public static final IProperty<EnumCableType> TYPE = PropertyEnum.create("type", EnumCableType.class);
	public Vecs3dCube[] boundingBoxes = new Vecs3dCube[14];
	public float center = 0.6F;
	public float offset = 0.10F;
	public Map<EnumFacing, BlockPos> connectedSides;
	public int ticks = 0;
	public ItemStack stack;
	public int power = 0;

	public CableMultipart() {
		connectedSides = new HashMap<>();
		refreshBounding();
	}

	public static CableMultipart getPartFromWorld(World world, BlockPos pos, EnumFacing side) {
		if (world == null || pos == null) {
			return null;
		}
		IMultipartContainer container = MultipartHelper.getPartContainer(world, pos);
		if (side != null && container != null) {
			ISlottedPart slottedPart = container.getPartInSlot(PartSlot.getFaceSlot(side));
			if (slottedPart instanceof IMicroblock.IFaceMicroblock
				&& !((IMicroblock.IFaceMicroblock) slottedPart).isFaceHollow()) {
				return null;
			}
		}

		if (container == null) {
			return null;
		}
		ISlottedPart part = container.getPartInSlot(PartSlot.CENTER);
		if (part instanceof CableMultipart) {
			return (CableMultipart) part;
		}
		return null;
	}

	public void refreshBounding() {
		float centerFirst = center - offset;
		double w = (getCableType().cableThickness / 16) - 0.5;
		boundingBoxes[6] = new Vecs3dCube(centerFirst - w, centerFirst - w, centerFirst - w, centerFirst + w,
			centerFirst + w, centerFirst + w);

		int i = 0;
		for (EnumFacing dir : EnumFacing.VALUES) {
			double xMin1 = (dir.getFrontOffsetX() < 0 ? 0.0
			                                          : (dir.getFrontOffsetX() == 0 ? centerFirst - w : centerFirst + w));
			double xMax1 = (dir.getFrontOffsetX() > 0 ? 1.0
			                                          : (dir.getFrontOffsetX() == 0 ? centerFirst + w : centerFirst - w));

			double yMin1 = (dir.getFrontOffsetY() < 0 ? 0.0
			                                          : (dir.getFrontOffsetY() == 0 ? centerFirst - w : centerFirst + w));
			double yMax1 = (dir.getFrontOffsetY() > 0 ? 1.0
			                                          : (dir.getFrontOffsetY() == 0 ? centerFirst + w : centerFirst - w));

			double zMin1 = (dir.getFrontOffsetZ() < 0 ? 0.0
			                                          : (dir.getFrontOffsetZ() == 0 ? centerFirst - w : centerFirst + w));
			double zMax1 = (dir.getFrontOffsetZ() > 0 ? 1.0
			                                          : (dir.getFrontOffsetZ() == 0 ? centerFirst + w : centerFirst - w));

			boundingBoxes[i] = new Vecs3dCube(xMin1, yMin1, zMin1, xMax1, yMax1, zMax1);
			i++;
		}
	}

	@Override
	public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {

		for (EnumFacing dir : EnumFacing.VALUES) {
			if (connectedSides.containsKey(dir)
				&& mask.intersectsWith(boundingBoxes[Functions.getIntDirFromDirection(dir)].toAABB()))
				list.add(boundingBoxes[Functions.getIntDirFromDirection(dir)].toAABB());
		}
		if (mask.intersectsWith(boundingBoxes[6].toAABB())) {
			list.add(boundingBoxes[6].toAABB());
		}
		super.addCollisionBoxes(mask, list, collidingEntity);
	}

	@Override
	public void addSelectionBoxes(List<AxisAlignedBB> list) {

		for (EnumFacing dir : EnumFacing.VALUES) {
			if (connectedSides.containsKey(dir))
				list.add(boundingBoxes[Functions.getIntDirFromDirection(dir)].toAABB());
		}
		list.add(boundingBoxes[6].toAABB());
		super.addSelectionBoxes(list);
	}

	@Override
	public void onRemoved() {
		super.onRemoved();
		for (EnumFacing dir : EnumFacing.VALUES) {
			CableMultipart multipart = getPartFromWorld(getWorld(), getPos().offset(dir), dir);
			if (multipart != null) {
				multipart.nearByChange();
			}
		}
	}

	@Override
	public void addOcclusionBoxes(List<AxisAlignedBB> list) {
		for (EnumFacing dir : EnumFacing.VALUES) {
			if (connectedSides.containsKey(dir))
				list.add(boundingBoxes[Functions.getIntDirFromDirection(dir)].toAABB());
		}
		list.add(boundingBoxes[6].toAABB());
	}

	@Override
	public void onNeighborBlockChange(Block block) {
		super.onNeighborBlockChange(block);
		nearByChange();

	}

	public void nearByChange() {
		checkConnectedSides();
		for (EnumFacing direction : EnumFacing.VALUES) {
			BlockPos blockPos = getPos().offset(direction);
			WorldUtils.updateBlock(getWorld(), blockPos);
			CableMultipart part = getPartFromWorld(getWorld(), blockPos, direction);
			if (part != null) {
				part.checkConnectedSides();
			}
		}

	}

	@Override
	public void onAdded() {
		checkConnectedSides();
	}

	public boolean shouldConnectTo(EnumFacing dir) {
		if (dir != null) {
			if (internalShouldConnectTo(dir)) {
				CableMultipart cableMultipart = getPartFromWorld(getWorld(), getPos().offset(dir), dir);
				if (cableMultipart != null && cableMultipart.internalShouldConnectTo(dir.getOpposite())) {
					return true;
				}
			} else {
				TileEntity tile = getNeighbourTile(dir);
				if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, dir)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean internalShouldConnectTo(EnumFacing dir) {
		ISlottedPart part = getContainer().getPartInSlot(PartSlot.getFaceSlot(dir));
		if (part instanceof IMicroblock.IFaceMicroblock) {
			if (!((IMicroblock.IFaceMicroblock) part).isFaceHollow()) {
				return false;
			}
		}

		if (!OcclusionHelper.occlusionTest(getContainer().getParts(), p -> p == this, boundingBoxes[Functions.getIntDirFromDirection(dir)].toAABB())) {
			return false;
		}

		CableMultipart cableMultipart = getPartFromWorld(getWorld(), getPos().offset(dir), dir.getOpposite());

		return cableMultipart != null && cableMultipart.getCableType() == getCableType();
	}

	public TileEntity getNeighbourTile(EnumFacing side) {
		return side != null ? getWorld().getTileEntity(getPos().offset(side)) : null;
	}

	public void checkConnectedSides() {
		refreshBounding();
		connectedSides = new HashMap<>();
		for (EnumFacing dir : EnumFacing.values()) {
			int d = Functions.getIntDirFromDirection(dir);
			if (getWorld() == null) {
				return;
			}
			TileEntity te = getNeighbourTile(dir);
			if (shouldConnectTo(dir)) {
				connectedSides.put(dir, te.getPos());
			}
		}
	}

	@Override
	public EnumSet<PartSlot> getSlotMask() {
		return EnumSet.of(PartSlot.CENTER);
	}

	@Override
	public void update() {
		if (getWorld() != null) {
			ticks ++;
			if (getWorld().getTotalWorldTime() % 80 == 0) {
				checkConnectedSides();
			}
			tickPower();
		}
	}

	public void tickPower(){
		for (EnumFacing face : EnumFacing.VALUES) {
			if (connectedSides.containsKey(face)) {
				BlockPos offPos = getPos().offset(face);
				TileEntity tile = getWorld().getTileEntity(offPos);
				if(tile == null){
					continue;
				}
				if (tile.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite())) {
					IEnergyStorage energy = tile.getCapability(CapabilityEnergy.ENERGY, face.getOpposite());
					if (energy.canReceive()) {
						int move = energy.receiveEnergy(Math.min(getCableType().transferRate, power), false);
						if (move != 0) {
							power -= move;
						}
					}
				}

				CableMultipart pipe = getPartFromWorld(getWorld(), getPos().offset(face), face);
				if (pipe != null) {
					int averPower = (power + pipe.power) / 2;
					pipe.power = averPower;
					if(averPower % 2 != 0 && power != 0){
						averPower ++;
					}
					power = averPower;

				}
			}
		}
	}

	@Override
	public IBlockState getExtendedState(IBlockState state) {
		IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
		return extendedBlockState.withProperty(DOWN, shouldConnectTo(EnumFacing.DOWN))
			.withProperty(UP, shouldConnectTo(EnumFacing.UP)).withProperty(NORTH, shouldConnectTo(EnumFacing.NORTH))
			.withProperty(SOUTH, shouldConnectTo(EnumFacing.SOUTH))
			.withProperty(WEST, shouldConnectTo(EnumFacing.WEST))
			.withProperty(EAST, shouldConnectTo(EnumFacing.EAST)).withProperty(TYPE, getCableType());
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new ExtendedBlockState(MCMultiPartMod.multipart, new IProperty[] { TYPE },
			new IUnlistedProperty[] { DOWN, UP, NORTH, SOUTH, WEST, EAST });
	}

	@Override
	public float getHardness(PartMOP hit) {
		return 0.5F;
	}

	public Material getMaterial() {
		return Material.CLOTH;
	}

	@Override
	public List<ItemStack> getDrops() {
		List<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(TechRebornParts.cables, 1, getCableType().ordinal()));
		return list;
	}


	@Override
	public void onEntityStanding(Entity entity) {

	}

	@Override
	public ItemStack getPickBlock(EntityPlayer player, PartMOP hit) {
		return new ItemStack(TechRebornParts.cables, 1, getCableType().ordinal());
	}


	@Override
	public void addInfo(List<String> info) {
		info.add(TextFormatting.GREEN + "EU Transfer: " +
			TextFormatting.LIGHT_PURPLE + getCableType().transferRate);
		if (getCableType().canKill) {
			info.add(TextFormatting.RED + "Damages entity's!");
		}
	}

	@Override
	public ResourceLocation getModelPath() {
		return new ResourceLocation("techreborn:cable");
	}

	@Override
	public boolean canRenderInLayer(BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		if (!canReceive()){
			return 0;
		}

		int energyReceived = Math.min(getMaxEnergyStored() - power, Math.min(getCableType().transferRate * RebornCoreConfig.euPerFU, maxReceive));
		if (!simulate)
			power += energyReceived;
		return energyReceived;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		if (!canExtract()){
			return 0;
		}

		int energyExtracted = Math.min(power, Math.min(getCableType().transferRate * RebornCoreConfig.euPerFU, maxExtract));
		if (!simulate)
			power -= energyExtracted;
		return energyExtracted;
	}

	@Override
	public int getEnergyStored() {
		return power;
	}

	@Override
	public int getMaxEnergyStored() {
		return getCableType().transferRate * 2;
	}

	@Override
	public boolean canExtract() {
		return true;
	}

	@Override
	public boolean canReceive() {
		return true;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY){
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY){
			return (T) this;
		}
		return super.getCapability(capability, facing);
	}
}
