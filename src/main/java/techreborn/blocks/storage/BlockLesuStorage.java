package techreborn.blocks.storage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import techreborn.blocks.BlockMachineBase;
import techreborn.client.texture.ConnectedTexture;
import techreborn.client.texture.LesuConnectedTextureGenerator;
import techreborn.config.ConfigTechReborn;
import techreborn.tiles.lesu.TileLesuStorage;

public class BlockLesuStorage extends BlockMachineBase {

    public IIcon[][] icons;

    public BlockLesuStorage(Material material) {
        super(material);
        setBlockName("techreborn.lesustorage");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[1][16];
            //  up   down  left  right
        if(!ConfigTechReborn.useConnectedTextures){
            for (int j = 0; j < 15; j++) {
                icons[0][j] = iconRegister.registerIcon("techreborn:" + "machine/lesu_block");
            }
            return;
        }

        int i = 0;
            icons[i][0] = genIcon(new ConnectedTexture(true, true, true, true), iconRegister, 0, i);
            icons[i][1] = genIcon(new ConnectedTexture(true, false, true, true), iconRegister, 1, i);
            icons[i][2] = genIcon(new ConnectedTexture(false, true, true, true), iconRegister, 2, i);
            icons[i][3] = genIcon(new ConnectedTexture(true, true, true, false), iconRegister, 3, i);
            icons[i][4] = genIcon(new ConnectedTexture(true, true, false, true), iconRegister, 4, i);
            icons[i][5] = genIcon(new ConnectedTexture(true, true, false, false), iconRegister, 5, i);
            icons[i][6] = genIcon(new ConnectedTexture(false, false, true, true), iconRegister, 6, i);
            icons[i][7] = genIcon(new ConnectedTexture(false, true, false, true), iconRegister, 7, i);
            icons[i][8] = genIcon(new ConnectedTexture(false, true, true, false), iconRegister, 8, i);
            icons[i][9] = genIcon(new ConnectedTexture(true, false, false, true), iconRegister, 9, i);
            icons[i][10] = genIcon(new ConnectedTexture(true, false, true, false), iconRegister, 10, i);
            icons[i][11] = genIcon(new ConnectedTexture(false, true, false, false), iconRegister, 11, i);
            icons[i][12] = genIcon(new ConnectedTexture(true, false, false, false), iconRegister, 12, i);
            icons[i][13] = genIcon(new ConnectedTexture(false, false, false, true), iconRegister, 13, i);
            icons[i][14] = genIcon(new ConnectedTexture(false, false, true, false), iconRegister, 14, i);
            icons[i][15] = genIcon(new ConnectedTexture(false, false, false, false), iconRegister, 15, i);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return getConnectedBlockTexture(blockAccess, x, y, z, side, icons[0]);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return icons[0][0];
    }


    public IIcon genIcon(ConnectedTexture connectedTexture, IIconRegister iconRegister, int texNum, int meta) {
        if (iconRegister instanceof TextureMap) {
            TextureMap map = (TextureMap) iconRegister;
            String name = LesuConnectedTextureGenerator.getDerivedName("lesu." + texNum);
            System.out.println(name);
            TextureAtlasSprite texture = map.getTextureExtry(name);
            if (texture == null) {
                texture = new LesuConnectedTextureGenerator(name, "lesu", connectedTexture);
                map.setTextureEntry(name, texture);
            }
            return map.getTextureExtry(name);
        } else {
            return null;
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemstack) {
        super.onBlockPlacedBy(world, x, y, z, player, itemstack);
        if (world.getTileEntity(x, y, z) instanceof TileLesuStorage) {
            ((TileLesuStorage) world.getTileEntity(x, y, z)).rebuildNetwork();
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (world.getTileEntity(x, y, z) instanceof TileLesuStorage) {
            ((TileLesuStorage) world.getTileEntity(x, y, z)).removeFromNetwork();
        }
        super.breakBlock(world, x, y, z, block, meta);
    }


    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileLesuStorage();
    }


    /**
     * This is taken from https://github.com/SlimeKnights/TinkersConstruct/blob/a7405a3d10318bb5c486ec75fb62897a8149d1a6/src/main/java/tconstruct/smeltery/blocks/GlassBlockConnected.java
     */
    public IIcon getConnectedBlockTexture (IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5, IIcon[] icons)
    {
        boolean isOpenUp = false, isOpenDown = false, isOpenLeft = false, isOpenRight = false;

        switch (par5)
        {
            case 0:
                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2 - 1, par3, par4), par1IBlockAccess.getBlockMetadata(par2 - 1, par3, par4)))
                {
                    isOpenDown = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2 + 1, par3, par4), par1IBlockAccess.getBlockMetadata(par2 + 1, par3, par4)))
                {
                    isOpenUp = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2, par3, par4 - 1), par1IBlockAccess.getBlockMetadata(par2, par3, par4 - 1)))
                {
                    isOpenLeft = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2, par3, par4 + 1), par1IBlockAccess.getBlockMetadata(par2, par3, par4 + 1)))
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
                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2 - 1, par3, par4), par1IBlockAccess.getBlockMetadata(par2 - 1, par3, par4)))
                {
                    isOpenDown = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2 + 1, par3, par4), par1IBlockAccess.getBlockMetadata(par2 + 1, par3, par4)))
                {
                    isOpenUp = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2, par3, par4 - 1), par1IBlockAccess.getBlockMetadata(par2, par3, par4 - 1)))
                {
                    isOpenLeft = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2, par3, par4 + 1), par1IBlockAccess.getBlockMetadata(par2, par3, par4 + 1)))
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
                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2, par3 - 1, par4), par1IBlockAccess.getBlockMetadata(par2, par3 - 1, par4)))
                {
                    isOpenDown = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2, par3 + 1, par4), par1IBlockAccess.getBlockMetadata(par2, par3 + 1, par4)))
                {
                    isOpenUp = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2 - 1, par3, par4), par1IBlockAccess.getBlockMetadata(par2 - 1, par3, par4)))
                {
                    isOpenLeft = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2 + 1, par3, par4), par1IBlockAccess.getBlockMetadata(par2 + 1, par3, par4)))
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
                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2, par3 - 1, par4), par1IBlockAccess.getBlockMetadata(par2, par3 - 1, par4)))
                {
                    isOpenDown = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2, par3 + 1, par4), par1IBlockAccess.getBlockMetadata(par2, par3 + 1, par4)))
                {
                    isOpenUp = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2 - 1, par3, par4), par1IBlockAccess.getBlockMetadata(par2 - 1, par3, par4)))
                {
                    isOpenLeft = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2 + 1, par3, par4), par1IBlockAccess.getBlockMetadata(par2 + 1, par3, par4)))
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
                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2, par3 - 1, par4), par1IBlockAccess.getBlockMetadata(par2, par3 - 1, par4)))
                {
                    isOpenDown = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2, par3 + 1, par4), par1IBlockAccess.getBlockMetadata(par2, par3 + 1, par4)))
                {
                    isOpenUp = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2, par3, par4 - 1), par1IBlockAccess.getBlockMetadata(par2, par3, par4 - 1)))
                {
                    isOpenLeft = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2, par3, par4 + 1), par1IBlockAccess.getBlockMetadata(par2, par3, par4 + 1)))
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
                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2, par3 - 1, par4), par1IBlockAccess.getBlockMetadata(par2, par3 - 1, par4)))
                {
                    isOpenDown = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2, par3 + 1, par4), par1IBlockAccess.getBlockMetadata(par2, par3 + 1, par4)))
                {
                    isOpenUp = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2, par3, par4 - 1), par1IBlockAccess.getBlockMetadata(par2, par3, par4 - 1)))
                {
                    isOpenLeft = true;
                }

                if (shouldConnectToBlock(par1IBlockAccess, par2, par3, par4, par1IBlockAccess.getBlock(par2, par3, par4 + 1), par1IBlockAccess.getBlockMetadata(par2, par3, par4 + 1)))
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

    public boolean shouldConnectToBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int meta) {
        return block == (Block) this;
    }
}
