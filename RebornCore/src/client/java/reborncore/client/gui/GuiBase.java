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

import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import reborncore.api.blockentity.IUpgradeable;
import reborncore.client.gui.config.GuiTab;
import reborncore.client.gui.widget.GuiButtonHologram;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.slot.PlayerInventorySlot;

import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.model.Material;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;

public class GuiBase<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
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

	public GuiBase(Player player, BlockEntity blockEntity, T screenHandler) {
		super(screenHandler, player.getInventory(), Component.literal(I18n.get(blockEntity.getBlockState().getBlock().getDescriptionId())));
		this.be = blockEntity;
		this.builtScreenHandler = (BuiltScreenHandler) screenHandler;
		tabs = GuiTab.TABS.stream()
			.map(factory -> factory.create(this))
			.filter(GuiTab::enabled)
			.toList();
		theme = ThemeManager.getTheme();
	}

	public int getScreenWidth() {
		return imageWidth;
	}

	public void drawSlot(GuiGraphics drawContext, int x, int y, Layer layer) {
		if (layer == Layer.BACKGROUND) {
			x += this.leftPos;
			y += this.topPos;
		}
		builder.drawSlot(drawContext, x - 1, y - 1);
	}

	public void drawOutputSlotBar(GuiGraphics drawContext, int x, int y, int count, Layer layer) {
		if (layer == Layer.BACKGROUND) {
			x += this.leftPos;
			y += this.topPos;
		}
		builder.drawOutputSlotBar(drawContext, x - 4, y - 4, count);
	}

	public void drawArmourSlots(GuiGraphics drawContext, int x, int y, Layer layer) {
		if (layer == Layer.BACKGROUND) {
			x += this.leftPos;
			y += this.topPos;
		}
		builder.drawSlot(drawContext, x - 1, y - 1);
		builder.drawSlot(drawContext, x - 1, y - 1 + 18);
		builder.drawSlot(drawContext, x - 1, y - 1 + 18 + 18);
		builder.drawSlot(drawContext, x - 1, y - 1 + 18 + 18 + 18);
	}

	public void drawOutputSlot(GuiGraphics drawContext, int x, int y, Layer layer) {
		if (layer == Layer.BACKGROUND) {
			x += this.leftPos;
			y += this.topPos;
		}
		builder.drawOutputSlot(drawContext, x - 5, y - 5);
	}

	@Override
	public void init() {
		super.init();
		for (GuiTab tab : getTabs()) {
			tab.open();
		}
	}

	@Override
	protected void renderBg(GuiGraphics drawContext, float lastFrameDuration, int mouseX, int mouseY) {
		drawContext.blit(RenderPipelines.GUI_TEXTURED, INVENTORY_LOCATION, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
		boolean drawPlayerSlots = selectedTab == null && drawPlayerSlots();
		updateSlotDraw(drawPlayerSlots);
		builder.drawDefaultBackground(drawContext, leftPos, topPos, xSize, ySize);
		if (drawPlayerSlots) {
			builder.drawPlayerSlots(drawContext, this, leftPos + imageWidth / 2, topPos + 93, true);
		}
		if (tryAddUpgrades() && be instanceof IUpgradeable upgradeable) {
			if (upgradeable.canBeUpgraded()) {
				builder.drawUpgrades(drawContext, this, leftPos - 24, topPos + 6);
				upgrades = true;
			}
		}
		int offset = upgrades ? 86 : 6;
		for (GuiTab slot : tabs) {
			if (slot.enabled()) {
				builder.drawSlotTab(drawContext, this, leftPos - 24, topPos + offset, slot.stack());
				offset += 24;
			}
		}

		final GuiBase<T> gui = this;
		getTab().ifPresent(guiTab -> builder.drawSlotConfigTips(drawContext, gui, leftPos + imageWidth / 2, topPos + 93, mouseX, mouseY, guiTab));

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
	protected void renderLabels(GuiGraphics drawContext, int mouseX, int mouseY) {
		drawTitle(drawContext);
	}

	@Override
	public void render(GuiGraphics drawContext, int mouseX, int mouseY, float partialTicks) {
		super.render(drawContext, mouseX, mouseY, partialTicks);
		this.renderTooltip(drawContext, mouseX, mouseY);

		drawContext.pose().pushMatrix();
		drawContext.pose().translate(this.leftPos, this.topPos);
		getTab().ifPresent(guiTab -> guiTab.draw(drawContext, mouseX, mouseY));
		drawContext.pose().popMatrix();
	}

	@Override
	protected void renderTooltip(GuiGraphics drawContext, int mouseX, int mouseY) {
		if (isHovering(-25, 6, 24, 80, mouseX, mouseY) && upgrades
				&& this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
			List<Component> list = new ArrayList<>();
			list.add(Component.translatable("reborncore.gui.tooltip.upgrades"));
			drawContext.setComponentTooltipForNextFrame(Minecraft.getInstance().font, list, mouseX, mouseY);
		}
		int offset = upgrades ? 82 : 0;
		for (GuiTab tab : getTabs()) {
			if (isHovering(-26, 6 + offset, 24, 23, mouseX, mouseY)) {
				drawContext.setComponentTooltipForNextFrame(Minecraft.getInstance().font, Collections.singletonList(Component.translatable(tab.name())), mouseX, mouseY);
			}
			offset += 24;
		}

		for (NarratableEntry selectable : narratables) {
			if (selectable instanceof AbstractWidget clickable) {
				if (clickable.isHovered()) {
					// TODO 1.19.3
					// clickable.renderTooltip(matrixStack, mouseX, mouseY);
					break;
				}
			}

		}
		super.renderTooltip(drawContext, mouseX, mouseY);
	}

	protected void drawTitle(GuiGraphics drawContext) {
		drawCentredText(drawContext, Component.translatable(be.getBlockState().getBlock().getDescriptionId()), 6, theme.titleColor().rgba(), Layer.FOREGROUND);
	}

	public void drawCentredText(GuiGraphics drawContext, Component text, int y, int colour, Layer layer) {
		drawText(drawContext, text, (imageWidth / 2 - getFont().width(text) / 2), y, colour, layer);
	}

	public void drawCentredText(GuiGraphics drawContext, Component text, int y, int colour, int modifier, Layer layer) {
		drawText(drawContext, text, (imageWidth / 2 - (getFont().width(text)) / 2) + modifier, y, colour, layer);
	}

	public void drawText(GuiGraphics drawContext, Component text, int x, int y, int colour, Layer layer) {
		int factorX = 0;
		int factorY = 0;
		if (layer == Layer.BACKGROUND) {
			factorX = this.leftPos;
			factorY = this.topPos;
		}
		drawContext.drawString(Minecraft.getInstance().font, text, x + factorX, y + factorY, colour, false);
	}

	public GuiButtonHologram addHologramButton(int x, int y, int id, Layer layer) {
		GuiButtonHologram buttonHologram = new GuiButtonHologram(x + this.leftPos, y + this.topPos, var1 -> {
		});
		addWidget(buttonHologram);
		return buttonHologram;
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent mouse, boolean doubled) {
		if (getTab().map(guiTab -> guiTab.click(mouse.x(), mouse.y(), mouse.button())).orElse(false)) {
			return true;
		}
		return super.mouseClicked(mouse, doubled);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent mouse) {
		getTab().ifPresent(guiTab -> guiTab.mouseReleased(mouse.x(), mouse.y(), mouse.button()));
		int offset = 0;
		if (!upgrades) {
			offset = 80;
		}
		for (GuiTab tab : getTabs()) {
			if (isHovering(-26, 84 - offset, 30, 23, mouse.x(), mouse.y())) {
				if (selectedTab == tab) {
					closeSelectedTab();
				} else {
					setSelectedTab(tab);
				}
				break;
			}
			offset -= 24;
		}

		return super.mouseReleased(mouse);
	}

	@Override
	public boolean keyPressed(KeyEvent key) {
		if (getTab().map(guiTab -> guiTab.keyPress(key)).orElse(false)) {
			return true;
		}
		if (selectedTab != null && key.key() == GLFW.GLFW_KEY_ESCAPE) {
			closeSelectedTab();
			return true;
		}
		return super.keyPressed(key);
	}

	@Override
	public void onClose() {
		closeSelectedTab();
		super.onClose();
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
		return super.isHovering(rectX, rectY, rectWidth, rectHeight, pointX, pointY);
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
		return leftPos;
	}

	public int getGuiTop() {
		return topPos;
	}

	public Minecraft getMinecraft() {
		// Just to stop complaints from IDEA
		if (minecraft == null) {
			throw new NullPointerException("Minecraft client is null.");
		}
		return this.minecraft;
	}

	public Font getFont() {
		return this.font;
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
	protected boolean hasClickedOutside(double mouseX, double mouseY, int left, int top) {
		// Upgrades are normally outside the bounds, so let's pretend we are within the bounds if there is a slot here.
		return getHoveredSlot(mouseX, mouseY) == null && super.hasClickedOutside(mouseX, mouseY, left, top);
	}

	public List<GuiTab> getTabs() {
		return tabs;
	}

	public static TextureAtlasSprite getSprite(Material spriteIdentifier) {
		return Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(spriteIdentifier.atlasLocation()).getSprite(spriteIdentifier.texture());
	}
}
