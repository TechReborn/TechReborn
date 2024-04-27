package reborncore.common.screen.builder;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

import java.util.function.Consumer;
import java.util.function.Supplier;

public record SyncedObject<T>(PacketCodec<? super RegistryByteBuf, T> codec, Supplier<T> getter, Consumer<T> setter) {
}
