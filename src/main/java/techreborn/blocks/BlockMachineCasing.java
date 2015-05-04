package techreborn.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileMachineCasing;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import erogenousbeef.coreTR.multiblock.BlockMultiblockBase;

public class BlockMachineCasing extends BlockMultiblockBase {

	public static final String[] types = new String[]
	{ "standard", "reinforced", "advanced" };
	private IIcon[] textures;

	public BlockMachineCasing(Material material)
	{
		super(material);
		setCreativeTab(TechRebornCreativeTab.instance);
		setBlockName("techreborn.machineCasing");
		setHardness(2F);
	}

	@Override
	public Item getItemDropped(int meta, Random random, int fortune)
	{
		return Item.getItemFromBlock(this);
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
	public int damageDropped(int metaData)
	{
		return metaData;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.textures = new IIcon[types.length];

		for (int i = 0; i < types.length; i++)
		{
			textures[i] = iconRegister.registerIcon("techreborn:"
					+ "machine/casing" + types[i]);
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

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new TileMachineCasing();
	}

    public static int getHeatFromMeta(int meta){
        switch (meta){
            case 0:
                return 1020;
            case 1:
                return 1700;
            case 2:
                return 2380;
        }
        return 0;
    }
}
