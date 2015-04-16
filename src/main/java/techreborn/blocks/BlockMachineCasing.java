package techreborn.blocks;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import techreborn.client.TechRebornCreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockMachineCasing extends Block{
	
	public static final String[] types = new String[] {"Standard", "Reinforced", "Advanced"};
    private IIcon[] textures;

	public BlockMachineCasing(Material material) 
	{
		super(material);
		setCreativeTab(TechRebornCreativeTab.instance);
		setBlockName("techreborn.machineCasing");
		setHardness(2F);
	}
	
    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        return Item.getItemFromBlock(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        for (int meta = 0; meta < types.length; meta++) {
            list.add(new ItemStack(item, 1, meta));
        }
    }

    @Override
    public int damageDropped(int metaData) {
        //TODO RubyOre Returns Rubys
        return metaData;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.textures = new IIcon[types.length];

        for (int i = 0; i < types.length; i++) {
            textures[i] = iconRegister.registerIcon("techreborn:" + "machine/casing" + types[i]);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metaData) {
        metaData = MathHelper.clamp_int(metaData, 0, types.length - 1);

        if (ForgeDirection.getOrientation(side) == ForgeDirection.UP
                || ForgeDirection.getOrientation(side) == ForgeDirection.DOWN) {
            return textures[metaData];
        } else {
            return textures[metaData];
        }
    }

}
