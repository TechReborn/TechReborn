package techreborn.blocks;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import techreborn.client.TechRebornCreativeTab;

import java.util.List;
import java.util.Random;

public class BlockPlayerDetector extends BlockMachineBase {

    public BlockPlayerDetector() {
        super(Material.rock);
        setBlockName("techreborn.playerDetector");
        setCreativeTab(TechRebornCreativeTab.instance);
        setHardness(2f);
    }

    public static final String[] types = new String[]
            {"all", "others", "you"};

    private IIcon[] textures;

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
                    + "machine/player_detector_" + types[i]);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metaData) {
        return textures[MathHelper.clamp_int(metaData, 0, types.length - 1)];
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {

    }
}
