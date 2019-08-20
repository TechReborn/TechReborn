package techreborn.blockentity.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.JsonHelper;
import org.apache.commons.lang3.Validate;
import reborncore.client.containerBuilder.builder.ContainerBlockEntityInventoryBuilder;
import reborncore.client.gui.builder.GuiBase;
import reborncore.common.util.serialization.SerializationUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DataDrivenSlot {

	public static List<DataDrivenSlot> read(JsonArray jsonArray){
		AtomicInteger idCount = new AtomicInteger();
		return SerializationUtil.stream(jsonArray)
			.map(JsonElement::getAsJsonObject)
			.map(json -> new DataDrivenSlot(idCount.getAndIncrement(), JsonHelper.getInt(json, "x"), JsonHelper.getInt(json, "y"), SlotType.fromString(JsonHelper.getString(json, "type"))))
			.collect(Collectors.toList());
	}

	private final int id;
	private final int x;
	private final int y;
	private final SlotType type;

	public DataDrivenSlot(int id, int x, int y, SlotType type) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.type = type;
		Validate.notNull(type);
	}

	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public SlotType getType() {
		return type;
	}

	public void add(ContainerBlockEntityInventoryBuilder inventoryBuilder){
		type.getSlotBiConsumer().accept(inventoryBuilder, this);
	}

	@Environment(EnvType.CLIENT)
	public void draw(GuiBase guiBase, GuiBase.Layer layer){
		//TODO find a better way to do this
		if(getType() == SlotType.OUTPUT){
			guiBase.drawOutputSlot(getX(), getY(), layer);
		} else {
			guiBase.drawSlot(getX(), getY(), layer);
		}
	}
}
