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

package techreborn.compat.rei;

import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.*;
import me.shedaniel.rei.api.fluid.FluidSupportProvider;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import me.shedaniel.rei.api.widgets.Tooltip;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.EntryWidget;
import me.shedaniel.rei.gui.widget.Widget;
import me.shedaniel.rei.impl.RenderingEntry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reborncore.api.blockentity.IUpgradeable;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.slot.GuiTab;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.RecipeManager;
import reborncore.common.fluid.container.ItemFluidInfo;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.TechReborn;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.GeneratorRecipeHelper;
import techreborn.api.recipe.recipes.FluidReplicatorRecipe;
import techreborn.api.recipe.recipes.RollingMachineRecipe;
import techreborn.compat.rei.fluidgenerator.FluidGeneratorRecipeCategory;
import techreborn.compat.rei.fluidgenerator.FluidGeneratorRecipeDisplay;
import techreborn.compat.rei.fluidreplicator.FluidReplicatorRecipeCategory;
import techreborn.compat.rei.fluidreplicator.FluidReplicatorRecipeDisplay;
import techreborn.compat.rei.machine.*;
import techreborn.compat.rei.rollingmachine.RollingMachineCategory;
import techreborn.compat.rei.rollingmachine.RollingMachineDisplay;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;
import techreborn.init.TRContent.Machine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class ReiPlugin implements REIPluginV0 {

	public static final Identifier PLUGIN = new Identifier(TechReborn.MOD_ID, "techreborn_plugin");

	public static final Map<RebornRecipeType<?>, ItemConvertible> iconMap = new HashMap<>();

	public ReiPlugin() {
		iconMap.put(ModRecipes.ALLOY_SMELTER, Machine.ALLOY_SMELTER);
		iconMap.put(ModRecipes.ASSEMBLING_MACHINE, Machine.ASSEMBLY_MACHINE);
		iconMap.put(ModRecipes.BLAST_FURNACE, Machine.INDUSTRIAL_BLAST_FURNACE);
		iconMap.put(ModRecipes.CENTRIFUGE, Machine.INDUSTRIAL_CENTRIFUGE);
		iconMap.put(ModRecipes.CHEMICAL_REACTOR, Machine.CHEMICAL_REACTOR);
		iconMap.put(ModRecipes.COMPRESSOR, Machine.COMPRESSOR);
		iconMap.put(ModRecipes.DISTILLATION_TOWER, Machine.DISTILLATION_TOWER);
		iconMap.put(ModRecipes.EXTRACTOR, Machine.EXTRACTOR);
		iconMap.put(ModRecipes.FLUID_REPLICATOR, Machine.FLUID_REPLICATOR);
		iconMap.put(ModRecipes.FUSION_REACTOR, Machine.FUSION_CONTROL_COMPUTER);
		iconMap.put(ModRecipes.GRINDER, Machine.GRINDER);
		iconMap.put(ModRecipes.IMPLOSION_COMPRESSOR, Machine.IMPLOSION_COMPRESSOR);
		iconMap.put(ModRecipes.INDUSTRIAL_ELECTROLYZER, Machine.INDUSTRIAL_ELECTROLYZER);
		iconMap.put(ModRecipes.INDUSTRIAL_GRINDER, Machine.INDUSTRIAL_GRINDER);
		iconMap.put(ModRecipes.INDUSTRIAL_SAWMILL, Machine.INDUSTRIAL_SAWMILL);
		iconMap.put(ModRecipes.ROLLING_MACHINE, Machine.ROLLING_MACHINE);
		iconMap.put(ModRecipes.SCRAPBOX, TRContent.SCRAP_BOX);
		iconMap.put(ModRecipes.SOLID_CANNING_MACHINE, Machine.SOLID_CANNING_MACHINE);
		iconMap.put(ModRecipes.VACUUM_FREEZER, Machine.VACUUM_FREEZER);
		iconMap.put(ModRecipes.WIRE_MILL, Machine.WIRE_MILL);
	}

	@Override
	public Identifier getPluginIdentifier() {
		return PLUGIN;
	}

	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
		recipeHelper.registerCategory(new TwoInputsCenterOutputCategory<>(ModRecipes.ALLOY_SMELTER));
		recipeHelper.registerCategory(new AssemblingMachineCategory<>(ModRecipes.ASSEMBLING_MACHINE));
		recipeHelper.registerCategory(new BlastFurnaceCategory<>(ModRecipes.BLAST_FURNACE));
		recipeHelper.registerCategory(new IndustrialCentrifugeCategory<>(ModRecipes.CENTRIFUGE));
		recipeHelper.registerCategory(new TwoInputsCenterOutputCategory<>(ModRecipes.CHEMICAL_REACTOR));
		recipeHelper.registerCategory(new OneInputOneOutputCategory<>(ModRecipes.COMPRESSOR));
		recipeHelper.registerCategory(new DistillationTowerCategory<>(ModRecipes.DISTILLATION_TOWER));
		recipeHelper.registerCategory(new OneInputOneOutputCategory<>(ModRecipes.EXTRACTOR));
		recipeHelper.registerCategory(new FluidReplicatorRecipeCategory(ModRecipes.FLUID_REPLICATOR));
		recipeHelper.registerCategory(new TwoInputsCenterOutputCategory<>(ModRecipes.FUSION_REACTOR));
		recipeHelper.registerCategory(new OneInputOneOutputCategory<>(ModRecipes.GRINDER));
		recipeHelper.registerCategory(new ImplosionCompressorCategory<>(ModRecipes.IMPLOSION_COMPRESSOR));
		recipeHelper.registerCategory(new ElectrolyzerCategory<>(ModRecipes.INDUSTRIAL_ELECTROLYZER));
		recipeHelper.registerCategory(new GrinderCategory<>(ModRecipes.INDUSTRIAL_GRINDER));
		recipeHelper.registerCategory(new SawmillCategory<>(ModRecipes.INDUSTRIAL_SAWMILL));
		recipeHelper.registerCategory(new RollingMachineCategory(ModRecipes.ROLLING_MACHINE));
		recipeHelper.registerCategory(new OneInputOneOutputCategory<>(ModRecipes.SCRAPBOX));
		recipeHelper.registerCategory(new TwoInputsCenterOutputCategory<>(ModRecipes.SOLID_CANNING_MACHINE));
		recipeHelper.registerCategory(new OneInputOneOutputCategory<>(ModRecipes.VACUUM_FREEZER));
		recipeHelper.registerCategory(new OneInputOneOutputCategory<>(ModRecipes.WIRE_MILL));

		recipeHelper.registerCategory(new FluidGeneratorRecipeCategory(Machine.THERMAL_GENERATOR));
		recipeHelper.registerCategory(new FluidGeneratorRecipeCategory(Machine.GAS_TURBINE));
		recipeHelper.registerCategory(new FluidGeneratorRecipeCategory(Machine.DIESEL_GENERATOR));
		recipeHelper.registerCategory(new FluidGeneratorRecipeCategory(Machine.SEMI_FLUID_GENERATOR));
		recipeHelper.registerCategory(new FluidGeneratorRecipeCategory(Machine.PLASMA_GENERATOR));
	}

	@Override
	public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		RecipeManager.getRecipeTypes("techreborn").forEach(rebornRecipeType -> registerMachineRecipe(recipeHelper, rebornRecipeType));

		registerFluidGeneratorDisplays(recipeHelper, EFluidGenerator.THERMAL, Machine.THERMAL_GENERATOR);
		registerFluidGeneratorDisplays(recipeHelper, EFluidGenerator.GAS, Machine.GAS_TURBINE);
		registerFluidGeneratorDisplays(recipeHelper, EFluidGenerator.DIESEL, Machine.DIESEL_GENERATOR);
		registerFluidGeneratorDisplays(recipeHelper, EFluidGenerator.SEMIFLUID, Machine.SEMI_FLUID_GENERATOR);
		registerFluidGeneratorDisplays(recipeHelper, EFluidGenerator.PLASMA, Machine.PLASMA_GENERATOR);
	}

	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(ModRecipes.ALLOY_SMELTER.name(), EntryStack.create(Machine.ALLOY_SMELTER), EntryStack.create(Machine.IRON_ALLOY_FURNACE));
		recipeHelper.registerWorkingStations(ModRecipes.ASSEMBLING_MACHINE.name(), EntryStack.create(Machine.ASSEMBLY_MACHINE));
		recipeHelper.registerWorkingStations(ModRecipes.BLAST_FURNACE.name(), EntryStack.create(Machine.INDUSTRIAL_BLAST_FURNACE));
		recipeHelper.registerWorkingStations(ModRecipes.CENTRIFUGE.name(), EntryStack.create(Machine.INDUSTRIAL_CENTRIFUGE));
		recipeHelper.registerWorkingStations(ModRecipes.CHEMICAL_REACTOR.name(), EntryStack.create(Machine.CHEMICAL_REACTOR));
		recipeHelper.registerWorkingStations(ModRecipes.COMPRESSOR.name(), EntryStack.create(Machine.COMPRESSOR));
		recipeHelper.registerWorkingStations(ModRecipes.DISTILLATION_TOWER.name(), EntryStack.create(Machine.DISTILLATION_TOWER));
		recipeHelper.registerWorkingStations(ModRecipes.EXTRACTOR.name(), EntryStack.create(Machine.EXTRACTOR));
		recipeHelper.registerWorkingStations(ModRecipes.FLUID_REPLICATOR.name(), EntryStack.create(Machine.FLUID_REPLICATOR));
		recipeHelper.registerWorkingStations(ModRecipes.FUSION_REACTOR.name(), EntryStack.create(Machine.FUSION_CONTROL_COMPUTER));
		recipeHelper.registerWorkingStations(ModRecipes.GRINDER.name(), EntryStack.create(Machine.GRINDER));
		recipeHelper.registerWorkingStations(ModRecipes.IMPLOSION_COMPRESSOR.name(), EntryStack.create(Machine.IMPLOSION_COMPRESSOR));
		recipeHelper.registerWorkingStations(ModRecipes.INDUSTRIAL_ELECTROLYZER.name(), EntryStack.create(Machine.INDUSTRIAL_ELECTROLYZER));
		recipeHelper.registerWorkingStations(ModRecipes.INDUSTRIAL_GRINDER.name(), EntryStack.create(Machine.INDUSTRIAL_GRINDER));
		recipeHelper.registerWorkingStations(ModRecipes.INDUSTRIAL_SAWMILL.name(), EntryStack.create(Machine.INDUSTRIAL_SAWMILL));
		recipeHelper.registerWorkingStations(ModRecipes.ROLLING_MACHINE.name(), EntryStack.create(Machine.ROLLING_MACHINE));
		recipeHelper.registerWorkingStations(ModRecipes.SOLID_CANNING_MACHINE.name(), EntryStack.create(Machine.SOLID_CANNING_MACHINE));
		recipeHelper.registerWorkingStations(ModRecipes.VACUUM_FREEZER.name(), EntryStack.create(Machine.VACUUM_FREEZER));
		recipeHelper.registerWorkingStations(ModRecipes.WIRE_MILL.name(), EntryStack.create(Machine.WIRE_MILL));
		recipeHelper.registerWorkingStations(new Identifier(TechReborn.MOD_ID, Machine.THERMAL_GENERATOR.name), EntryStack.create(Machine.THERMAL_GENERATOR));
		recipeHelper.registerWorkingStations(new Identifier(TechReborn.MOD_ID, Machine.GAS_TURBINE.name), EntryStack.create(Machine.GAS_TURBINE));
		recipeHelper.registerWorkingStations(new Identifier(TechReborn.MOD_ID, Machine.DIESEL_GENERATOR.name), EntryStack.create(Machine.DIESEL_GENERATOR));
		recipeHelper.registerWorkingStations(new Identifier(TechReborn.MOD_ID, Machine.SEMI_FLUID_GENERATOR.name), EntryStack.create(Machine.SEMI_FLUID_GENERATOR));
		recipeHelper.registerWorkingStations(new Identifier(TechReborn.MOD_ID, Machine.PLASMA_GENERATOR.name), EntryStack.create(Machine.PLASMA_GENERATOR));

		registerFluidSupport();
	}

	@SuppressWarnings("UnstableApiUsage")
	private void registerFluidSupport() {
		FluidSupportProvider.INSTANCE.registerFluidProvider(new FluidSupportProvider.FluidProvider() {
			@Override
			@NotNull
			public EntryStack itemToFluid(@NotNull EntryStack itemStack) {
				if (itemStack.getItem() instanceof ItemFluidInfo) {
					Fluid fluid = ((ItemFluidInfo) itemStack.getItem()).getFluid(itemStack.getItemStack());
					if (fluid != null)
						return EntryStack.create(fluid);
				}
				return EntryStack.empty();
			}
		});
	}

	@Override
	public void postRegister() {
		// Alright we are going to apply check tags to cells, this should not take long at all.
		// Check Tags will not check the amount of the ItemStack, but will enable checking their tags.
		EntryRegistry.getInstance().getEntryStacks().forEach(ReiPlugin::applyCellEntry);
	}

	public static void applyCellEntry(EntryStack stack) {
		// getItem can be null but this works
		if (stack.getItem() == TRContent.CELL)
			stack.setting(EntryStack.Settings.CHECK_TAGS, EntryStack.Settings.TRUE);
	}

	private void registerFluidGeneratorDisplays(RecipeHelper recipeHelper, EFluidGenerator generator, Machine machine) {
		Identifier identifier = new Identifier(TechReborn.MOD_ID, machine.name);
		GeneratorRecipeHelper.getFluidRecipesForGenerator(generator).getRecipes().forEach(recipe ->
			recipeHelper.registerDisplay(new FluidGeneratorRecipeDisplay(recipe, identifier))
		);
	}

	private <R extends RebornRecipe> void registerMachineRecipe(RecipeHelper recipeHelper, RebornRecipeType<R> recipeType) {
		Function<R, RecipeDisplay> recipeDisplay = r -> new MachineRecipeDisplay<>((RebornRecipe) r);

		if (recipeType == ModRecipes.ROLLING_MACHINE) {
			recipeDisplay = r -> {
				RollingMachineRecipe rollingMachineRecipe = (RollingMachineRecipe) r;
				return new RollingMachineDisplay(rollingMachineRecipe.getShapedRecipe());
			};
		}

		if (recipeType == ModRecipes.FLUID_REPLICATOR) {
			recipeDisplay = r -> {
				FluidReplicatorRecipe recipe = (FluidReplicatorRecipe) r;
				return new FluidReplicatorRecipeDisplay(recipe);
			};
		}

		recipeHelper.registerRecipes(recipeType.name(), (Predicate<Recipe>) recipe -> {
			if (recipe instanceof RebornRecipe) {
				return ((RebornRecipe) recipe).getRebornRecipeType() == recipeType;
			}
			return false;
		}, recipeDisplay);
	}

	@Override
	public void registerBounds(DisplayHelper displayHelper) {
		BaseBoundsHandler baseBoundsHandler = BaseBoundsHandler.getInstance();
		baseBoundsHandler.registerExclusionZones(GuiBase.class, () -> {
			Screen currentScreen = MinecraftClient.getInstance().currentScreen;
			if (currentScreen instanceof GuiBase<?> guiBase) {
				int height = 0;
				if (guiBase.tryAddUpgrades() && guiBase.be instanceof IUpgradeable upgradeable) {
					if (upgradeable.canBeUpgraded()) {
						height = 80;
					}
				}
				for (GuiTab slot : guiBase.getTabs()) {
					if (slot.enabled()) {
						height += 24;
					}
				}
				if (height > 0) {
					int width = 20;
					return Collections.singletonList(new Rectangle(guiBase.getGuiLeft() - width, guiBase.getGuiTop() + 8, width, height));
				}
			}
			return Collections.emptyList();
		});
	}

	public static Widget createProgressBar(int x, int y, double animationDuration, GuiBuilder.ProgressDirection direction) {
		return Widgets.createDrawableWidget((helper, matrices, mouseX, mouseY, delta) -> {
			MinecraftClient.getInstance().getTextureManager().bindTexture(GuiBuilder.defaultTextureSheet);
			helper.drawTexture(matrices, x, y, direction.x, direction.y, direction.width, direction.height);
			int j = (int) ((System.currentTimeMillis() / animationDuration) % 1.0 * 16.0);
			if (j < 0) {
				j = 0;
			}

			switch (direction) {
				case RIGHT -> helper.drawTexture(matrices, x, y, direction.xActive, direction.yActive, j, 10);
				case LEFT -> helper.drawTexture(matrices, x + 16 - j, y, direction.xActive + 16 - j, direction.yActive, j, 10);
				case UP -> helper.drawTexture(matrices, x, y + 16 - j, direction.xActive, direction.yActive + 16 - j, 10, j);
				case DOWN -> helper.drawTexture(matrices, x, y, direction.xActive, direction.yActive, 10, j);
			}
		});
	}

	public static Widget createEnergyDisplay(Rectangle bounds, double energy, EntryAnimation animation, Function<Point, Tooltip> tooltipBuilder) {
		return new EnergyEntryWidget(bounds, animation).entry(
				new RenderingEntry() {
					@Override
					public void render(MatrixStack matrices, Rectangle bounds, int mouseX, int mouseY, float delta) {}

					@Override
					public @Nullable Tooltip getTooltip(Point mouse) {
						return tooltipBuilder.apply(mouse);
					}
				}
		).notFavoritesInteractable();
	}

	public static Widget createFluidDisplay(Rectangle bounds, EntryStack fluid, EntryAnimation animation) {
		return new FluidEntryWidget(bounds, fluid.getFluid(), animation).entry(fluid);
	}

	private static class EnergyEntryWidget extends EntryWidget {
		private EntryAnimation animation;

		protected EnergyEntryWidget(Rectangle rectangle, EntryAnimation animation) {
			super(rectangle.x, rectangle.y);
			this.getBounds().setBounds(rectangle);
			this.animation = animation;
		}

		@Override
		protected void drawBackground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			if (background) {
				Rectangle bounds = getBounds();
				int width = bounds.width;
				int height = bounds.height;
				int innerHeight = height - 2;

				PowerSystem.EnergySystem displayPower = PowerSystem.getDisplayPower();
				MinecraftClient.getInstance().getTextureManager().bindTexture(GuiBuilder.defaultTextureSheet);
				drawTexture(matrices, bounds.x, bounds.y, displayPower.xBar - 15, displayPower.yBar - 1, width, height);
				int innerDisplayHeight;
				if (animation.animationType != EntryAnimationType.NONE) {
					innerDisplayHeight = MathHelper.ceil((System.currentTimeMillis() / (animation.duration / innerHeight) % innerHeight));
					if (animation.animationType == EntryAnimationType.DOWNWARDS)
						innerDisplayHeight = innerHeight - innerDisplayHeight;
				} else innerDisplayHeight = innerHeight;
				drawTexture(matrices, bounds.x + 1, bounds.y + 1 + innerHeight - innerDisplayHeight, displayPower.xBar, innerHeight + displayPower.yBar - innerDisplayHeight, width - 2, innerDisplayHeight);
			}
		}

		@Override
		protected void drawCurrentEntry(MatrixStack matrices, int mouseX, int mouseY, float delta) {}
	}

	private static class FluidEntryWidget extends EntryWidget {
		private Fluid fluid;
		private EntryAnimation animation;

		protected FluidEntryWidget(Rectangle rectangle, Fluid fluid, EntryAnimation animation) {
			super(rectangle.x, rectangle.y);
			this.getBounds().setBounds(rectangle);
			this.fluid = fluid;
			this.animation = animation;
		}

		@Override
		protected void drawBackground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			if (background) {
				Rectangle bounds = getBounds();
				int width = bounds.width;
				int height = bounds.height;
				int innerHeight = height - 2;

				PowerSystem.EnergySystem displayPower = PowerSystem.getDisplayPower();
				MinecraftClient.getInstance().getTextureManager().bindTexture(GuiBuilder.defaultTextureSheet);
				drawTexture(matrices, bounds.x - 3, bounds.y - 3, 194, 26, width + 6, height + 6);
				drawTexture(matrices, bounds.x, bounds.y, 194, 82, width, height);
				int innerDisplayHeight;
				if (animation.animationType != EntryAnimationType.NONE) {
					innerDisplayHeight = MathHelper.ceil((System.currentTimeMillis() / (animation.duration / innerHeight) % innerHeight));
					if (animation.animationType == EntryAnimationType.DOWNWARDS)
						innerDisplayHeight = innerHeight - innerDisplayHeight;
				} else innerDisplayHeight = innerHeight;
				drawFluid(matrices, fluid, innerDisplayHeight, bounds.x + 1, bounds.y + 1, width - 2, innerHeight);
			}
		}

		public void drawFluid(MatrixStack matrixStack, Fluid fluid, int drawHeight, int x, int y, int width, int height) {
			MinecraftClient.getInstance().getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
			y += height;

			FluidRenderHandler handler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);

			// If registry can't find it, don't render.
			if(handler == null){
				return;
			}
			
			final Sprite sprite = handler.getFluidSprites(MinecraftClient.getInstance().world, BlockPos.ORIGIN, fluid.getDefaultState())[0];
			int color = FluidRenderHandlerRegistry.INSTANCE.get(fluid).getFluidColor(MinecraftClient.getInstance().world, BlockPos.ORIGIN, fluid.getDefaultState());

			final int iconHeight = sprite.getHeight();
			int offsetHeight = drawHeight;

			RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, 1F);

			int iteration = 0;
			while (offsetHeight != 0) {
				final int curHeight = Math.min(offsetHeight, iconHeight);

				DrawableHelper.drawSprite(matrixStack, x, y - offsetHeight, 0, width, curHeight, sprite);
				offsetHeight -= curHeight;
				iteration++;
				if (iteration > 50) {
					break;
				}
			}
		}

		@Override
		protected void drawCurrentEntry(MatrixStack matrices, int mouseX, int mouseY, float delta) {}
	}

	public record EntryAnimation(EntryAnimationType animationType, long duration) {

		public static EntryAnimation upwards(long duration) {
			return new EntryAnimation(EntryAnimationType.UPWARDS, duration);
		}

		public static EntryAnimation downwards(long duration) {
			return new EntryAnimation(EntryAnimationType.DOWNWARDS, duration);
		}

		public static EntryAnimation none() {
			return new EntryAnimation(EntryAnimationType.NONE, 0);
		}
	}

	public enum EntryAnimationType {
		UPWARDS,
		DOWNWARDS,
		NONE
	}
}
