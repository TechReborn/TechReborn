package techreborn.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileDigitalChest;

public class BlockDigitalChest extends BlockMachineBase implements IAdvancedRotationTexture {

    public BlockDigitalChest() {
        super();
        setUnlocalizedName("techreborn.digitalChest");
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileDigitalChest();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.digitalChestID, world, x,
                    y, z);
        return true;
    }

    private final String prefix = "techreborn:blocks/machine/";

    @Override
    public String getFront(boolean isActive) {
        return prefix + "quantum_chest";
    }

    @Override
    public String getSide(boolean isActive) {
        return prefix + "qchest_side" ;
    }

    @Override
    public String getTop(boolean isActive) {
        return prefix + "quantum_top";
    }

    @Override
    public String getBottom(boolean isActive) {
        return prefix + "machine_bottom";
    }

}
