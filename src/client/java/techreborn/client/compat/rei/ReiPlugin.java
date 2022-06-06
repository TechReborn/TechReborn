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

package techreborn.client.compat.rei;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.event.CompoundEventResult;
import dev.architectury.fluid.FluidStack;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.renderer.AbstractEntryRenderer;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.client.gui.AbstractRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.util.ClientEntryStacks;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.fluid.FluidSupportProvider;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.impl.client.gui.widget.EntryWidget;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
import techreborn.client.compat.rei.fluidgenerator.FluidGeneratorRecipeCategory;
import techreborn.client.compat.rei.fluidgenerator.FluidGeneratorRecipeDisplay;
import techreborn.client.compat.rei.fluidreplicator.FluidReplicatorRecipeCategory;
import techreborn.client.compat.rei.fluidreplicator.FluidReplicatorRecipeDisplay;
import techreborn.client.compat.rei.machine.*;
import techreborn.client.compat.rei.rollingmachine.RollingMachineCategory;
import techreborn.client.compat.rei.rollingmachine.RollingMachineDisplay;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;
import techreborn.init.TRContent.Machine;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class ReiPlugin implements REIClientPlugin {
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
	public void registerCategories(CategoryRegistry registry) {
		registry.add(new TwoInputsCenterOutputCategory<>(ModRecipes.ALLOY_SMELTER));
		registry.add(new AssemblingMachineCategory<>(ModRecipes.ASSEMBLING_MACHINE));
		registry.add(new BlastFurnaceCategory<>(ModRecipes.BLAST_FURNACE));
		registry.add(new IndustrialCentrifugeCategory<>(ModRecipes.CENTRIFUGE));
		registry.add(new TwoInputsCenterOutputCategory<>(ModRecipes.CHEMICAL_REACTOR));
		registry.add(new OneInputOneOutputCategory<>(ModRecipes.COMPRESSOR));
		registry.add(new DistillationTowerCategory<>(ModRecipes.DISTILLATION_TOWER));
		registry.add(new OneInputOneOutputCategory<>(ModRecipes.EXTRACTOR));
		registry.add(new FluidReplicatorRecipeCategory(ModRecipes.FLUID_REPLICATOR));
		registry.add(new TwoInputsCenterOutputCategory<>(ModRecipes.FUSION_REACTOR));
		registry.add(new OneInputOneOutputCategory<>(ModRecipes.GRINDER));
		registry.add(new ImplosionCompressorCategory<>(ModRecipes.IMPLOSION_COMPRESSOR));
		registry.add(new ElectrolyzerCategory<>(ModRecipes.INDUSTRIAL_ELECTROLYZER));
		registry.add(new GrinderCategory<>(ModRecipes.INDUSTRIAL_GRINDER));
		registry.add(new SawmillCategory<>(ModRecipes.INDUSTRIAL_SAWMILL));
		registry.add(new RollingMachineCategory(ModRecipes.ROLLING_MACHINE));
		registry.add(new OneInputOneOutputCategory<>(ModRecipes.SCRAPBOX));
		registry.add(new TwoInputsCenterOutputCategory<>(ModRecipes.SOLID_CANNING_MACHINE));
		registry.add(new OneInputOneOutputCategory<>(ModRecipes.VACUUM_FREEZER));
		registry.add(new OneInputOneOutputCategory<>(ModRecipes.WIRE_MILL));

		registry.add(new FluidGeneratorRecipeCategory(Machine.THERMAL_GENERATOR));
		registry.add(new FluidGeneratorRecipeCategory(Machine.GAS_TURBINE));
		registry.add(new FluidGeneratorRecipeCategory(Machine.DIESEL_GENERATOR));
		registry.add(new FluidGeneratorRecipeCategory(Machine.SEMI_FLUID_GENERATOR));
		registry.add(new FluidGeneratorRecipeCategory(Machine.PLASMA_GENERATOR));

		addWorkstations(ModRecipes.ALLOY_SMELTER.name(), EntryStacks.of(Machine.ALLOY_SMELTER), EntryStacks.of(Machine.IRON_ALLOY_FURNACE));
		addWorkstations(ModRecipes.ASSEMBLING_MACHINE.name(), EntryStacks.of(Machine.ASSEMBLY_MACHINE));
		addWorkstations(ModRecipes.BLAST_FURNACE.name(), EntryStacks.of(Machine.INDUSTRIAL_BLAST_FURNACE));
		addWorkstations(ModRecipes.CENTRIFUGE.name(), EntryStacks.of(Machine.INDUSTRIAL_CENTRIFUGE));
		addWorkstations(ModRecipes.CHEMICAL_REACTOR.name(), EntryStacks.of(Machine.CHEMICAL_REACTOR));
		addWorkstations(ModRecipes.COMPRESSOR.name(), EntryStacks.of(Machine.COMPRESSOR));
		addWorkstations(ModRecipes.DISTILLATION_TOWER.name(), EntryStacks.of(Machine.DISTILLATION_TOWER));
		addWorkstations(ModRecipes.EXTRACTOR.name(), EntryStacks.of(Machine.EXTRACTOR));
		addWorkstations(ModRecipes.FLUID_REPLICATOR.name(), EntryStacks.of(Machine.FLUID_REPLICATOR));
		addWorkstations(ModRecipes.FUSION_REACTOR.name(), EntryStacks.of(Machine.FUSION_CONTROL_COMPUTER));
		addWorkstations(ModRecipes.GRINDER.name(), EntryStacks.of(Machine.GRINDER));
		addWorkstations(ModRecipes.IMPLOSION_COMPRESSOR.name(), EntryStacks.of(Machine.IMPLOSION_COMPRESSOR));
		addWorkstations(ModRecipes.INDUSTRIAL_ELECTROLYZER.name(), EntryStacks.of(Machine.INDUSTRIAL_ELECTROLYZER));
		addWorkstations(ModRecipes.INDUSTRIAL_GRINDER.name(), EntryStacks.of(Machine.INDUSTRIAL_GRINDER));
		addWorkstations(ModRecipes.INDUSTRIAL_SAWMILL.name(), EntryStacks.of(Machine.INDUSTRIAL_SAWMILL));
		addWorkstations(ModRecipes.ROLLING_MACHINE.name(), EntryStacks.of(Machine.ROLLING_MACHINE));
		addWorkstations(ModRecipes.SOLID_CANNING_MACHINE.name(), EntryStacks.of(Machine.SOLID_CANNING_MACHINE));
		addWorkstations(ModRecipes.VACUUM_FREEZER.name(), EntryStacks.of(Machine.VACUUM_FREEZER));
		addWorkstations(ModRecipes.WIRE_MILL.name(), EntryStacks.of(Machine.WIRE_MILL));
		registry.addWorkstations(CategoryIdentifier.of(TechReborn.MOD_ID, Machine.THERMAL_GENERATOR.name), EntryStacks.of(Machine.THERMAL_GENERATOR));
		registry.addWorkstations(CategoryIdentifier.of(TechReborn.MOD_ID, Machine.GAS_TURBINE.name), EntryStacks.of(Machine.GAS_TURBINE));
		registry.addWorkstations(CategoryIdentifier.of(TechReborn.MOD_ID, Machine.DIESEL_GENERATOR.name), EntryStacks.of(Machine.DIESEL_GENERATOR));
		registry.addWorkstations(CategoryIdentifier.of(TechReborn.MOD_ID, Machine.SEMI_FLUID_GENERATOR.name), EntryStacks.of(Machine.SEMI_FLUID_GENERATOR));
		registry.addWorkstations(CategoryIdentifier.of(TechReborn.MOD_ID, Machine.PLASMA_GENERATOR.name), EntryStacks.of(Machine.PLASMA_GENERATOR));
	}

	private void addWorkstations(Identifier identifier, EntryStack<?>... stacks) {
		CategoryRegistry.getInstance().addWorkstations(CategoryIdentifier.of(identifier), stacks);
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		RecipeManager.getRecipeTypes("techreborn").forEach(rebornRecipeType -> registerMachineRecipe(registry, rebornRecipeType));

		registerFluidGeneratorDisplays(registry, EFluidGenerator.THERMAL, Machine.THERMAL_GENERATOR);
		registerFluidGeneratorDisplays(registry, EFluidGenerator.GAS, Machine.GAS_TURBINE);
		registerFluidGeneratorDisplays(registry, EFluidGenerator.DIESEL, Machine.DIESEL_GENERATOR);
		registerFluidGeneratorDisplays(registry, EFluidGenerator.SEMIFLUID, Machine.SEMI_FLUID_GENERATOR);
		registerFluidGeneratorDisplays(registry, EFluidGenerator.PLASMA, Machine.PLASMA_GENERATOR);
	}

	@Override
	public void registerFluidSupport(FluidSupportProvider support) {
		support.register(new FluidSupportProvider.Provider() {
			@Override
			public CompoundEventResult<Stream<EntryStack<FluidStack>>> itemToFluid(EntryStack<? extends ItemStack> stack) {
				ItemStack itemStack = stack.getValue();
				if (itemStack.getItem() instanceof ItemFluidInfo) {
					Fluid fluid = ((ItemFluidInfo) itemStack.getItem()).getFluid(itemStack);
					if (fluid != null)
						return CompoundEventResult.interruptTrue(Stream.of(EntryStacks.of(fluid)));
				}
				return CompoundEventResult.pass();
			}
		});
	}

	@Override
	public void registerItemComparators(ItemComparatorRegistry registry) {
		registry.registerNbt(TRContent.CELL);
	}

	private void registerFluidGeneratorDisplays(DisplayRegistry registry, EFluidGenerator generator, Machine machine) {
		Identifier identifier = new Identifier(TechReborn.MOD_ID, machine.name);
		GeneratorRecipeHelper.getFluidRecipesForGenerator(generator).getRecipes().forEach(recipe ->
				registry.add(new FluidGeneratorRecipeDisplay(recipe, identifier))
		);
	}

	private <R extends RebornRecipe> void registerMachineRecipe(DisplayRegistry registry, RebornRecipeType<R> recipeType) {
		Function<R, Display> recipeDisplay = r -> new MachineRecipeDisplay<>((RebornRecipe) r);

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

		registry.registerFiller(RebornRecipe.class, recipe -> {
			if (recipe instanceof RebornRecipe) {
				return recipe.getRebornRecipeType() == recipeType;
			}
			return false;
		}, recipeDisplay);
	}

	@Override
	public void registerScreens(ScreenRegistry registry) {
		ExclusionZones exclusionZones = registry.exclusionZones();
		exclusionZones.register(GuiBase.class, guiBase -> {
			int height = 0;
			if (guiBase.tryAddUpgrades() && guiBase.be instanceof IUpgradeable upgradeable) {
				if (upgradeable.canBeUpgraded()) {
					height = 80;
				}
			}
			for (GuiTab slot : (List<GuiTab>) guiBase.getTabs()) {
				if (slot.enabled()) {
					height += 24;
				}
			}
			if (height > 0) {
				int width = 20;
				return Collections.singletonList(new Rectangle(guiBase.getGuiLeft() - width, guiBase.getGuiTop() + 8, width, height));
			}
			return Collections.emptyList();
		});
	}

	public static Widget createProgressBar(int x, int y, double animationDuration, GuiBuilder.ProgressDirection direction) {
		return Widgets.createDrawableWidget((helper, matrices, mouseX, mouseY, delta) -> {
			RenderSystem.setShaderTexture(0, GuiBuilder.defaultTextureSheet);
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

	public static Widget createEnergyDisplay(Rectangle bounds, double energy, EntryAnimation animation, Function<TooltipContext, Tooltip> tooltipBuilder) {
		return new EnergyEntryWidget(bounds, animation).entry(
				ClientEntryStacks.of(new AbstractRenderer() {
					@Override
					public void render(MatrixStack matrices, Rectangle bounds, int mouseX, int mouseY, float delta) {}

					@Override
					public @Nullable Tooltip getTooltip(TooltipContext context) {
						return tooltipBuilder.apply(context);
					}
				})
		).notFavoritesInteractable();
	}

	public static Widget createFluidDisplay(Rectangle bounds, EntryStack<FluidStack> fluid, EntryAnimation animation) {
		EntryStack<FluidStack> copy = fluid.copy();
		ClientEntryStacks.setRenderer(copy, new FluidStackRenderer(animation, copy.getRenderer()));
		return Widgets.createSlot(bounds).entry(copy);
	}

	private static class EnergyEntryWidget extends EntryWidget {
		private EntryAnimation animation;

		protected EnergyEntryWidget(Rectangle rectangle, EntryAnimation animation) {
			super(new Point(rectangle.x, rectangle.y));
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
				RenderSystem.setShaderTexture(0, GuiBuilder.defaultTextureSheet);
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
	
	private static class FluidStackRenderer extends AbstractEntryRenderer<FluidStack> {
		private final EntryAnimation animation;
		private final EntryRenderer<FluidStack> parent;

		public FluidStackRenderer(EntryAnimation animation, EntryRenderer<FluidStack> parent) {
			this.animation = animation;
			this.parent = parent;
		}

		@Override
		public void render(EntryStack<FluidStack> entry, MatrixStack matrices, Rectangle bounds, int mouseX, int mouseY, float delta) {
			int width = bounds.width;
			int height = bounds.height;

			PowerSystem.EnergySystem displayPower = PowerSystem.getDisplayPower();
			RenderSystem.setShaderTexture(0, GuiBuilder.defaultTextureSheet);
			drawTexture(matrices, bounds.x - 4, bounds.y - 4, 194, 26, width + 8, height + 8);
			drawTexture(matrices, bounds.x - 1, bounds.y - 1, 194, 82, width + 2, height + 2);
			int innerDisplayHeight;
			if (animation.animationType != EntryAnimationType.NONE) {
				innerDisplayHeight = MathHelper.ceil((System.currentTimeMillis() / (animation.duration / height) % height));
				if (animation.animationType == EntryAnimationType.DOWNWARDS)
					innerDisplayHeight = height - innerDisplayHeight;
			} else innerDisplayHeight = height;
			drawFluid(matrices, entry.getValue().getFluid(), innerDisplayHeight, bounds.x, bounds.y, width, height);
		}

		public void drawFluid(MatrixStack matrixStack, Fluid fluid, int drawHeight, int x, int y, int width, int height) {
			RenderSystem.setShaderTexture(0, PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
			y += height;

			FluidRenderHandler handler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);

			// If registry can't find it, don't render.
			if (handler == null) {
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

				drawSprite(matrixStack, x, y - offsetHeight, 0, width, curHeight, sprite);
				offsetHeight -= curHeight;
				iteration++;
				if (iteration > 50) {
					break;
				}
			}
		}

		@Override
		@Nullable
		public Tooltip getTooltip(EntryStack<FluidStack> entry, TooltipContext context) {
			return parent.getTooltip(entry, context);
		}
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
