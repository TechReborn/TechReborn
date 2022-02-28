package reborncore.client.network;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import reborncore.common.network.ExtendedPacketBuffer;

import java.util.function.Consumer;

public class ClientNetworkManager {
	public static void registerClientBoundHandler(Identifier identifier, ClientPlayNetworking.PlayChannelHandler handler) {
		ClientPlayNetworking.registerGlobalReceiver(identifier, handler);
	}

	public static <T> void registerClientBoundHandler(Identifier identifier, Codec<T> codec, Consumer<T> consumer) {
		registerClientBoundHandler(identifier, (client, handler, buf, responseSender) -> {
			T value = new ExtendedPacketBuffer(buf).readCodec(codec);
			client.execute(() -> consumer.accept(value));
		});
	}
}
