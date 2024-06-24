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
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentType;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.GlobalPos;
import techreborn.TechReborn;

public class TRDataComponentTypes {
	private static final PacketCodec<RegistryByteBuf, BlockState> BLOCK_STATE_PACKET_CODEC = PacketCodec.of((value, buf) -> buf.writeNbt(NbtHelper.fromBlockState(value)), buf -> NbtHelper.toBlockState(Registries.BLOCK.getReadOnlyWrapper(), buf.readNbt()));

	public static final ComponentType<Boolean> IS_ACTIVE =
		ComponentType.<Boolean>builder().codec(PrimitiveCodec.BOOL).packetCodec(PacketCodecs.BOOL).build();

	public static final ComponentType<Boolean> AOE5 =
		ComponentType.<Boolean>builder().codec(PrimitiveCodec.BOOL).packetCodec(PacketCodecs.BOOL).build();

	public static final ComponentType<GlobalPos> FREQUENCY_TRANSMITTER =
		ComponentType.<GlobalPos>builder().codec(GlobalPos.CODEC).packetCodec(GlobalPos.PACKET_CODEC).build();

	public static final ComponentType<BlockState> PAINTING_COVER =
		ComponentType.<BlockState>builder().codec(BlockState.CODEC).packetCodec(BLOCK_STATE_PACKET_CODEC).build();

	public static final ComponentType<RegistryEntry<Fluid>> FLUID =
		ComponentType.<RegistryEntry<Fluid>>builder().codec(Registries.FLUID.getEntryCodec()).packetCodec(PacketCodecs.registryEntry(RegistryKeys.FLUID)).build();

	public static void init(){
		Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(TechReborn.MOD_ID, "is_active"), IS_ACTIVE);
		Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(TechReborn.MOD_ID, "aoe5"), AOE5);
		Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(TechReborn.MOD_ID, "frequency_transmitter"), FREQUENCY_TRANSMITTER);
		Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(TechReborn.MOD_ID, "painting_cover"), PAINTING_COVER);
		Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(TechReborn.MOD_ID, "fluid"), FLUID);
	}
}
