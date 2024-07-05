/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.client.gui;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import reborncore.api.blockentity.IUpgradeable;
import reborncore.client.gui.config.GuiTab;
import reborncore.client.gui.widget.GuiButtonHologram;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.slot.PlayerInventorySlot;

import java.util.*;

public class GuiBase<T extends ScreenHandler> extends HandledScreen<T> {
	public static FluidCellProvider fluidCellProvider = fluid -> ItemStack.EMPTY;
	public static ItemStack wrenchStack = ItemStack.EMPTY;

	public GuiBuilder builder = new GuiBuilder();
	public BlockEntity be;
	@Nullable
	public BuiltScreenHandler builtScreenHandler;
	private final int xSize = 176;
	private final int ySize = 176;

	@Nullable
	private GuiTab selectedTab = null;
	private final List<GuiTab> tabs;
	protected final Theme theme;

	public boolean upgrades;

	public GuiBase(PlayerEntity player, BlockEntity blockEntity, T screenHandler) {
		super(screenHandler, player.getInventory(), Text.literal(I18n.translate(blockEntity.getCachedState().getBlock().getTranslationKey())));
		this.be = blockEntity;
		this.builtScreenHandler = (BuiltScreenHandler) screenHandler;
		tabs = GuiTab.TABS.stream()
			.map(factory -> factory.create(this))
			.filter(GuiTab::enabled)
			.toList();
		theme = ThemeManager.getTheme();
	}

	public int getScreenWidth() {
		return backgroundWidth;
	}

	public void drawSlot(DrawContext drawContext, int x, int y, Layer layer) {
		if (layer == Layer.BACKGROUND) {
			x += this.x;
			y += this.y;
		}
		builder.drawSlot(drawContext, x - 1, y - 1);
	}

	public void drawOutputSlotBar(DrawContext drawContext, int x, int y, int count, Layer layer) {
		if (layer == Layer.BACKGROUND) {
			x += this.x;
			y += this.y;
		}
		builder.drawOutputSlotBar(drawContext, x - 4, y - 4, count);
	}

	public void drawArmourSlots(DrawContext drawContext, int x, int y, Layer layer) {
		if (layer == Layer.BACKGROUND) {
			x += this.x;
			y += this.y;
		}
		builder.drawSlot(drawContext, x - 1, y - 1);
		builder.drawSlot(drawContext, x - 1, y - 1 + 18);
		builder.drawSlot(drawContext, x - 1, y - 1 + 18 + 18);
		builder.drawSlot(drawContext, x - 1, y - 1 + 18 + 18 + 18);
	}

	public void drawOutputSlot(DrawContext drawContext, int x, int y, Layer layer) {
		if (layer == Layer.BACKGROUND) {
			x += this.x;
			y += this.y;
		}
		builder.drawOutputSlot(drawContext, x - 5, y - 5);
	}

	@Override
	public void init() {
		super.init();
		for (GuiTab tab : tabs) {
			tab.open();
		}
	}

	@Override
	protected void drawBackground(DrawContext drawContext, float lastFrameDuration, int mouseX, int mouseY) {
		drawContext.drawTexture(BACKGROUND_TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
		boolean drawPlayerSlots = selectedTab == null && drawPlayerSlots();
		updateSlotDraw(drawPlayerSlots);
		builder.drawDefaultBackground(drawContext, x, y, xSize, ySize);
		if (drawPlayerSlots) {
			builder.drawPlayerSlots(drawContext, this, x + backgroundWidth / 2, y + 93, true);
		}
		if (tryAddUpgrades() && be instanceof IUpgradeable upgradeable) {
			if (upgradeable.canBeUpgraded()) {
				builder.drawUpgrades(drawContext, this, x - 24, y + 6);
				upgrades = true;
			}
		}
		int offset = upgrades ? 86 : 6;
		for (GuiTab slot : tabs) {
			if (slot.enabled()) {
				builder.drawSlotTab(drawContext, this, x - 24, y + offset, slot.stack());
				offset += 24;
			}
		}

		final GuiBase<T> gui = this;
		getTab().ifPresent(guiTab -> builder.drawSlotConfigTips(drawContext, gui, x + backgroundWidth / 2, y + 93, mouseX, mouseY, guiTab));

	}

	private void updateSlotDraw(boolean doDraw) {
		if (builtScreenHandler == null) {
			return;
		}
		for (Slot slot : builtScreenHandler.slots) {
			if (slot instanceof PlayerInventorySlot) {
				((PlayerInventorySlot) slot).doDraw = doDraw;
			}
		}
	}

	public boolean drawPlayerSlots() {
		return true;
	}

	public boolean tryAddUpgrades() {
		return true;
	}

	@Override
	protected void drawForeground(DrawContext drawContext, int mouseX, int mouseY) {
		drawTitle(drawContext);
	}

	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
		super.render(drawContext, mouseX, mouseY, partialTicks);
		this.drawMouseoverTooltip(drawContext, mouseX, mouseY);

		drawContext.getMatrices().push();
		drawContext.getMatrices().translate(this.x, this.y, 900);
		getTab().ifPresent(guiTab -> guiTab.draw(drawContext, mouseX, mouseY));
		drawContext.getMatrices().pop();
	}

	@Override
	protected void drawMouseoverTooltip(DrawContext drawContext, int mouseX, int mouseY) {
		if (isPointWithinBounds(-25, 6, 24, 80, mouseX, mouseY) && upgrades
				&& this.focusedSlot != null && !this.focusedSlot.hasStack()) {
			List<Text> list = new ArrayList<>();
			list.add(Text.translatable("reborncore.gui.tooltip.upgrades"));
			drawContext.drawTooltip(MinecraftClient.getInstance().textRenderer, list, mouseX, mouseY);
		}
		int offset = upgrades ? 82 : 0;
		for (GuiTab tab : tabs) {
			if (isPointWithinBounds(-26, 6 + offset, 24, 23, mouseX, mouseY)) {
				drawContext.drawTooltip(MinecraftClient.getInstance().textRenderer, Collections.singletonList(Text.translatable(tab.name())), mouseX, mouseY);
			}
			offset += 24;
		}

		for (Selectable selectable : selectables) {
			if (selectable instanceof ClickableWidget clickable) {
				if (clickable.isHovered()) {
					// TODO 1.19.3
					// clickable.renderTooltip(matrixStack, mouseX, mouseY);
					break;
				}
			}

		}
		super.drawMouseoverTooltip(drawContext, mouseX, mouseY);
	}

	protected void drawTitle(DrawContext drawContext) {
		drawCentredText(drawContext, Text.translatable(be.getCachedState().getBlock().getTranslationKey()), 6, theme.titleColor().rgba(), Layer.FOREGROUND);
	}

	public void drawCentredText(DrawContext drawContext, Text text, int y, int colour, Layer layer) {
		drawText(drawContext, text, (backgroundWidth / 2 - getTextRenderer().getWidth(text) / 2), y, colour, layer);
	}

	public void drawCentredText(DrawContext drawContext, Text text, int y, int colour, int modifier, Layer layer) {
		drawText(drawContext, text, (backgroundWidth / 2 - (getTextRenderer().getWidth(text)) / 2) + modifier, y, colour, layer);
	}

	public void drawText(DrawContext drawContext, Text text, int x, int y, int colour, Layer layer) {
		int factorX = 0;
		int factorY = 0;
		if (layer == Layer.BACKGROUND) {
			factorX = this.x;
			factorY = this.y;
		}
		drawContext.drawText(MinecraftClient.getInstance().textRenderer, text, x + factorX, y + factorY, colour, false);
	}

	public GuiButtonHologram addHologramButton(int x, int y, int id, Layer layer) {
		GuiButtonHologram buttonHologram = new GuiButtonHologram(x + this.x, y + this.y, var1 -> {
		});
		addSelectableChild(buttonHologram);
		return buttonHologram;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (getTab().map(guiTab -> guiTab.click(mouseX, mouseY, mouseButton)).orElse(false)) {
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int state) {
		int offset = 0;
		if (!upgrades) {
			offset = 80;
		}
		for (GuiTab tab : tabs) {
			if (isPointWithinBounds(-26, 84 - offset, 30, 23, mouseX, mouseY)) {
				if (selectedTab == tab) {
					closeSelectedTab();
				} else {
					setSelectedTab(tab);
				}
				break;
			}
			offset -= 24;
		}

		return super.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (getTab().map(guiTab -> guiTab.keyPress(keyCode, scanCode, modifiers)).orElse(false)) {
			return true;
		}
		if (selectedTab != null && keyCode == GLFW.GLFW_KEY_ESCAPE) {
			closeSelectedTab();
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public void close() {
		closeSelectedTab();
		super.close();
	}

	public MachineBaseBlockEntity getMachine() {
		return (MachineBaseBlockEntity) be;
	}

	/**
	 * @param rectX      {@code int} Top left corner of region
	 * @param rectY      {@code int} Top left corner of region
	 * @param rectWidth  {@code int} Width of region
	 * @param rectHeight {@code int} Height of region
	 * @param pointX     {@code int} Mouse pointer
	 * @param pointY     {@code int} Mouse pointer
	 * @return {@code boolean} Returns true if mouse pointer is in region specified
	 */
	public boolean isPointInRect(int rectX, int rectY, int rectWidth, int rectHeight, double pointX, double pointY) {
		return super.isPointWithinBounds(rectX, rectY, rectWidth, rectHeight, pointX, pointY);
	}

	public enum Layer {
		BACKGROUND, FOREGROUND
	}

	public interface FluidCellProvider {
		ItemStack provide(Fluid fluid);
	}

	public boolean isConfigEnabled() {
		return be instanceof MachineBaseBlockEntity && builtScreenHandler != null;
	}

	public int getGuiLeft() {
		return x;
	}

	public int getGuiTop() {
		return y;
	}

	public MinecraftClient getMinecraft() {
		// Just to stop complaints from IDEA
		if (client == null) {
			throw new NullPointerException("Minecraft client is null.");
		}
		return this.client;
	}

	public TextRenderer getTextRenderer() {
		return this.textRenderer;
	}

	public Optional<GuiTab> getTab() {
		if (!isConfigEnabled()) {
			return Optional.empty();
		}
		return Optional.ofNullable(selectedTab);
	}

	public boolean isTabOpen() {
		return selectedTab != null;
	}

	public boolean hideGuiElements() {
		return selectedTab != null && selectedTab.hideGuiElements();
	}

	private void setSelectedTab(GuiTab tab) {
		Objects.requireNonNull(tab);
		selectedTab = tab;
		selectedTab.open();
	}

	public void closeSelectedTab() {
		if (selectedTab != null) {
			selectedTab.close();
		}

		selectedTab = null;
	}

	public GuiTab getSelectedTab() {
		return selectedTab;
	}

	@Override
	protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int mouseButton) {
		// Upgrades are normally outside the bounds, so let's pretend we are within the bounds if there is a slot here.
		return getSlotAt(mouseX, mouseY) == null && super.isClickOutsideBounds(mouseX, mouseY, left, top, mouseButton);
	}

	public List<GuiTab> getTabs() {
		return tabs;
	}

	public static Sprite getSprite(SpriteIdentifier spriteIdentifier) {
		return MinecraftClient.getInstance().getGuiAtlasManager().getSprite(spriteIdentifier.getTextureId());
	}
}
