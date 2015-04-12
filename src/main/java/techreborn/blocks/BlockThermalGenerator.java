package techreborn.blocks;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileThermalGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class BlockThermalGenerator extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private IIcon top;
    @SideOnly(Side.CLIENT)
    private IIcon other;

    public BlockThermalGenerator() {
        super(Material.piston);
        setHardness(2f);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister icon) {
        top = icon.registerIcon("techreborn:ThermalGenerator_top");
        other = icon.registerIcon("techreborn:ThermalGenerator_other");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int currentSide, int meta) {
        if (currentSide == 1) {
            return top;
        } else {
            return other;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileThermalGenerator();
    }


    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.thermalGeneratorID, world, x, y, z);
        return true;
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        //TODO change when added crafting
        return Item.getItemFromBlock(Blocks.furnace);
    }
}
