package techreborn.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import erogenousbeef.coreTR.multiblock.BlockMultiblockBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import techreborn.client.TechRebornCreativeTab;
import techreborn.client.texture.ConnectedTexture;
import techreborn.client.texture.ConnectedTextureGenerator;
import techreborn.tiles.TileMachineCasing;

import java.util.List;
import java.util.Random;

public class BlockMachineCasing extends BlockMultiblockBase {

    public static final String[] types = new String[]
            {"standard", "reinforced", "advanced"};
    public IIcon[][] icons;

    public BlockMachineCasing(Material material) {
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
        return metaData;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[types.length][16];
        for (int i = 0; i < types.length; i++) {
                                                    //  up   down  left  right
            icons[i][0] = genIcon(new ConnectedTexture(true, true, true, true), iconRegister, 0, i);
            icons[i][1] = genIcon(new ConnectedTexture(true, false, true, true), iconRegister, 1, i);
            icons[i][2] = genIcon(new ConnectedTexture(false, true, true, true), iconRegister, 2, i);
            icons[i][3]  = genIcon(new ConnectedTexture(true, true, true, false), iconRegister, 3, i);
            icons[i][4] = genIcon(new ConnectedTexture(true, true, false, true), iconRegister, 4, i);
            icons[i][5] = genIcon(new ConnectedTexture(true, true, false, false), iconRegister, 5, i);
            icons[i][6] = genIcon(new ConnectedTexture(false, false, true, true), iconRegister, 6, i);
            icons[i][7] = genIcon(new ConnectedTexture(true, false, false, true), iconRegister, 7, i);
            icons[i][8] = genIcon(new ConnectedTexture(true, false, true, false), iconRegister, 8, i);
            icons[i][9] = genIcon(new ConnectedTexture(false, true, false, true), iconRegister, 9, i);
            icons[i][10] = genIcon(new ConnectedTexture(false, true, true, false), iconRegister, 9, i);
            icons[i][11] = genIcon(new ConnectedTexture(false, true, false, false), iconRegister, 9, i);
            icons[i][12] = genIcon(new ConnectedTexture(true, false, false, false), iconRegister, 9, i);
            icons[i][13] = genIcon(new ConnectedTexture(true, false, false, false), iconRegister, 9, i);
            icons[i][14] = genIcon(new ConnectedTexture(false, false, true, false), iconRegister, 9, i);
            icons[i][15] = genIcon(new ConnectedTexture(false, false, false, false), iconRegister, 9, i);
        }

    }

    public IIcon genIcon(ConnectedTexture connectedTexture, IIconRegister iconRegister, int texNum, int meta){
        if(iconRegister instanceof TextureMap){
            TextureMap map = (TextureMap) iconRegister;
            String name = ConnectedTextureGenerator.getDerivedName(types[meta] + "." + texNum);
            TextureAtlasSprite texture = map.getTextureExtry(name);
            if(texture == null){
                texture = new ConnectedTextureGenerator(name, types[meta], connectedTexture);
                map.setTextureEntry(name, texture);
            }
            return map.getTextureExtry(name);
        } else {
            return null;
        }
    }


    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
        int meta = blockAccess.getBlockMetadata(x, y, z);
        if(meta > types.length){
            return getConnectedBlockTexture(blockAccess, x, y, z, meta, icons[0]);
        }
        return getConnectedBlockTexture(blockAccess, x, y, z, meta, icons[meta]);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileMachineCasing();
    }

    public static int getHeatFromMeta(int meta) {
        switch (meta) {
            case 0:
                return 1020 / 25;
            case 1:
                return 1700 / 25;
            case 2:
                return 2380 / 25;
        }
        return 0;
    }

    /**
     *
     * This is taken from https://github.com/SlimeKnights/TinkersConstruct/blob/a7405a3d10318bb5c486ec75fb62897a8149d1a6/src/main/java/tconstruct/smeltery/blocks/GlassBlockConnected.java
     *
     * @param par1IBlockAccess
     * @param x
     * @param y
     * @param z
     * @param meta
     * @param icons
     * @return
     */
    public IIcon getConnectedBlockTexture (IBlockAccess par1IBlockAccess, int x, int y, int z, int meta, IIcon[] icons)
    {
        boolean isOpenUp = false, isOpenDown = false, isOpenLeft = false, isOpenRight = false;
        switch (meta)
        {
            case 0:
                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x - 1, y, z), par1IBlockAccess.getBlockMetadata(x - 1, y, z)))
                {
                    isOpenDown = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x + 1, y, z), par1IBlockAccess.getBlockMetadata(x + 1, y, z)))
                {
                    isOpenUp = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x, y, z - 1), par1IBlockAccess.getBlockMetadata(x, y, z - 1)))
                {
                    isOpenLeft = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x, y, z + 1), par1IBlockAccess.getBlockMetadata(x, y, z + 1)))
                {
                    isOpenRight = true;
                }

                if (isOpenUp && isOpenDown && isOpenLeft && isOpenRight)
                {
                    return icons[15];
                }
                else if (isOpenUp && isOpenDown && isOpenLeft)
                {
                    return icons[11];
                }
                else if (isOpenUp && isOpenDown && isOpenRight)
                {
                    return icons[12];
                }
                else if (isOpenUp && isOpenLeft && isOpenRight)
                {
                    return icons[13];
                }
                else if (isOpenDown && isOpenLeft && isOpenRight)
                {
                    return icons[14];
                }
                else if (isOpenDown && isOpenUp)
                {
                    return icons[5];
                }
                else if (isOpenLeft && isOpenRight)
                {
                    return icons[6];
                }
                else if (isOpenDown && isOpenLeft)
                {
                    return icons[8];
                }
                else if (isOpenDown && isOpenRight)
                {
                    return icons[10];
                }
                else if (isOpenUp && isOpenLeft)
                {
                    return icons[7];
                }
                else if (isOpenUp && isOpenRight)
                {
                    return icons[9];
                }
                else if (isOpenDown)
                {
                    return icons[3];
                }
                else if (isOpenUp)
                {
                    return icons[4];
                }
                else if (isOpenLeft)
                {
                    return icons[2];
                }
                else if (isOpenRight)
                {
                    return icons[1];
                }
                break;
            case 1:
                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x - 1, y, z), par1IBlockAccess.getBlockMetadata(x - 1, y, z)))
                {
                    isOpenDown = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x + 1, y, z), par1IBlockAccess.getBlockMetadata(x + 1, y, z)))
                {
                    isOpenUp = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x, y, z - 1), par1IBlockAccess.getBlockMetadata(x, y, z - 1)))
                {
                    isOpenLeft = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x, y, z + 1), par1IBlockAccess.getBlockMetadata(x, y, z + 1)))
                {
                    isOpenRight = true;
                }

                if (isOpenUp && isOpenDown && isOpenLeft && isOpenRight)
                {
                    return icons[15];
                }
                else if (isOpenUp && isOpenDown && isOpenLeft)
                {
                    return icons[11];
                }
                else if (isOpenUp && isOpenDown && isOpenRight)
                {
                    return icons[12];
                }
                else if (isOpenUp && isOpenLeft && isOpenRight)
                {
                    return icons[13];
                }
                else if (isOpenDown && isOpenLeft && isOpenRight)
                {
                    return icons[14];
                }
                else if (isOpenDown && isOpenUp)
                {
                    return icons[5];
                }
                else if (isOpenLeft && isOpenRight)
                {
                    return icons[6];
                }
                else if (isOpenDown && isOpenLeft)
                {
                    return icons[8];
                }
                else if (isOpenDown && isOpenRight)
                {
                    return icons[10];
                }
                else if (isOpenUp && isOpenLeft)
                {
                    return icons[7];
                }
                else if (isOpenUp && isOpenRight)
                {
                    return icons[9];
                }
                else if (isOpenDown)
                {
                    return icons[3];
                }
                else if (isOpenUp)
                {
                    return icons[4];
                }
                else if (isOpenLeft)
                {
                    return icons[2];
                }
                else if (isOpenRight)
                {
                    return icons[1];
                }
                break;
            case 2:
                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x, y - 1, z), par1IBlockAccess.getBlockMetadata(x, y - 1, z)))
                {
                    isOpenDown = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x, y + 1, z), par1IBlockAccess.getBlockMetadata(x, y + 1, z)))
                {
                    isOpenUp = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x - 1, y, z), par1IBlockAccess.getBlockMetadata(x - 1, y, z)))
                {
                    isOpenLeft = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x + 1, y, z), par1IBlockAccess.getBlockMetadata(x + 1, y, z)))
                {
                    isOpenRight = true;
                }

                if (isOpenUp && isOpenDown && isOpenLeft && isOpenRight)
                {
                    return icons[15];
                }
                else if (isOpenUp && isOpenDown && isOpenLeft)
                {
                    return icons[13];
                }
                else if (isOpenUp && isOpenDown && isOpenRight)
                {
                    return icons[14];
                }
                else if (isOpenUp && isOpenLeft && isOpenRight)
                {
                    return icons[11];
                }
                else if (isOpenDown && isOpenLeft && isOpenRight)
                {
                    return icons[12];
                }
                else if (isOpenDown && isOpenUp)
                {
                    return icons[6];
                }
                else if (isOpenLeft && isOpenRight)
                {
                    return icons[5];
                }
                else if (isOpenDown && isOpenLeft)
                {
                    return icons[9];
                }
                else if (isOpenDown && isOpenRight)
                {
                    return icons[10];
                }
                else if (isOpenUp && isOpenLeft)
                {
                    return icons[7];
                }
                else if (isOpenUp && isOpenRight)
                {
                    return icons[8];
                }
                else if (isOpenDown)
                {
                    return icons[1];
                }
                else if (isOpenUp)
                {
                    return icons[2];
                }
                else if (isOpenLeft)
                {
                    return icons[4];
                }
                else if (isOpenRight)
                {
                    return icons[3];
                }
                break;
            case 3:
                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x, y - 1, z), par1IBlockAccess.getBlockMetadata(x, y - 1, z)))
                {
                    isOpenDown = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x, y + 1, z), par1IBlockAccess.getBlockMetadata(x, y + 1, z)))
                {
                    isOpenUp = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x - 1, y, z), par1IBlockAccess.getBlockMetadata(x - 1, y, z)))
                {
                    isOpenLeft = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x + 1, y, z), par1IBlockAccess.getBlockMetadata(x + 1, y, z)))
                {
                    isOpenRight = true;
                }

                if (isOpenUp && isOpenDown && isOpenLeft && isOpenRight)
                {
                    return icons[15];
                }
                else if (isOpenUp && isOpenDown && isOpenLeft)
                {
                    return icons[14];
                }
                else if (isOpenUp && isOpenDown && isOpenRight)
                {
                    return icons[13];
                }
                else if (isOpenUp && isOpenLeft && isOpenRight)
                {
                    return icons[11];
                }
                else if (isOpenDown && isOpenLeft && isOpenRight)
                {
                    return icons[12];
                }
                else if (isOpenDown && isOpenUp)
                {
                    return icons[6];
                }
                else if (isOpenLeft && isOpenRight)
                {
                    return icons[5];
                }
                else if (isOpenDown && isOpenLeft)
                {
                    return icons[10];
                }
                else if (isOpenDown && isOpenRight)
                {
                    return icons[9];
                }
                else if (isOpenUp && isOpenLeft)
                {
                    return icons[8];
                }
                else if (isOpenUp && isOpenRight)
                {
                    return icons[7];
                }
                else if (isOpenDown)
                {
                    return icons[1];
                }
                else if (isOpenUp)
                {
                    return icons[2];
                }
                else if (isOpenLeft)
                {
                    return icons[3];
                }
                else if (isOpenRight)
                {
                    return icons[4];
                }
                break;
            case 4:
                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x, y - 1, z), par1IBlockAccess.getBlockMetadata(x, y - 1, z)))
                {
                    isOpenDown = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x, y + 1, z), par1IBlockAccess.getBlockMetadata(x, y + 1, z)))
                {
                    isOpenUp = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x, y, z - 1), par1IBlockAccess.getBlockMetadata(x, y, z - 1)))
                {
                    isOpenLeft = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x, y, z + 1), par1IBlockAccess.getBlockMetadata(x, y, z + 1)))
                {
                    isOpenRight = true;
                }

                if (isOpenUp && isOpenDown && isOpenLeft && isOpenRight)
                {
                    return icons[15];
                }
                else if (isOpenUp && isOpenDown && isOpenLeft)
                {
                    return icons[14];
                }
                else if (isOpenUp && isOpenDown && isOpenRight)
                {
                    return icons[13];
                }
                else if (isOpenUp && isOpenLeft && isOpenRight)
                {
                    return icons[11];
                }
                else if (isOpenDown && isOpenLeft && isOpenRight)
                {
                    return icons[12];
                }
                else if (isOpenDown && isOpenUp)
                {
                    return icons[6];
                }
                else if (isOpenLeft && isOpenRight)
                {
                    return icons[5];
                }
                else if (isOpenDown && isOpenLeft)
                {
                    return icons[10];
                }
                else if (isOpenDown && isOpenRight)
                {
                    return icons[9];
                }
                else if (isOpenUp && isOpenLeft)
                {
                    return icons[8];
                }
                else if (isOpenUp && isOpenRight)
                {
                    return icons[7];
                }
                else if (isOpenDown)
                {
                    return icons[1];
                }
                else if (isOpenUp)
                {
                    return icons[2];
                }
                else if (isOpenLeft)
                {
                    return icons[3];
                }
                else if (isOpenRight)
                {
                    return icons[4];
                }
                break;
            case 5:
                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x, y - 1, z), par1IBlockAccess.getBlockMetadata(x, y - 1, z)))
                {
                    isOpenDown = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x, y + 1, z), par1IBlockAccess.getBlockMetadata(x, y + 1, z)))
                {
                    isOpenUp = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x, y, z - 1), par1IBlockAccess.getBlockMetadata(x, y, z - 1)))
                {
                    isOpenLeft = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, x, y, z, par1IBlockAccess.getBlock(x, y, z + 1), par1IBlockAccess.getBlockMetadata(x, y, z + 1)))
                {
                    isOpenRight = true;
                }

                if (isOpenUp && isOpenDown && isOpenLeft && isOpenRight)
                {
                    return icons[15];
                }
                else if (isOpenUp && isOpenDown && isOpenLeft)
                {
                    return icons[13];
                }
                else if (isOpenUp && isOpenDown && isOpenRight)
                {
                    return icons[14];
                }
                else if (isOpenUp && isOpenLeft && isOpenRight)
                {
                    return icons[11];
                }
                else if (isOpenDown && isOpenLeft && isOpenRight)
                {
                    return icons[12];
                }
                else if (isOpenDown && isOpenUp)
                {
                    return icons[6];
                }
                else if (isOpenLeft && isOpenRight)
                {
                    return icons[5];
                }
                else if (isOpenDown && isOpenLeft)
                {
                    return icons[9];
                }
                else if (isOpenDown && isOpenRight)
                {
                    return icons[10];
                }
                else if (isOpenUp && isOpenLeft)
                {
                    return icons[7];
                }
                else if (isOpenUp && isOpenRight)
                {
                    return icons[8];
                }
                else if (isOpenDown)
                {
                    return icons[1];
                }
                else if (isOpenUp)
                {
                    return icons[2];
                }
                else if (isOpenLeft)
                {
                    return icons[4];
                }
                else if (isOpenRight)
                {
                    return icons[3];
                }
                break;
        }

        return icons[0];
    }

    public boolean shouldConnectToBlock (IBlockAccess blockAccess, int x, int y, int z, Block block, int meta)
    {
        return block == (Block) this;
    }
}
