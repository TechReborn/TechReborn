/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.blockentity.data;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import net.fabricmc.loader.launch.common.FabricLauncherBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.Nullable;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.screen.builder.BlockEntityScreenHandlerBuilder;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.serialization.SerializationUtil;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.init.ModRecipes;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class DataDrivenBEProvider extends BlockEntityType<DataDrivenBEProvider.DataDrivenBlockEntity> implements BiFunction<BlockPos, BlockState, BlockEntity> {

	private final Identifier identifier;
	private final Block block;
	private final int energy;
	private final int maxInput;

	private final List<DataDrivenSlot> slots;

	public static DataDrivenBEProvider create(Block block, Identifier identifier) {
		String location = String.format("%s/machines/%s.json", identifier.getNamespace(), identifier.getPath());
		JsonObject jsonObject;
		try {
			jsonObject = SerializationUtil.GSON.fromJson(IOUtils.toString(FabricLauncherBase.getLauncher().getResourceAsStream(location), StandardCharsets.UTF_8), JsonObject.class);
		} catch (Exception e) {
			throw new RuntimeException("failed to read json: " + location, e);
		}
		Identifier id = new Identifier(JsonHelper.getString(jsonObject, "name"));
		DataDrivenBEProvider provider = new DataDrivenBEProvider(block, jsonObject);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, id, provider);
		return provider;
	}

	private DataDrivenBEProvider(Block block, JsonObject jsonObject) {
		super(null, ImmutableSet.copyOf(Collections.singletonList(block)), null);
		this.block = block;
		this.identifier = new Identifier(JsonHelper.getString(jsonObject, "name"));
		this.energy = JsonHelper.getInt(jsonObject, "energy");
		this.maxInput = JsonHelper.getInt(jsonObject, "maxInput");
		this.slots = DataDrivenSlot.read(JsonHelper.getArray(jsonObject, "slots"));
		// Ensure there is an energy slot, doing it here ensures it crashes on game load
		Validate.isTrue(getEnergySlot() > 0);
	}

	@Nullable
	@Override
	public DataDrivenBlockEntity instantiate(BlockPos pos, BlockState state) {
		return new DataDrivenBlockEntity(this, pos, state);
	}

	public BuiltScreenHandler createScreenHandler(DataDrivenBlockEntity blockEntity, int syncID, PlayerEntity player) {
		BlockEntityScreenHandlerBuilder builder = new ScreenHandlerBuilder(identifier.getPath()).player(player.getInventory())
				.inventory().hotbar().addInventory().blockEntity(blockEntity);

		slots.forEach(dataDrivenSlot -> dataDrivenSlot.add(builder));

		builder.syncEnergyValue().syncCrafterValue();

		return builder.addInventory().create(blockEntity, syncID);
	}

	//Used by the GenericMachineBlock
	@Override
	public BlockEntity apply(BlockPos pos, BlockState state) {
		return instantiate(pos, state);
	}

	public static class DataDrivenBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {

		private final DataDrivenBEProvider provider;

		private DataDrivenBlockEntity(DataDrivenBEProvider provider, BlockPos pos, BlockState state) {
			super(provider, pos, state,  provider.getSimpleName(), provider.maxInput, provider.energy, provider.block, provider.getEnergySlot());
			this.provider = provider;

			RebornRecipeType<?> recipeType = ModRecipes.byName(provider.identifier);
			Validate.notNull(recipeType);

			this.inventory = new RebornInventory<>(provider.slots.size(), provider.getSimpleName() + "BlockEntity", 64, this);
			this.crafter = new RecipeCrafter(recipeType, this, provider.countOfSlotType(SlotType.INPUT), provider.countOfSlotType(SlotType.OUTPUT), this.inventory, provider.slotIds(SlotType.INPUT), provider.slotIds(SlotType.OUTPUT));
		}

		@Override
		public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
			return provider.createScreenHandler(this, syncID, player);
		}

		public DataDrivenBEProvider getProvider() {
			return provider;
		}
	}

	private int getEnergySlot() {
		return slots.stream().filter(slot -> slot.type() == SlotType.ENERGY).findFirst().orElse(null).id();
	}

	private int countOfSlotType(SlotType type) {
		return (int) slots.stream()
				.filter(slot -> slot.type() == type)
				.count();
	}

	private int[] slotIds(SlotType type) {
		return slots.stream()
				.filter(slot -> slot.type() == type)
				.mapToInt(DataDrivenSlot::id)
				.toArray();
	}

	public List<DataDrivenSlot> getSlots() {
		return Collections.unmodifiableList(slots);
	}

	private String getSimpleName() {
		return WordUtils.capitalize(identifier.getPath());
	}
}
