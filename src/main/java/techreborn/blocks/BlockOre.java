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

package techreborn.blocks;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.common.blocks.PropertyString;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.ArrayUtils;
import reborncore.common.util.OreDrop;
import reborncore.common.util.OreDropSet;
import reborncore.common.util.StringUtils;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModBlocks;
import techreborn.items.ItemDusts;
import techreborn.items.ItemGems;
import techreborn.lib.ModInfo;
import techreborn.world.config.IOreNameProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class BlockOre extends Block implements IOreNameProvider {

	public static final String[] ores = new String[] {
		"galena", "iridium", "ruby", "sapphire", "bauxite", "pyrite",
		"cinnabar", "sphalerite", "tungsten", "sheldonite", "peridot", "sodalite",
		"lead", "silver" };
	private static final List<String> oreNamesList = Lists.newArrayList(ArrayUtils.arrayToLowercase(ores));
	public static final PropertyString VARIANTS = new PropertyString("type", oreNamesList);
	@ConfigRegistry(category = "blocks", key = "secondaryDropChance", comment = "Secondary Gem drop chance")
	public static double secondaryDropChance = 0.5;

	public BlockOre() {
		super(Material.ROCK);
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setHardness(2.0f);
		setHarvestLevel("pickaxe", 2);
		this.setDefaultState(this.getStateFromMeta(0));
		for (int i = 0; i < ores.length; i++) {
			ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, i).setInvVariant("type=" + ores[i]).setFileName("ores"));
		}
	}

	public static ItemStack getOreByName(String name, int count) {
		for (int i = 0; i < ores.length; i++) {
			if (ores[i].equalsIgnoreCase(name)) {
				return new ItemStack(ModBlocks.ORE, count, i);
			}
		}
		return BlockOre2.getOreByName(name, count);
	}

	public static ItemStack getOreByName(String name) {
		return getOreByName(name, 1);
	}

	public IBlockState getBlockStateFromName(String name) {
		int index = -1;
		for (int i = 0; i < ores.length; i++) {
			if (ores[i].equalsIgnoreCase(name)) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			return ModBlocks.ORE2.getBlockStateFromName(name);
		}
		return getStateFromMeta(index);
	}

	@Deprecated
	public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		String variant = state.getValue(VARIANTS);
		int meta = getMetaFromState(state);
		Random random = new Random();
		// Ruby
		if (variant.equalsIgnoreCase("Ruby")) {
			OreDrop ruby = new OreDrop(ItemGems.getGemByName("ruby"),
				secondaryDropChance);
			OreDrop redGarnet = new OreDrop(ItemGems.getGemByName("red_garnet"), 0.02);
			OreDropSet set = new OreDropSet(ruby);
			return set.drop(fortune, random);
		}

		// Sapphire
		if (variant.equalsIgnoreCase("Sapphire")) {
			OreDrop sapphire = new OreDrop(ItemGems.getGemByName("sapphire"),
				secondaryDropChance);
			OreDrop peridot = new OreDrop(ItemGems.getGemByName("peridot"), 0.03);
			OreDropSet set = new OreDropSet(sapphire, peridot);
			return set.drop(fortune, random);
		}

		// Pyrite
		if (variant.equalsIgnoreCase("Pyrite")) {
			OreDrop pyriteDust = new OreDrop(ItemDusts.getDustByName("pyrite"),
				secondaryDropChance);
			OreDropSet set = new OreDropSet(pyriteDust);
			return set.drop(fortune, random);
		}

		// Sodalite
		if (variant.equalsIgnoreCase("Sodalite")) {
			OreDrop sodalite = new OreDrop(ItemDusts.getDustByName("sodalite", 6),
				secondaryDropChance);
			OreDrop aluminum = new OreDrop(ItemDusts.getDustByName("aluminum"), 0.50);
			OreDropSet set = new OreDropSet(sodalite, aluminum);
			return set.drop(fortune, random);
		}

		// Cinnabar
		if (variant.equalsIgnoreCase("Cinnabar")) {
			OreDrop cinnabar = new OreDrop(ItemDusts.getDustByName("cinnabar"),
				secondaryDropChance);
			OreDrop redstone = new OreDrop(new ItemStack(Items.REDSTONE), 0.25);
			OreDropSet set = new OreDropSet(cinnabar, redstone);
			return set.drop(fortune, random);
		}

		// Sphalerite 1, 1/8 yellow garnet
		if (variant.equalsIgnoreCase("Sphalerite")) {
			OreDrop sphalerite = new OreDrop(ItemDusts.getDustByName("sphalerite"),
				secondaryDropChance);
			OreDrop yellowGarnet = new OreDrop(ItemGems.getGemByName("yellowGarnet"), 0.125);
			OreDropSet set = new OreDropSet(sphalerite, yellowGarnet);
			return set.drop(fortune, random);
		}

		ArrayList<ItemStack> block = new ArrayList<>();
		block.add(new ItemStack(Item.getItemFromBlock(this), 1, meta));
		return block;
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
		for (int meta = 0; meta < ores.length; meta++) {
			list.add(new ItemStack(this, 1, meta));
		}
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
	                              EntityPlayer player) {
		return new ItemStack(this, 1, getMetaFromState(state));
	}

	//	@Override
	//	public int damageDropped(IBlockState state)
	//	{
	//		int meta = getMetaFromState(state);
	//		if (meta == 2)
	//		{
	//			return 0;
	//		} else if (meta == 3)
	//		{
	//			return 1;
	//		} else if (meta == 5)
	//		{
	//			return 60;
	//		}
	//		return meta;
	//	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta > ores.length) {
			meta = 0;
		}
		return getBlockState().getBaseState().withProperty(VARIANTS, oreNamesList.get(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return oreNamesList.indexOf(state.getValue(VARIANTS));
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANTS);
	}

	@Override
	public String getUserLoclisedName(IBlockState state) {
		return StringUtils.toFirstCapital(oreNamesList.get(getMetaFromState(state)));
	}

}
