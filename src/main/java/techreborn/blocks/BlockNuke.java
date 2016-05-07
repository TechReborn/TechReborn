package techreborn.blocks;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import reborncore.RebornCore;
import reborncore.common.BaseBlock;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.entitys.EntityNukePrimed;

/**
 * Created by Mark on 13/03/2016.
 */
public class BlockNuke extends BaseBlock implements ITexturedBlock
{
	public static final PropertyBool OVERLAY = PropertyBool.create("overlay");

	public BlockNuke()
	{
		super(Material.TNT);
		setUnlocalizedName("techreborn.nuke");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		RebornCore.jsonDestroyer.registerObject(this);
		this.setDefaultState(this.blockState.getBaseState().withProperty(OVERLAY, false));
	}

	public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter)
	{
		if (!worldIn.isRemote)
		{
			EntityNukePrimed entitynukeprimed = new EntityNukePrimed(worldIn, (double) ((float) pos.getX() + 0.5F),
					(double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);
			worldIn.spawnEntityInWorld(entitynukeprimed);
			// worldIn.playSoundAtEntity(entitynukeprimed, "game.tnt.primed",
			// 1.0F, 1.0F);
		}
	}

	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn)
	{
		if (!worldIn.isRemote)
		{
			EntityNukePrimed entitynukeprimed = new EntityNukePrimed(worldIn, (double) ((float) pos.getX() + 0.5F),
					(double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), explosionIn.getExplosivePlacedBy());
			entitynukeprimed.fuse = worldIn.rand.nextInt(entitynukeprimed.fuse / 4) + entitynukeprimed.fuse / 8;
			worldIn.spawnEntityInWorld(entitynukeprimed);
		}
	}

	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if (!worldIn.isRemote && entityIn instanceof EntityArrow)
		{
			EntityArrow entityarrow = (EntityArrow) entityIn;

			if (entityarrow.isBurning())
			{
				this.explode(worldIn, pos, state, entityarrow.shootingEntity instanceof EntityLivingBase
						? (EntityLivingBase) entityarrow.shootingEntity : null);
				worldIn.setBlockToAir(pos);
			}
		}
	}

	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(worldIn, pos, state);
		if (worldIn.isBlockPowered(pos))
		{
			this.explode(worldIn, pos, state, null);
			worldIn.setBlockToAir(pos);
		}
	}

	/**
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		if (worldIn.isBlockPowered(pos))
		{
			this.explode(worldIn, pos, state, null);
			worldIn.setBlockToAir(pos);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		return false;
	}

	@Override
	public String getTextureNameFromState(IBlockState iBlockState, EnumFacing enumFacing)
	{
		if (iBlockState.getValue(OVERLAY))
		{
			return "techreborn:blocks/nuke_overlay";
		}
		return "techreborn:blocks/nuke";
	}

	@Override
	public int amountOfStates()
	{
		return 2;
	}

	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(OVERLAY) ? 1 : 0;
	}

	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(OVERLAY, (meta & 1) > 0);
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, OVERLAY);
	}

}
