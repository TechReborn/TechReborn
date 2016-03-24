package techreborn.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileQuantumTank;

public class BlockQuantumTank extends BlockMachineBase implements IAdvancedRotationTexture {

    public BlockQuantumTank() {
        super();
        setUnlocalizedName("techreborn.quantumTank");
        setHardness(2.0F);
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileQuantumTank();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(fillBlockWithFluid(world, new BlockPos(x, y, z), player)) {
            return true;
        }
        if (!player.isSneaking()) {
            player.openGui(Core.INSTANCE, GuiHandler.quantumTankID, world, x, y, z);
        }
        return true;
    }

    private final String prefix = "techreborn:blocks/machine/greg_machines/";

    @Override
    public String getFront(boolean isActive) {
        return "techreborn:blocks/machine/generators/thermal_generator_side_off";
    }

    @Override
    public String getSide(boolean isActive) {
        return "techreborn:blocks/machine/generators/thermal_generator_side_off";
    }

    @Override
    public String getTop(boolean isActive) {
        return prefix + "quantum_top";
    }

    @Override
    public String getBottom(boolean isActive) {
        return "techreborn:blocks/machine/generators/thermal_generator_bottom";
    }
}
