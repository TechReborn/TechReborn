package techreborn.blocks.teir1;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.teir1.TileGrinder;

public class BlockGrinder extends BlockMachineBase implements IRotationTexture{

	public BlockGrinder(Material material) {
        super();
        setUnlocalizedName("techreborn.grinder");
        setCreativeTab(TechRebornCreativeTab.instance);
	}
	
    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileGrinder();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()){
            player.openGui(Core.INSTANCE, GuiHandler.grinderID, world, x, y, z);
        }
        return true;
    }

    private final String prefix = "techreborn:blocks/machine/";

    @Override
    public String getFrontOff() {
        return prefix + "industrial_grinder_front_off";
    }

    @Override
    public String getFrontOn() {
        return prefix + "industrial_grinder_front_on";
    }

    @Override
    public String getSide() {
        return prefix + "machine_side";
    }

    @Override
    public String getTop() {
        return prefix + "industrial_grinder_top_off";
    }

    @Override
    public String getBottom() {
        return prefix + "industrial_centrifuge_bottom";
    }
}
