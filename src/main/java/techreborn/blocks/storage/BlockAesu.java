package techreborn.blocks.storage;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.blocks.BlockMachineBase;
import techreborn.blocks.IRotationTexture;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileAesu;

public class BlockAesu extends BlockMachineBase implements IRotationTexture {



    public BlockAesu(Material material) {
        super(material);
        setUnlocalizedName("techreborn.aesu");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileAesu();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.aesuID, world, x, y,
                    z);
        return true;
    }

    private final String prefix = "techreborn:/blocks/machine/";

    @Override
    public String getFrontOff() {
        return prefix + "aesu_front";
    }

    @Override
    public String getFrontOn() {
        return prefix + "aesu_front";
    }

    @Override
    public String getSide() {
        return prefix + "aesu_side";
    }

    @Override
    public String getTop() {
        return prefix + "aesu_side";
    }

    @Override
    public String getBottom() {
        return prefix + "aesu_side";
    }

}
