package techreborn.blocks.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.IC2Items;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.blocks.BlockMachineBase;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileMatterFabricator;

import java.util.Random;

public class BlockMatterFabricator extends BlockMachineBase {

    @SideOnly(Side.CLIENT)
    private IIcon iconOff;

    @SideOnly(Side.CLIENT)
    private IIcon iconOn;

    public BlockMatterFabricator(Material material) {
        super(material);
        setBlockName("techreborn.matterfabricator");
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileMatterFabricator();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.matterfabID, world, x, y,
                    z);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
        int metadata = getTileMeta(blockAccess, x, y, z);
        if (side == metadata && blockAccess.getBlockMetadata(x, y, z) == 1) {
            return this.iconOn;
        } else {
            return this.iconOff;
        }
    }
    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        return IC2Items.getItem("advancedMachine").getItem();
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return iconOff;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister icon) {
        this.iconOn = icon.registerIcon("techreborn:machine/matter_fabricator_off");
        this.iconOff = icon.registerIcon("techreborn:machine/matter_fabricator_on");
    }

}
