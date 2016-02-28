package techreborn.items.tools;

import ic2.api.tile.IWrenchable;
import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.items.ItemTR;
import techreborn.items.ItemTextureBase;

import java.util.Random;

/**
 * Created by mark on 26/02/2016.
 */
public class ItemWrench extends ItemTR implements ITexturedItem {

    public ItemWrench() {
        setCreativeTab(TechRebornCreativeTabMisc.instance);
        setUnlocalizedName("techreborn.wrench");
        
    }


    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(world.isAirBlock(pos) || !player.isSneaking()){
            return false;
        }
        TileEntity tile = world.getTileEntity(pos);
        if(tile == null){
            return false;
        }
        if(tile instanceof IWrenchable){
            if(((IWrenchable) tile).wrenchCanRemove(player)){
                ItemStack itemStack = ((IWrenchable) tile).getWrenchDrop(player);
                if(itemStack == null){
                    return false;
                }
                Random rand = new Random();

                float dX = rand.nextFloat() * 0.8F + 0.1F;
                float dY = rand.nextFloat() * 0.8F + 0.1F;
                float dZ = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world, pos.getX() + dX, pos.getY() + dY, pos.getZ() + dZ, itemStack.copy());

                if (itemStack.hasTagCompound()) {
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                }

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;


                world.playSoundAtEntity(player, "techreborn:block_dismantle", 0.8F, 1F);
                if(!world.isRemote){
                    world.spawnEntityInWorld(entityItem);
                    world.setBlockState(pos, Blocks.air.getDefaultState(), 2);
                }

                return true;
            }
        }
        return false;
    }

    @Override
    public String getTextureName(int damage) {
        return "techreborn:items/tool/wrench";
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }
}
