package reborncore.common.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;

import java.util.stream.Stream;

/**
 * Simple map codec for {@link JsonObject}.
 */
public class JsonMapCodec extends MapCodec<JsonObject> {
	public static final MapCodec<JsonObject> INSTANCE = new JsonMapCodec();

	@Override
	public <T> Stream<T> keys(DynamicOps<T> ops) {
		throw new UnsupportedOperationException("Not implemented yet! Should not be necessary for regular JSON serialization.");
	}

	@Override
	public <T> DataResult<JsonObject> decode(DynamicOps<T> ops, MapLike<T> input) {
		// convert input to JsonElement
		JsonElement converted = ops.convertTo(JsonOps.INSTANCE, ops.createMap(input.entries()));

		// convert JsonElement to JsonObject
		if (converted.isJsonObject()) {
			return DataResult.success(converted.getAsJsonObject());
		} else {
			return DataResult.error(() -> "Not a json object: " + converted);
		}
	}

	@Override
	public <T> RecordBuilder<T> encode(JsonObject input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
		// Convert each value to the correct type and add it to the prefix
		for (var entry : input.entrySet()) {
			// Convert each value
			var convertedValue = JsonOps.INSTANCE.convertTo(ops, entry.getValue());
			// Add to prefix
			prefix.add(entry.getKey(), convertedValue);
		}

		return prefix;
	}
}
