package techreborn.blocks.machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileChargeBench;

public class BlockChargeBench extends BlockMachineBase implements IRotationTexture {

    public BlockChargeBench(Material material) {
        super();
        setUnlocalizedName("techreborn.chargebench");
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileChargeBench();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.chargeBench, world, x, y, z);
        return true;
    }

    private final String prefix = "techreborn:blocks/machine/";

    @Override
    public String getFrontOff() {
        return prefix + "chargeBench_side";
    }

    @Override
    public String getFrontOn() {
        return prefix + "chargeBench_side";
    }

    @Override
    public String getSide() {
        return prefix + "chargeBench_side";
    }

    @Override
    public String getTop() {
        return prefix + "chargeBench_side";
    }

    @Override
    public String getBottom() {
        return prefix + "chargeBench_side";
    }
}
