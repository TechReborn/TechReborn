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

package techreborn.component;

import com.mojang.serialization.codecs.PrimitiveCodec;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import techreborn.TechReborn;

public class TRDataComponentTypes {
	private static final StreamCodec<RegistryFriendlyByteBuf, BlockState> BLOCK_STATE_PACKET_CODEC = StreamCodec.ofMember((value, buf) -> buf.writeNbt(NbtUtils.writeBlockState(value)), buf -> NbtUtils.readBlockState(BuiltInRegistries.BLOCK, buf.readNbt()));

	public static final DataComponentType<Boolean> IS_ACTIVE =
		DataComponentType.<Boolean>builder().persistent(PrimitiveCodec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build();

	public static final DataComponentType<Boolean> AOE5 =
		DataComponentType.<Boolean>builder().persistent(PrimitiveCodec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build();

	public static final DataComponentType<GlobalPos> FREQUENCY_TRANSMITTER =
		DataComponentType.<GlobalPos>builder().persistent(GlobalPos.CODEC).networkSynchronized(GlobalPos.STREAM_CODEC).build();

	public static final DataComponentType<BlockState> PAINTING_COVER =
		DataComponentType.<BlockState>builder().persistent(BlockState.CODEC).networkSynchronized(BLOCK_STATE_PACKET_CODEC).build();

	public static final DataComponentType<Holder<Fluid>> FLUID =
		DataComponentType.<Holder<Fluid>>builder().persistent(BuiltInRegistries.FLUID.holderByNameCodec()).networkSynchronized(ByteBufCodecs.holderRegistry(Registries.FLUID)).build();

	public static void init(){
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(TechReborn.MOD_ID, "is_active"), IS_ACTIVE);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(TechReborn.MOD_ID, "aoe5"), AOE5);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(TechReborn.MOD_ID, "frequency_transmitter"), FREQUENCY_TRANSMITTER);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(TechReborn.MOD_ID, "painting_cover"), PAINTING_COVER);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(TechReborn.MOD_ID, "fluid"), FLUID);
	}
}
