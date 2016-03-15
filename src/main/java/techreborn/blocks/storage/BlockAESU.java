package techreborn.blocks.storage;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileAesu;

public class BlockAESU extends BlockMachineBase implements IRotationTexture {

    public BlockAESU(Material material) {
        super();
        setUnlocalizedName("techreborn.aesu");
        setCreativeTab(TechRebornCreativeTab.instance);
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

    private final String prefix = "techreborn:blocks/machine/storage/";

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
        return prefix + "aesu_top";
    }

    @Override
    public String getBottom() {
        return prefix + "aesu_bottom";
    }

}
