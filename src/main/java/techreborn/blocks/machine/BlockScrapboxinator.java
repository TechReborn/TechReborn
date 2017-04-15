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

package techreborn.blocks.machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IRotationTexture;

import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileScrapboxinator;

import java.util.List;

public class BlockScrapboxinator extends BlockMachineBase implements IRotationTexture {

	private final String prefix = "techreborn:blocks/machine/greg_machines/";

	public BlockScrapboxinator(final Material material) {
		super();
		this.setUnlocalizedName("techreborn.scrapboxinator");
		this.setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(final World p_149915_1_, final int p_149915_2_) {
		return new TileScrapboxinator();
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX,
			final float hitY, final float hitZ) {
		if (!player.isSneaking()) {
			player.openGui(Core.INSTANCE, EGui.SCRAPBOXINATOR.ordinal(), world, x, y, z);
		}
		return true;
	}

	@Override
	public String getFrontOff() {
		return this.prefix + "scrapboxinator_front_off";
	}

	@Override
	public String getFrontOn() {
		return this.prefix + "scrapboxinator_front_on";
	}

	@Override
	public String getSide() {
		return this.prefix + "machine_side";
	}

	@Override
	public String getTop() {
		return this.prefix + "machine_top";
	}

	@Override
	public String getBottom() {
		return this.prefix + "machine_bottom";
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(TextFormatting.RED + "WIP Coming Soon");
		//TODO Finish Scrapboxinator and add recipe
		//Remember to remove WIP override and imports once complete
	}
}
