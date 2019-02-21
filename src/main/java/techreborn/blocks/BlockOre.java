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

package techreborn.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.OreDrop;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import java.util.Random;

@RebornRegister(TechReborn.MOD_ID)
public class BlockOre extends Block {

	@ConfigRegistry(config = "misc", category = "blocks", key = "rubyMinQuatity", comment = "Minimum quantity of Ruby gems per Ruby ore")
	public static int rubyMinQuatity = 1;
	@ConfigRegistry(config = "misc", category = "blocks", key = "rubyMaxQuantity", comment = "Maximum quantity of Ruby gems per Ruby ore")
	public static int rubyMaxQuantity = 2;
	@ConfigRegistry(config = "misc", category = "blocks", key = "sapphireMinQuantity", comment = "Minimum quantity of Sapphire gems per Sapphire ore")
	public static int sapphireMinQuantity = 1;
	@ConfigRegistry(config = "misc", category = "blocks", key = "sapphireMaxQuantity", comment = "Maximum quantity of Sapphire gems per Sapphire ore")
	public static int sapphireMaxQuantity = 2;
	@ConfigRegistry(config = "misc", category = "blocks", key = "pyriteMinQuatity", comment = "Minimum quantity of Pyrite dust per Pyrite ore")
	public static int pyriteMinQuatity = 1;
	@ConfigRegistry(config = "misc", category = "blocks", key = "pyriteMaxQuantity", comment = "Maximum quantity of Pyrite dust per Pyrite ore")
	public static int pyriteMaxQuantity = 2;
	@ConfigRegistry(config = "misc", category = "blocks", key = "sodaliteMinQuatity", comment = "Minimum quantity of Sodalite dust per Sodalite ore")
	public static int sodaliteMinQuatity = 1;
	@ConfigRegistry(config = "misc", category = "blocks", key = "sodaliteMaxQuantity", comment = "Maximum quantity of Sodalite dust per Sodalite ore")
	public static int sodaliteMaxQuantity = 2;
	@ConfigRegistry(config = "misc", category = "blocks", key = "cinnabarMinQuatity", comment = "Minimum quantity of Cinnabar dust per Cinnabar ore")
	public static int cinnabarMinQuatity = 1;
	@ConfigRegistry(config = "misc", category = "blocks", key = "cinnabarMaxQuantity", comment = "Maximum quantity of Cinnabar dust per Cinnabar ore")
	public static int cinnabarMaxQuantity = 2;
	@ConfigRegistry(config = "misc", category = "blocks", key = "sphaleriteMinQuatity", comment = "Minimum quantity of Sphalerite dust per Sphalerite ore")
	public static int sphaleriteMinQuatity = 1;
	@ConfigRegistry(config = "misc", category = "blocks", key = "sphaleriteMaxQuantity", comment = "Maximum quantity of Sphalerite dust per Sphalerite ore")
	public static int sphaleriteMaxQuantity = 2;

	public BlockOre() {
		super(Block.Properties.create(Material.ROCK).hardnessAndResistance(2f));
	}

	@Override
	public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune) {
		Block ore = state.getBlock();
		Random random = new Random();

		// Secondary drop, like Yellow Garnet from Sphalerite ore added via event handler.
		if (ore == TRContent.Ores.RUBY.block) {
			OreDrop ruby = new OreDrop(TRContent.Gems.RUBY.getStack(rubyMinQuatity), rubyMaxQuantity);
			drops.add(ruby.getDrops(fortune, random));
		} else if (ore == TRContent.Ores.SAPPHIRE.block) {
			OreDrop sapphire = new OreDrop(TRContent.Gems.SAPPHIRE.getStack(sapphireMinQuantity), sapphireMaxQuantity);
			drops.add(sapphire.getDrops(fortune, random));
		} else if (ore == TRContent.Ores.PYRITE.block) {
			OreDrop pyriteDust = new OreDrop(TRContent.Dusts.PYRITE.getStack(pyriteMinQuatity), pyriteMaxQuantity);
			drops.add(pyriteDust.getDrops(fortune, random));
		} else if (ore == TRContent.Ores.SODALITE.block) {
			OreDrop sodalite = new OreDrop(TRContent.Dusts.SODALITE.getStack(sodaliteMinQuatity), sodaliteMaxQuantity);
			drops.add(sodalite.getDrops(fortune, random));
		} else if (ore == TRContent.Ores.CINNABAR.block) {
			OreDrop cinnabar = new OreDrop(TRContent.Dusts.CINNABAR.getStack(cinnabarMinQuatity), cinnabarMaxQuantity);
			drops.add(cinnabar.getDrops(fortune, random));
		} else if (ore == TRContent.Ores.SPHALERITE.block) {
			OreDrop sphalerite = new OreDrop(TRContent.Dusts.SPHALERITE.getStack(sphaleriteMinQuatity), sphaleriteMaxQuantity);
			drops.add(sphalerite.getDrops(fortune, random));
		} else {
			drops.add(new ItemStack(this));
		}

		return;
	}
}
