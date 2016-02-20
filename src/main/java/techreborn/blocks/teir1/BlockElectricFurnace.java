package techreborn.blocks.teir1;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.blocks.BlockMachineBase;
import techreborn.blocks.IRotationTexture;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileIndustrialGrinder;
import techreborn.tiles.teir1.TileElectricFurnace;
import techreborn.tiles.teir1.TileGrinder;

public class BlockElectricFurnace extends BlockMachineBase implements IRotationTexture{

	public BlockElectricFurnace(Material material) {
        super(material);
        setUnlocalizedName("techreborn.electricfurnace");
	}
	
    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileElectricFurnace();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()){
            player.openGui(Core.INSTANCE, GuiHandler.electricFurnaceID, world, x, y, z);
        }
        return true;
    }

    private final String prefix = "techreborn:blocks/machine/";

    @Override
    public String getFrontOff() {
        return prefix + "machine_side";
    }

    @Override
    public String getFrontOn() {
        return prefix + "machine_side";
    }

    @Override
    public String getSide() {
        return prefix + "machine_side";
    }

    @Override
    public String getTop() {
        return prefix + "machine_side";
    }

    @Override
    public String getBottom() {
        return prefix + "machine_side";
    }
}
