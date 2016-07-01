package techreborn.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import reborncore.common.util.ItemNBTHelper;
import reborncore.shields.RebornCoreShields;
import techreborn.client.TechRebornCreativeTab;

/**
 * Created by mark on 14/05/16.
 */
public class BlockDistributor extends Block {

    AxisAlignedBB AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.0625D, 0.9375D);

    public BlockDistributor( ) {
        super(Material.IRON);
        setHardness(2f);
        setUnlocalizedName("techreborn.distributor");
    }

    public static ItemStack getReleaseStack(){
        ItemStack newStack = new ItemStack(Items.SHIELD);
        ItemNBTHelper.setString(newStack, "type", "btm");
        ItemNBTHelper.setBoolean(newStack, "vanilla", false);
        return newStack;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if(!entityIn.worldObj.isRemote){
            if(entityIn instanceof EntityPlayer){
                EntityPlayer player = (EntityPlayer) entityIn;
                if(player.getHeldItemOffhand() != null && player.getHeldItemOffhand().getItem() == Items.SHIELD){
                    if(ItemNBTHelper.getString(player.getHeldItemOffhand(), "type", "null").equals("btm")){
                        return;
                    }
                }
                player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, getReleaseStack());
            }
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }
}
