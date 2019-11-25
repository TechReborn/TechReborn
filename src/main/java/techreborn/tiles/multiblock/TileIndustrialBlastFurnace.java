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

package techreborn.tiles.multiblock;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.multiblock.IMultiblockPart;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.Core;
import techreborn.api.Reference;
import techreborn.api.recipe.ITileRecipeHandler;
import techreborn.api.recipe.machines.BlastFurnaceRecipe;
import techreborn.blocks.BlockMachineCasing;
import techreborn.init.ModBlocks;
import techreborn.items.ingredients.ItemParts;
import techreborn.lib.ModInfo;
import techreborn.multiblocks.MultiBlockCasing;
import techreborn.tiles.TileGenericMachine;
import techreborn.tiles.TileMachineCasing;

import java.util.ArrayList;
import java.util.List;

/**
 * @author modmuss50, drcrazy, estebes
 */
@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileIndustrialBlastFurnace extends TileGenericMachine implements IContainerProvider, ITileRecipeHandler<BlastFurnaceRecipe> {
	// Fields >>
	@ConfigRegistry(config = "machines", category = "industrial_furnace", key = "IndustrialFurnaceMaxInput", comment = "Industrial Blast Furnace Max Input (Value in EU)")
	public static int maxInput = 128;
	@ConfigRegistry(config = "machines", category = "industrial_furnace", key = "IndustrialFurnaceMaxEnergy", comment = "Industrial Blast Furnace Max Energy (Value in EU)")
	public static int maxEnergy = 40_000;

	public MultiblockChecker multiblockChecker;
	private int cachedHeat;

	public byte coils = 0; // to handle coils in the blast furnace
	// << Fields

	public TileIndustrialBlastFurnace() {
		super("IndustrialBlastFurnace", maxInput, maxEnergy, ModBlocks.INDUSTRIAL_BLAST_FURNACE, 4);
		final int[] inputs = new int[] { 0, 1 };
		final int[] outputs = new int[] { 2, 3 };
		this.inventory = new Inventory(5, "TileIndustrialBlastFurnace", 64, this);
		this.crafter = new RecipeCrafter(Reference.BLAST_FURNACE_RECIPE, this, 2, 2, this.inventory, inputs, outputs);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		coils = tag.getByte("coils");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setByte("coils", coils);
		return tag;
	}

	public int getHeat() {
		if (!getMutliBlock()) return 0;
		
		// Bottom center of multiblock
		BlockPos location = pos.offset(getFacing().getOpposite(), 2);
		TileEntity tileEntity = world.getTileEntity(location);

		if (! (tileEntity instanceof TileMachineCasing)) {
			return 0;
		}
		TileMachineCasing tileCasing = (TileMachineCasing) tileEntity;
		if (tileCasing.isConnected() && tileCasing.getMultiblockController().isAssembled()) {
			MultiBlockCasing controller = tileCasing.getMultiblockController();

			int heat = (coils & 0xFF) * 500;

			// Bottom center shouldn't have any tile entities below it
			if (world.getBlockState(new BlockPos(location.getX(), location.getY() - 1, location.getZ()))
					.getBlock() == tileEntity.getBlockType()) {
				return 0;
			}

			for (final IMultiblockPart part : controller.connectedParts) {
				try {
					BlockMachineCasing casing1 = (BlockMachineCasing) world.getBlockState(part.getPos()).getBlock();
					heat += casing1.getHeatFromState(world.getBlockState(part.getPos()));
				} catch (ClassCastException e) {
					Core.logHelper.error(String.format(
							"[%s] Incorrect part (%d) @ %d, %d, %d, this is unexpected and may cause problems. If you encounter anomalies, please tear multiblock and rebuild it.",
							world.isRemote ? "CLIENT" : "SERVER", part.hashCode(), part.getPos().getX(),
							part.getPos().getY(), part.getPos().getZ()));
				}
			}

			if (world.getBlockState(location.offset(EnumFacing.UP, 1)).getBlock().getUnlocalizedName().equals("tile.lava")
					&& world.getBlockState(location.offset(EnumFacing.UP, 2)).getBlock().getUnlocalizedName().equals("tile.lava")) {
				heat += 500;
			}
			return heat;
		}
		
		return 0;
	}
	
	public boolean getMutliBlock() {
		if (multiblockChecker == null) return false;

		final boolean layer0 = multiblockChecker.checkRectY(1, 1, MultiblockChecker.CASING_ANY, MultiblockChecker.ZERO_OFFSET);
		final boolean layer1 = multiblockChecker.checkRingY(1, 1, MultiblockChecker.CASING_ANY, new BlockPos(0, 1, 0));
		final boolean layer2 = multiblockChecker.checkRingY(1, 1, MultiblockChecker.CASING_ANY, new BlockPos(0, 2, 0));
		final boolean layer3 = multiblockChecker.checkRectY(1, 1, MultiblockChecker.CASING_ANY, new BlockPos(0, 3, 0));
		final Material centerBlock1 = multiblockChecker.getBlock(0, 1, 0).getMaterial();
		final Material centerBlock2 = multiblockChecker.getBlock(0, 2, 0).getMaterial();
		final boolean center1 = (centerBlock1 == Material.AIR || centerBlock1 == Material.LAVA);
		final boolean center2 = (centerBlock2 == Material.AIR || centerBlock2 == Material.LAVA);
		return layer0 && layer1 && layer2 && layer3 && center1 && center2;
	}
	
	public void setHeat(final int heat) {
		cachedHeat = heat;
	}

	public int getCachedHeat() {
		return cachedHeat;
	}
	
	// TileGenericMachine
	@Override
	public void update() {
		if (multiblockChecker == null) {
			final BlockPos downCenter = pos.offset(getFacing().getOpposite(), 2);
			multiblockChecker = new MultiblockChecker(world, downCenter);
		}
		
		if (getMutliBlock()) super.update();
	}

	// RebornMachineTile
	@Override
	public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity packet) {
		world.markBlockRangeForRenderUpdate(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
		readFromNBT(packet.getNbtCompound());
	}
	
	// IContainerProvider
	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("blastfurnace").player(player.inventory).inventory().hotbar().addInventory()
				.tile(this).slot(0, 50, 27).slot(1, 50, 47).outputSlot(2, 93, 37).outputSlot(3, 113, 37)
				.energySlot(4, 8, 72).syncEnergyValue().syncCrafterValue()
				.syncIntegerValue(this::getHeat, this::setHeat).addInventory().create(this);
	}
	
	// ITileRecipeHandler
	@Override
	public boolean canCraft(final TileEntity tile, final BlastFurnaceRecipe recipe) {
		if (tile instanceof TileIndustrialBlastFurnace) {
			final TileIndustrialBlastFurnace blastFurnace = (TileIndustrialBlastFurnace) tile;
			return blastFurnace.getHeat() >= recipe.neededHeat;
		}

		return false;
	}

	@Override
	public boolean onCraft(final TileEntity tile, final BlastFurnaceRecipe recipe) {
		return true;
	}

	// IToolDrop >>
	@Override
	public ItemStack getToolDrop(EntityPlayer entityPlayer) {
		ItemStack ret = new ItemStack(getBlockType(), 1);
		NBTTagCompound nbt = ItemUtils.getStackNbtData(ret);
		nbt.setByte("coils", coils);
		return ret;
	}


	// << IToolDrop

	// Handle coils >>
	public boolean addCoils(ItemStack stack) {
		switch (coils) {
			case 0:
				if (ItemUtils.isItemEqual(stack, ItemParts.getPartByName("kanthal_heating_coil"), true, true)) {
					coils++;
					return true;
				}
				break;
			case 1:
				if (ItemUtils.isItemEqual(stack, ItemParts.getPartByName("nichrome_heating_coil"), true, true)) {
					coils++;
					return true;
				}
				break;
		}

		return false;
	}

	public List<ItemStack> getCoils() {
		List<ItemStack> ret = new ArrayList<ItemStack>();

		switch (coils) {
			case 1:
				ret.add(ItemParts.getPartByName("kanthal_heating_coil", 4));
				break;
			case 2:
				ret.add(ItemParts.getPartByName("kanthal_heating_coil", 4));
				ret.add(ItemParts.getPartByName("nichrome_heating_coil", 4));
				break;
		}

		return ret;
	}
	// << Handle coils
}
