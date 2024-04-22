package techreborn.component;

import com.mojang.serialization.codecs.PrimitiveCodec;
import net.minecraft.component.DataComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import techreborn.TechReborn;

public class TRDataComponentTypes {

	public static final DataComponentType<Boolean> IS_ACTIVE =
		DataComponentType.<Boolean>builder().codec(PrimitiveCodec.BOOL).packetCodec(PacketCodecs.BOOL).build();

	public static final DataComponentType<Boolean> AOE5 =
		DataComponentType.<Boolean>builder().codec(PrimitiveCodec.BOOL).packetCodec(PacketCodecs.BOOL).build();


	public static void init(){
		Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier(TechReborn.MOD_ID, "is_active"), IS_ACTIVE);
		Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier(TechReborn.MOD_ID, "aoe5"), AOE5);
	}


}
