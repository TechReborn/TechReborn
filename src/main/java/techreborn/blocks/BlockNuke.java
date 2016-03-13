package techreborn.blocks;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.entitys.EntityNukePrimed;

/**
 * Created by Mark on 13/03/2016.
 */
public class BlockNuke extends BlockTNT implements ITexturedBlock {

    public BlockNuke() {
        setUnlocalizedName("techreborn.nuke");
        setCreativeTab(TechRebornCreativeTabMisc.instance);
        RebornCore.jsonDestroyer.registerObject(this);
    }

    @Override
    public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter) {
        if (!worldIn.isRemote) {
            if (state.getValue(EXPLODE).booleanValue()) {
                EntityNukePrimed entitynukeprimed = new EntityNukePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);
                worldIn.spawnEntityInWorld(entitynukeprimed);
                worldIn.playSoundAtEntity(entitynukeprimed, "game.tnt.primed", 1.0F, 1.0F);
            }
        }
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isRemote) {
            EntityNukePrimed entitynukeprimed = new EntityNukePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), explosionIn.getExplosivePlacedBy());
            entitynukeprimed.fuse = worldIn.rand.nextInt(entitynukeprimed.fuse / 4) + entitynukeprimed.fuse / 8;
            worldIn.spawnEntityInWorld(entitynukeprimed);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false; //No flint and steel
    }

    @Override
    public String getTextureNameFromState(IBlockState iBlockState, EnumFacing enumFacing) {
        return "techreborn:blocks/machine/machine_bottom";
    }

    @Override
    public int amountOfStates() {
        return 1;
    }
}
