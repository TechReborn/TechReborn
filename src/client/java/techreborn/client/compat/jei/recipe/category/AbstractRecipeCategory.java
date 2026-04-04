package techreborn.client.compat.jei.recipe.category;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import reborncore.client.gui.GuiSprites;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.client.compat.jei.JEIPlugin;
import techreborn.client.compat.jei.gui.render.SpriteDrawable;

public abstract class AbstractRecipeCategory<R> implements IRecipeCategory<R> {

	public static final NumberFormat TIME_FORMAT = new DecimalFormat("###.##");

	public static final SpriteDrawable SLOT = new SpriteDrawable(GuiSprites.SLOT, 18, 18);
	public static final SpriteDrawable TANK_BACKGROUND = new SpriteDrawable(GuiSprites.TANK_BACKGROUND, 22, 56);
	public static final SpriteDrawable TANK_FOREGROUND = new SpriteDrawable(GuiSprites.TANK_FOREGROUND, 16, 50);

	public final IRecipeType<R> recipeType;
	public final Component title;

	public AbstractRecipeCategory(IRecipeType<R> recipeType, Component title) {
		this.recipeType = recipeType;
		this.title = title;
	}

	public AbstractRecipeCategory(IRecipeType<R> recipeType) {
		this.recipeType = recipeType;
		this.title = Component.translatable(recipeType.getUid().toString());
	}

	@Override
	public IRecipeType<R> getRecipeType() {
		return recipeType;
	}

	@Override
	public Component getTitle() {
		return title;
	}

	@Override
	public int getWidth() {
		return 140;
	}

	@Override
	public int getHeight() {
		return 56;
	}

	@Override
	public IDrawable getIcon() {
		return null;
	}

	public Font font() {
		return Minecraft.getInstance().font;
	}

	public IJeiHelpers jeiHelpers() {
		return JEIPlugin.jeiHelpers;
	}

	public IPlatformFluidHelper<?> fluidHelper() {
		return jeiHelpers().getPlatformFluidHelper();
	}

	public IRecipeSlotBuilder addItem(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, IDrawable background) {
		return builder.addSlot(ingredientRole, x, y).setBackground(background, 8 - background.getWidth() / 2, 8 - background.getHeight() / 2);
	}

	public IRecipeSlotBuilder addItem(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, List<ItemStack> itemStacks, IDrawable background) {
		return addItem(builder, ingredientRole, x, y, background).addItemStacks(itemStacks);
	}

	public IRecipeSlotBuilder addItem(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, ItemStack itemStack, IDrawable background) {
		return addItem(builder, ingredientRole, x, y, background).add(itemStack);
	}

	public IRecipeSlotBuilder addFluid(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, FluidInstance fluidInstance) {
		Fluid fluid = fluidInstance.fluid();
		long amount = fluidInstance.amount().rawValue() / (FluidConstants.BUCKET / fluidHelper().bucketVolume());
		long fraction = fluidInstance.getAmount().rawValue() % (FluidConstants.BUCKET / 1000);
		DataComponentPatch data = fluidInstance.fluidVariant().getComponentsPatch();
		IRecipeSlotBuilder slot = builder.addSlot(ingredientRole, x, y)
			.setBackground(TANK_BACKGROUND, -3, -3)
			.setOverlay(TANK_FOREGROUND, 0, 0)
			.setFluidRenderer(Math.max(amount, 1), false, 16, 50);
			//.addRichTooltipCallback(JEIDrawables.appendFraction(fraction));
		if(!fluidInstance.isEmpty()) {
			slot.add(fluid, amount, data);
		}
		return slot;
	}
}
