/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

//package techreborn.compat.compat.psi.spell.trick.block;
//
//import java.util.Random;
//
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.entity.item.EntityItem;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.math.BlockPos;
//import techreborn.blocks.BlockRubberLog;
//import techreborn.items.ItemParts;
//import vazkii.psi.api.internal.Vector3;
//import vazkii.psi.api.spell.EnumSpellStat;
//import vazkii.psi.api.spell.Spell;
//import vazkii.psi.api.spell.SpellCompilationException;
//import vazkii.psi.api.spell.SpellContext;
//import vazkii.psi.api.spell.SpellMetadata;
//import vazkii.psi.api.spell.SpellParam;
//import vazkii.psi.api.spell.SpellRuntimeException;
//import vazkii.psi.api.spell.param.ParamVector;
//import vazkii.psi.api.spell.piece.PieceTrick;
//
//public class PieceTrickBlockTap extends PieceTrick {
//
//	SpellParam position;
//	SpellParam side;
//
//	public PieceTrickBlockTap(Spell spell) {
//		super(spell);
//	}
//
//	@Override
//	public void initParams() {
//		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
//		addParam(side = new ParamVector("Side", SpellParam.GREEN, false, false));
//	}
//
//	@Override
//	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
//		super.addToMetadata(meta);
//
//		meta.addStat(EnumSpellStat.POTENCY, 20);
//		meta.addStat(EnumSpellStat.COST, 25);
//	}
//
//	@Override
//	public Object execute(SpellContext context) throws SpellRuntimeException {
//		if (context.caster.worldObj.isRemote)
//			return null;
//
//		Vector3 positionVal = this.<Vector3> getParamValue(context, position);
//		Vector3 sideVal = this.<Vector3> getParamValue(context, side);
//
//		if (positionVal == null)
//			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
//		if (!context.isInRadius(positionVal))
//			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
//
//		if (sideVal == null)
//			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
//		if (!context.isInRadius(sideVal))
//			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
//
//		if(!sideVal.isAxial()){
//			return null;
//		}
//
//		BlockPos pos = new BlockPos(positionVal.x, positionVal.y, positionVal.z);
//		IBlockState state = context.caster.worldObj.getBlockState(pos);
//		if (state.getBlock() instanceof BlockRubberLog) {
//			System.out.println("Is rubber log");
//			if (state.getValue(BlockRubberLog.HAS_SAP)) {
//				System.out.println("Has sap");
//				if (state.getValue(BlockRubberLog.SAP_SIDE) == null) {
//					System.out.println("got this far");
//					context.caster.worldObj.setBlockState(pos, state.withProperty(BlockRubberLog.HAS_SAP, false)
//							.withProperty(BlockRubberLog.SAP_SIDE, EnumFacing.getHorizontal(0)));
//					// TODO 1.9 sounds
//					// worldIn.playSoundAtEntity(playerIn,
//					// "techreborn:sap_extract", 0.8F, 1F);
//					if (!context.caster.worldObj.isRemote) {
//						System.out.println("doing stuff");
//						Random rand = new Random();
//						BlockPos itemPos = pos.offset(context.positionBroken.sideHit);
//						EntityItem item = new EntityItem(context.caster.worldObj, itemPos.getX(), itemPos.getY(),
//								itemPos.getZ(), ItemParts.getPartByName("rubberSap").copy());
//						float factor = 0.05F;
//						item.motionX = rand.nextGaussian() * factor;
//						item.motionY = rand.nextGaussian() * factor + 0.2F;
//						item.motionZ = rand.nextGaussian() * factor;
//						context.caster.worldObj.spawnEntityInWorld(item);
//					}
//				} else {
//					return null;
//				}
//			} else {
//				return null;
//			}
//		}
//		return null;
//
//	}
//}
