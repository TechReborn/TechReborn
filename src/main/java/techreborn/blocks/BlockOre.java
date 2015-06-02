package techreborn.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;




import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockOre extends Block {

	public static final String[] types = new String[]
	{ "Galena", "Iridium", "Ruby", "Sapphire", "Bauxite", "Pyrite", "Cinnabar",
			"Sphalerite", "Tungston", "Sheldonite", "Peridot", "Sodalite",
			"Tetrahedrite", "Cassiterite", "Lead", "Silver" };

	private IIcon[] textures;

	public BlockOre(Material material)
	{
		super(material);
		setBlockName("techreborn.ore");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setHardness(1f);
	}

	@Override
	public Item getItemDropped(int meta, Random random, int fortune){
		if (meta == 5){
			return new ItemStack(ModItems.dusts, 1, 60).getItem();
		}
		return Item.getItemFromBlock(this);
	}
		
    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
	{
		for (int meta = 0; meta < types.length; meta++)
		{
			list.add(new ItemStack(item, 1, meta));
		}
	}

	@Override
	public int damageDropped(int metaData){
		return metaData;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.textures = new IIcon[types.length];

		for (int i = 0; i < types.length; i++)
		{
			textures[i] = iconRegister.registerIcon("techreborn:" + "ore/ore"
					+ types[i]);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metaData)
	{
		metaData = MathHelper.clamp_int(metaData, 0, types.length - 1);

		if (ForgeDirection.getOrientation(side) == ForgeDirection.UP
				|| ForgeDirection.getOrientation(side) == ForgeDirection.DOWN)
		{
			return textures[metaData];
		} else
		{
			return textures[metaData];
		}
	}

}
