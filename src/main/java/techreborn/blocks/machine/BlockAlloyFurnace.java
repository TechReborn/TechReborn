package techreborn.blocks.machine;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileAlloyFurnace;

import java.util.ArrayList;
import java.util.List;

public class BlockAlloyFurnace extends BlockMachineBase implements IRotationTexture {

    public BlockAlloyFurnace(Material material) {
        super();
        setUnlocalizedName("techreborn.alloyfurnace");
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileAlloyFurnace();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.alloyFurnaceID, world, x, y, z);
        return true;
    }
    
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> items = new ArrayList<ItemStack>();
        items.add(new ItemStack(this));
        return items;
    }

    private final String prefix = "techreborn:blocks/machine/";

    @Override
    public String getFrontOff() {
        return prefix + "alloy_furnace_front_off";
    }

    @Override
    public String getFrontOn() {
        return prefix + "alloy_furnace_front_on";
    }

    @Override
    public String getSide() {
        return prefix + "alloy_furnace_side";
    }

    @Override
    public String getTop() {
        return prefix + "alloy_furnace_top";
    }

    @Override
    public String getBottom() {
        return prefix + "machine_bottom";
    }
}
