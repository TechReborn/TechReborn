package techreborn.component;

import com.mojang.serialization.codecs.PrimitiveCodec;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentType;
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

	public static final DataComponentType<Boolean> IS_ACTIVE =
		DataComponentType.<Boolean>builder().codec(PrimitiveCodec.BOOL).packetCodec(PacketCodecs.BOOL).build();

	public static final DataComponentType<Boolean> AOE5 =
		DataComponentType.<Boolean>builder().codec(PrimitiveCodec.BOOL).packetCodec(PacketCodecs.BOOL).build();

	public static final DataComponentType<GlobalPos> FREQUENCY_TRANSMITTER =
		DataComponentType.<GlobalPos>builder().codec(GlobalPos.CODEC).packetCodec(GlobalPos.PACKET_CODEC).build();

	public static final DataComponentType<BlockState> PAINTING_COVER =
		DataComponentType.<BlockState>builder().codec(BlockState.CODEC).packetCodec(BLOCK_STATE_PACKET_CODEC).build();

	public static final DataComponentType<RegistryEntry<Fluid>> FLUID =
		DataComponentType.<RegistryEntry<Fluid>>builder().codec(Registries.FLUID.getEntryCodec()).packetCodec(PacketCodecs.registryEntry(RegistryKeys.FLUID)).build();

	public static void init(){
		Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier(TechReborn.MOD_ID, "is_active"), IS_ACTIVE);
		Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier(TechReborn.MOD_ID, "aoe5"), AOE5);
		Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier(TechReborn.MOD_ID, "frequency_transmitter"), FREQUENCY_TRANSMITTER);
		Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier(TechReborn.MOD_ID, "painting_cover"), PAINTING_COVER);
		Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier(TechReborn.MOD_ID, "fluid"), FLUID);
	}
}
