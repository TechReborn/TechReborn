package techreborn.component;

import com.mojang.serialization.codecs.PrimitiveCodec;
import net.minecraft.component.DataComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import techreborn.TechReborn;

import java.util.function.UnaryOperator;

public class DataComponentTypes {

	public static final DataComponentType<Boolean> IS_ACTIVE =
		DataComponentTypes.register(
			"is_active",
			builder -> builder
				.codec(PrimitiveCodec.BOOL)
				.packetCodec(PacketCodecs.BOOL)
		);

	private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
		return Registry.register(
			Registries.DATA_COMPONENT_TYPE,
			new Identifier(TechReborn.MOD_ID, id),
			((DataComponentType.Builder) builderOperator.apply(DataComponentType.builder())).build());
	}
}
