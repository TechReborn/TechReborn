package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.Core;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import reborncore.common.blocks.IRotationTexture;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.generator.TileGenerator;

public class BlockGenerator extends BlockMachineBase implements IRotationTexture {

	public BlockGenerator() {
		super();
		setUnlocalizedName("techreborn.generator");
        setCreativeTab(TechRebornCreativeTab.instance);
	}
	
    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileGenerator();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()){
            player.openGui(Core.INSTANCE, GuiHandler.generatorID, world, x, y, z);
        }
        return true;
    }

    private final String prefix = "techreborn:blocks/machine/";

    @Override
    public String getFrontOff() {
        return prefix + "generator_front_off";
    }

    @Override
    public String getFrontOn() {
        return prefix + "generator_front_on";
    }

    @Override
    public String getSide() {
        return prefix + "machine_side" ;
    }

    @Override
    public String getTop() {
        return prefix + "diesel_generator_top_off";
    }

    @Override
    public String getBottom() {
        return prefix + "machine_bottom";
    }
}
