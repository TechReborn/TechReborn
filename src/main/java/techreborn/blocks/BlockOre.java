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
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import reborncore.client.models.ModelCompound;
import reborncore.client.models.RebornModelRegistry;
import reborncore.common.blocks.PropertyString;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.OreDrop;
import techreborn.init.TRIngredients;
import techreborn.lib.ModInfo;
import techreborn.utils.TechRebornCreativeTab;
import techreborn.world.config.IOreNameProvider;

import java.util.Random;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class BlockOre extends Block implements IOreNameProvider {

//	public static final String[] ores = new String[] {
//		"galena", "iridium", "ruby", "sapphire", "bauxite", "pyrite",
//		"cinnabar", "sphalerite", "tungsten", "sheldonite", "peridot", "sodalite",
//		"lead", "silver" };
//	public static List<String> oreNamesList = Lists.newArrayList(ArrayUtils.arrayToLowercase(ores));
	public static final PropertyString VARIANTS = getVarients();
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
		super(Material.ROCK);
		setCreativeTab(TechRebornCreativeTab.instance);
		setHardness(2.0f);
		setHarvestLevel("pickaxe", 2);
		// TODO: Fix block
//		for (int i = 0; i < ores.length; i++) {
//			RebornModelRegistry.registerModel(new ModelCompound(ModInfo.MOD_ID, this, i).setInvVariant("type=" + OreBlockStateManager.convert(ores[i])).setFileName("ores"));
//		}
	}

//	public static ItemStack getOreByName(String name, int count) {
//		name = OreBlockStateManager.invert(name);
//		for (int i = 0; i < ores.length; i++) {
//			if (ores[i].equalsIgnoreCase(name)) {
//				return new ItemStack(ModBlocks.ORE, count, i);
//			}
//		}
//		return BlockOre2.getOreByName(name, count);
//	}

//	public static ItemStack getOreByName(String name) {
//		return getOreByName(name, 1);
//	}

//	public IBlockState getBlockStateFromName(String name) {
//		name = OreBlockStateManager.invert(name);
//		int index = -1;
//		for (int i = 0; i < ores.length; i++) {
//			if (ores[i].equalsIgnoreCase(name)) {
//				index = i;
//				break;
//			}
//		}
//		if (index == -1) {
//			return ModBlocks.ORE2.getBlockStateFromName(name);
//		}
//		return getStateFromMeta(index);
//	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		String variant = state.getValue(VARIANTS);
		int meta = getMetaFromState(state);
		Random random = new Random();

		// Secondary drop, like peridot from sapphire ore added via event handler. 
		if (variant.equalsIgnoreCase("Ruby")) {
			OreDrop ruby = new OreDrop(TRIngredients.Gems.RUBY.getStack(rubyMinQuatity), rubyMaxQuantity);
			drops.add(ruby.getDrops(fortune, random));
		} else if (variant.equalsIgnoreCase("Sapphire")) {
			OreDrop sapphire = new OreDrop(TRIngredients.Gems.SAPPHIRE.getStack(sapphireMinQuantity), sapphireMaxQuantity);
			drops.add(sapphire.getDrops(fortune, random));
		} else if (variant.equalsIgnoreCase("Pyrite")) {
			OreDrop pyriteDust = new OreDrop(TRIngredients.Dusts.PYRITE.getStack(pyriteMinQuatity), pyriteMaxQuantity);
			drops.add(pyriteDust.getDrops(fortune, random));
		} else if (variant.equalsIgnoreCase("Sodalite")) {
			OreDrop sodalite = new OreDrop(TRIngredients.Dusts.SODALITE.getStack(sodaliteMinQuatity), sodaliteMaxQuantity);
			drops.add(sodalite.getDrops(fortune, random));
		} else if (variant.equalsIgnoreCase("Cinnabar")) {
			OreDrop cinnabar = new OreDrop(TRIngredients.Dusts.CINNABAR.getStack(cinnabarMinQuatity), cinnabarMaxQuantity);
			drops.add(cinnabar.getDrops(fortune, random));
		} else if (variant.equalsIgnoreCase("Sphalerite")) {
			OreDrop sphalerite = new OreDrop(TRIngredients.Dusts.SPHALERITE.getStack(sphaleriteMinQuatity), sphaleriteMaxQuantity);
			drops.add(sphalerite.getDrops(fortune, random));
		} else {
			drops.add(new ItemStack(Item.getItemFromBlock(this), 1, meta));
		}

		return;
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}

//	@Override
//	@SideOnly(Side.CLIENT)
//	public void getSubBlocks(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
//		for (int meta = 0; meta < ores.length; meta++) {
//			list.add(new ItemStack(this, 1, meta));
//		}
//	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1, getMetaFromState(state));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

//	@Override
//	public IBlockState getStateFromMeta(int meta) {
//		if (meta > ores.length) {
//			meta = 0;
//		}
//		return getBlockState().getBaseState().withProperty(VARIANTS, oreNamesList.get(meta));
//	}

//	@Override
//	public int getMetaFromState(IBlockState state) {
//		return oreNamesList.indexOf(state.getValue(VARIANTS));
//	}
//
//	@Override
//	protected BlockStateContainer createBlockState() {
//		return new BlockStateContainer(this, VARIANTS);
//	}

	@Override
	public String getUserLoclisedName(IBlockState state) {
//		return StringUtils.toFirstCapital(oreNamesList.get(getMetaFromState(state)));
		return null;
	}

	public static PropertyString getVarients() {
//		if (OreBlockStateManager.endOreStone) {
//			oreNamesList = BlockOre.oreNamesList.stream().map(OreBlockStateManager::convert).collect(Collectors.toList());
//			return new PropertyString("type", oreNamesList);
//		} else {
//			return new PropertyString("type", BlockOre.oreNamesList);
//		}
		return null;
	}

}
