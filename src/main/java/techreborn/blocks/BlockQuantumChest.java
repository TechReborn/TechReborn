package techreborn.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileQuantumChest;

public class BlockQuantumChest extends BlockContainer {

    public BlockQuantumChest() {
        super(Material.rock);
        setUnlocalizedName("techreborn.quantumChest");
        setCreativeTab(TechRebornCreativeTab.instance);
        setHardness(2.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileQuantumChest();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!playerIn.isSneaking())
            playerIn.openGui(Core.INSTANCE, GuiHandler.quantumChestID, worldIn, pos.getX(),
                    pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        this.setDefaultDirection(worldIn, pos);
    }

    private void setDefaultDirection(World world, BlockPos pos) {

        if (!world.isRemote) {
            Block block1 = world.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1)).getBlock();
            Block block2 = world.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1)).getBlock();
            Block block3 = world.getBlockState(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ())).getBlock();
            Block block4 = world.getBlockState(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ())).getBlock();

            byte b = 3;

            if (block1.isOpaqueCube() && !block2.isOpaqueCube()) {
                b = 3;
            }
            if (block2.isOpaqueCube() && !block1.isOpaqueCube()) {
                b = 2;
            }
            if (block3.isOpaqueCube() && !block4.isOpaqueCube()) {
                b = 5;
            }
            if (block4.isOpaqueCube() && !block3.isOpaqueCube()) {
                b = 4;
            }

            //TODO 1.8 meta
            //  world.setBlockMetadataWithNotify(x, y, z, b, 2);
           // setTileRotation(world, pos, b);

        }

    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        //TODO fix rotaion

//        int l = MathHelper
//                .floor_double((double) (placer.rotationYaw * 4.0F / 360F) + 0.5D) & 3;
//
//        if (l == 0) {
//            setTileRotation(worldIn, pos, 2);
//        }
//        if (l == 1) {
//            setTileRotation(worldIn, pos, 5);
//        }
//        if (l == 2) {
//            setTileRotation(worldIn, pos, 3);
//        }
//        if (l == 3) {
//            setTileRotation(worldIn, pos, 4);
//        }
    }
}
