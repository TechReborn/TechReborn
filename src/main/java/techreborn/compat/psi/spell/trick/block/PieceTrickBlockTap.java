package techreborn.compat.psi.spell.trick.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import techreborn.blocks.BlockRubberLog;
import techreborn.items.ItemParts;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.ConfigHandler;

public class PieceTrickBlockTap extends PieceTrick {

	SpellParam position;

	public PieceTrickBlockTap(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);

		meta.addStat(EnumSpellStat.POTENCY, 20);
		meta.addStat(EnumSpellStat.COST, 25);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		if(context.caster.worldObj.isRemote)
			return null;

		Vector3 positionVal = this.<Vector3>getParamValue(context, position);

		if(positionVal == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		if(!context.isInRadius(positionVal))
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

		BlockPos pos = new BlockPos(positionVal.x, positionVal.y, positionVal.z);
		IBlockState state = context.caster.worldObj.getBlockState(pos);
		if(state instanceof BlockRubberLog){
		if (state.getValue(BlockRubberLog.HAS_SAP))
		{
			if (state.getValue(BlockRubberLog.SAP_SIDE) == context.positionBroken.sideHit)
			{
				context.caster.worldObj.setBlockState(pos,
						state.withProperty(BlockRubberLog.HAS_SAP, false).withProperty(BlockRubberLog.SAP_SIDE, EnumFacing.getHorizontal(0)));
				// TODO 1.9 sounds
				// worldIn.playSoundAtEntity(playerIn,
				// "techreborn:sap_extract", 0.8F, 1F);
				if (!context.caster.worldObj.isRemote)
				{
					Random rand = new Random();
					BlockPos itemPos = pos.offset(context.positionBroken.sideHit);
					EntityItem item = new EntityItem(context.caster.worldObj, itemPos.getX(), itemPos.getY(), itemPos.getZ(),
							ItemParts.getPartByName("rubberSap").copy());
					float factor = 0.05F;
					item.motionX = rand.nextGaussian() * factor;
					item.motionY = rand.nextGaussian() * factor + 0.2F;
					item.motionZ = rand.nextGaussian() * factor;
					context.caster.worldObj.spawnEntityInWorld(item);
				}
				return true;
			}
		}
		}
		removeBlockWithDrops(context, context.caster, context.caster.worldObj, context.tool, pos, true);

		return null;
	}

	public static void removeBlockWithDrops(SpellContext context, EntityPlayer player, World world, ItemStack tool,
			BlockPos pos, boolean particles) {
		if (!world.isBlockLoaded(pos)
				|| context.positionBroken != null && pos.equals(context.positionBroken.getBlockPos()))
			return;

		int harvestLevel = ConfigHandler.cadHarvestLevel;
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (!world.isRemote && block != null && !block.isAir(state, world, pos) && !(block instanceof BlockLiquid)
				&& block.getPlayerRelativeBlockHardness(state, player, world, pos) > 0) {
			int neededHarvestLevel = block.getHarvestLevel(state);
			if (neededHarvestLevel > harvestLevel && (tool != null && !tool.canHarvestBlock(state)))
				return;

			BreakEvent event = new BreakEvent(world, pos, state, player);
			MinecraftForge.EVENT_BUS.post(event);
			if (!event.isCanceled()) {
				if (!player.capabilities.isCreativeMode) {
					block.onBlockHarvested(world, pos, state, player);

					if (block.removedByPlayer(state, world, pos, player, true)) {
						block.onBlockDestroyedByPlayer(world, pos, state);
						block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), tool);
					}
				} else
					world.setBlockToAir(pos);
			}

			if (particles)
				world.playAuxSFX(2001, pos, Block.getStateId(state));
		}
	}
}
