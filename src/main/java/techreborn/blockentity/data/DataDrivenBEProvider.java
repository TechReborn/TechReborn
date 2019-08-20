package techreborn.blockentity.data;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import net.fabricmc.loader.launch.common.FabricLauncherBase;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.WordUtils;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBlockEntityInventoryBuilder;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.serialization.SerializationUtil;
import techreborn.blockentity.GenericMachineBlockEntity;
import techreborn.init.ModRecipes;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class DataDrivenBEProvider extends BlockEntityType<DataDrivenBEProvider.DataDrivenBlockEntity> implements Supplier<BlockEntity> {

	private final Identifier identifier;
	private final Block block;
	private final int energy;
	private final int maxInput;

	private final List<DataDrivenSlot> slots;

	public static DataDrivenBEProvider create(Block block, Identifier identifier){
		String location = String.format("%s/machines/%s.json", identifier.getNamespace(), identifier.getPath());
		JsonObject jsonObject;
		try {
			jsonObject = SerializationUtil.GSON.fromJson(IOUtils.toString(FabricLauncherBase.getLauncher().getResourceAsStream(location), StandardCharsets.UTF_8), JsonObject.class);
		} catch (Exception e) {
			throw new RuntimeException("failed to read json: " + location,  e);
		}
		Identifier id = new Identifier(JsonHelper.getString(jsonObject, "name"));
		DataDrivenBEProvider provider = new DataDrivenBEProvider(block, jsonObject);
		Registry.register(Registry.BLOCK_ENTITY, id, provider);
		return provider;
	}

	private DataDrivenBEProvider(Block block, JsonObject jsonObject) {
		super(null, ImmutableSet.copyOf(Collections.singletonList(block)), null);
		this.block = block;
		this.identifier = new Identifier(JsonHelper.getString(jsonObject, "name"));
		this.energy = JsonHelper.getInt(jsonObject, "energy");
		this.maxInput = JsonHelper.getInt(jsonObject, "maxInput");
		this.slots = DataDrivenSlot.read(JsonHelper.getArray(jsonObject, "slots"));
		Validate.isTrue(getEnergySlot() > 0); //Ensure there is an energy slot, doing it here so it crashes on game load
	}

	@Nullable
	@Override
	public DataDrivenBlockEntity instantiate() {
		return new DataDrivenBlockEntity(this);
	}

	public BuiltContainer createContainer(DataDrivenBlockEntity blockEntity, int syncID, PlayerEntity player) {
		ContainerBlockEntityInventoryBuilder builder = new ContainerBuilder(identifier.getPath()).player(player.inventory)
			.inventory().hotbar().addInventory().blockEntity(blockEntity);

		slots.forEach(dataDrivenSlot -> dataDrivenSlot.add(builder));

		builder.syncEnergyValue().syncCrafterValue();

		return builder.addInventory().create(blockEntity, syncID);
	}

	//Used by the GenericMachineBlock
	@Override
	public BlockEntity get() {
		return instantiate();
	}

	public static class DataDrivenBlockEntity extends GenericMachineBlockEntity implements IContainerProvider {

		private final DataDrivenBEProvider provider;

		private DataDrivenBlockEntity(DataDrivenBEProvider provider) {
			super(provider, provider.getSimpleName(), provider.maxInput, provider.energy, provider.block, provider.getEnergySlot());
			this.provider = provider;

			RebornRecipeType recipeType = ModRecipes.byName(provider.identifier);
			Validate.notNull(recipeType);

			this.inventory = new RebornInventory<>(provider.slots.size(), provider.getSimpleName() + "BlockEntity", 64, this);
			this.crafter = new RecipeCrafter(recipeType, this, provider.countOfSlotType(SlotType.INPUT), provider.countOfSlotType(SlotType.OUTPUT), this.inventory, provider.slotIds(SlotType.INPUT), provider.slotIds(SlotType.OUTPUT));
		}

		@Override
		public BuiltContainer createContainer(int syncID, PlayerEntity player) {
			return provider.createContainer(this,syncID, player);
		}

		public DataDrivenBEProvider getProvider() {
			return provider;
		}
	}

	private int getEnergySlot() {
		return slots.stream().filter(slot -> slot.getType() == SlotType.ENERGY).findFirst().orElse(null).getId();
	}

	private int countOfSlotType(SlotType type){
		return (int) slots.stream()
			.filter(slot -> slot.getType() == type)
			.count();
	}

	private int[] slotIds(SlotType type){
		return slots.stream()
			.filter(slot -> slot.getType() == type)
			.mapToInt(DataDrivenSlot::getId)
			.toArray();
	}

	public List<DataDrivenSlot> getSlots() {
		return Collections.unmodifiableList(slots);
	}

	private String getSimpleName(){
		return WordUtils.capitalize(identifier.getPath());
	}
}
