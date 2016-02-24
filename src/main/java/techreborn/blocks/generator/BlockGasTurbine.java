package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.generator.TileGasTurbine;

public class BlockGasTurbine extends BlockMachineBase implements IAdvancedRotationTexture {


    public BlockGasTurbine(Material material) {
        super();
        setUnlocalizedName("techreborn.gasTurbine");
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileGasTurbine();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(fillBlockWithFluid(world, new BlockPos(x, y, z), player)){
            return true;
        }
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.gasTurbineID, world, x, y,
                    z);
        return true;
    }

    private final String prefix = "techreborn:blocks/machine/";

    @Override
    public String getFront(boolean isActive) {
        return prefix + "machine_side";
    }

    @Override
    public String getSide(boolean isActive) {
        return prefix + "machine_side" ;
    }

    @Override
    public String getTop(boolean isActive) {
        return prefix + "gas_generator_top";
    }

    @Override
    public String getBottom(boolean isActive) {
        return prefix + "gas_generator_bottom";
    }

}
