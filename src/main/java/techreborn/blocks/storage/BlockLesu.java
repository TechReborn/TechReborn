package techreborn.blocks.storage;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.blocks.BlockMachineBase;
import techreborn.blocks.IAdvancedRotationTexture;
import techreborn.client.GuiHandler;
import techreborn.tiles.lesu.TileLesu;

public class BlockLesu extends BlockMachineBase implements IAdvancedRotationTexture {


    public BlockLesu(Material material) {
        super(material);
        setUnlocalizedName("techreborn.lesu");
    }


    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileLesu();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.lesuID, world, x, y,
                    z);
        return true;
    }

    private final String prefix = "techreborn:blocks/machine/";


    @Override
    public String getFront(boolean isActive) {
        return prefix + "lesu_front";
    }

    @Override
    public String getSide(boolean isActive) {
        return prefix + "lesu_side" ;
    }

    @Override
    public String getTop(boolean isActive) {
        return prefix + "lesu_side";
    }

    @Override
    public String getBottom(boolean isActive) {
        return prefix + "lesu_side";
    }

}
