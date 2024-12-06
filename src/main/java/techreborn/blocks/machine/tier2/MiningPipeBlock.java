/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2024 TechReborn
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

package techreborn.blocks.machine.tier2;

import net.minecraft.block.*;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import techreborn.init.TRBlockSettings;


public class MiningPipeBlock extends Block implements Waterloggable {

	public static final EnumProperty<MiningPipeType> TYPE = EnumProperty.of("pipe_type", MiningPipeType.class);

	public MiningPipeBlock(){
		super(TRBlockSettings.miningPipe());
		setDefaultState(getStateManager().getDefaultState().with(TYPE, MiningPipeType.DRILL));
	}

	@Override
	protected VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
		return VoxelShapes.empty();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(TYPE);
	}

	public static MiningPipeType getType(BlockState state) {
		return state.get(TYPE);
	}

	public static void setType(MiningPipeType type, World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).with(TYPE, type));
	}

	public enum MiningPipeType implements StringIdentifiable {
		PIPE,
		DRILL;

		@Override
		public String asString() {
			switch (this) {
				case PIPE -> {
					return "pipe";
				}
				case DRILL -> {
					return "drill";
				}
			}
			return null;
		}
	}

}
