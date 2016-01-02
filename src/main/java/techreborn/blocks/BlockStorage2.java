package techreborn.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModBlocks;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Random;

public class BlockStorage2 extends Block {

    public static ItemStack getStorageBlockByName(String name, int count) {
        for (int i = 0; i < types.length; i++) {
            if (types[i].equals(name)) {
                return new ItemStack(ModBlocks.storage2, count, i);
            }
        }
        throw new InvalidParameterException("The storage block " + name + " could not be found.");
    }

    public static final String[] types = new String[]
            {"tungstensteel", "lodestone", "tellurium", "iridium_reinforced_tungstensteel",
                    "iridium_reinforced_stone", "ruby", "sapphire", "peridot", "yellowGarnet", "redGarnet"};

    private IIcon[] textures;

    public BlockStorage2(Material material) {
        super(material);
        setBlockName("techreborn.storage2");
        setCreativeTab(TechRebornCreativeTabMisc.instance);
        setHardness(2f);
        ModBlocks.blocksToCut.add(this);
    }

    @Override
    public Item getItemDropped(int par1, Random random, int par2) {
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
        return metaData;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.textures = new IIcon[types.length];

        for (int i = 0; i < types.length; i++) {
            textures[i] = iconRegister.registerIcon("techreborn:"
                    + "storage/" + types[i] + "_block");
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
