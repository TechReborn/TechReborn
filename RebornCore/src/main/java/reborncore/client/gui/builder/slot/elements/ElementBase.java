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

package reborncore.client.gui.builder.slot.elements;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import reborncore.client.RenderUtil;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.common.blockentity.MachineBaseBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class ElementBase {

	public int x;
	public int y;
	public boolean isHovering = false;
	public boolean isDragging = false;
	public boolean isPressing = false;
	public boolean isReleasing = false;
	public boolean startPressLast = false;
	public boolean isHoveringLast = false;
	public boolean isDraggingLast = false;
	public boolean isPressingLast = false;
	public boolean isReleasingLast = false;
	public List<ElementBase.Action> hoverActions = new ArrayList<>();
	public List<ElementBase.Action> dragActions = new ArrayList<>();
	public List<ElementBase.Action> startPressActions = new ArrayList<>();
	public List<ElementBase.Action> pressActions = new ArrayList<>();
	public List<ElementBase.Action> releaseActions = new ArrayList<>();
	public SpriteContainer container;
	public List<UpdateAction> updateActions = new ArrayList<>();
	public List<UpdateAction> buttonUpdate = new ArrayList<>();
	private int width;
	private int height;

	public static final Identifier MECH_ELEMENTS = new Identifier("reborncore", "textures/gui/elements.png");

	public ElementBase(int x, int y, SpriteContainer container) {
		this.container = container;
		this.x = x;
		this.y = y;
	}

	public ElementBase(int x, int y, ISprite... sprites) {
		this.container = new SpriteContainer();
		for (ISprite sprite : sprites) {
			container.addSprite(sprite);
		}
		this.x = x;
		this.y = y;
	}

	public ElementBase(int x, int y, int width, int height) {
		this.container = new SpriteContainer();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public ElementBase(int x, int y, int width, int height, SpriteContainer container) {
		this.container = container;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public ElementBase(int x, int y, int width, int height, ISprite... sprites) {
		this.container = new SpriteContainer();
		for (ISprite sprite : sprites) {
			container.addSprite(sprite);
		}
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public SpriteContainer getSpriteContainer() {
		return container;
	}

	public void adjustDimensions(MachineBaseBlockEntity provider) {
		if (container.offsetSprites != null) {
			for (OffsetSprite offsetSprite : container.offsetSprites) {
				if (offsetSprite.getSprite().getSprite(provider).width + offsetSprite.getOffsetX(provider) > this.width) {
					this.width = offsetSprite.getSprite().getSprite(provider).width + offsetSprite.getOffsetX(provider);
				}
				if (offsetSprite.getSprite().getSprite(provider).height + offsetSprite.getOffsetY(provider) > this.height) {
					this.height = offsetSprite.getSprite().getSprite(provider).height + offsetSprite.getOffsetY(provider);
				}
			}
		}
	}

	public void draw(MatrixStack matrixStack, GuiBase<?> gui) {
		for (OffsetSprite sprite : getSpriteContainer().offsetSprites) {
			drawSprite(matrixStack, gui, sprite.getSprite(), x + sprite.getOffsetX(gui.getMachine()), y + sprite.getOffsetY(gui.getMachine()));
		}
	}

	public void renderUpdate(GuiBase<?> gui) {
		isHoveringLast = isHovering;
		isPressingLast = isPressing;
		isDraggingLast = isDragging;
		isReleasingLast = isReleasing;
	}

	public void update(GuiBase<?> gui) {
		for (UpdateAction action : updateActions) {
			action.update(gui, this);
		}
	}

	public ElementBase addUpdateAction(UpdateAction action) {
		updateActions.add(action);
		return this;
	}

	public ElementBase setWidth(int width) {
		this.width = width;
		return this;
	}

	public ElementBase setHeight(int height) {
		this.height = height;
		return this;
	}

	public int getX() {
		return x;
	}

	public ElementBase setX(int x) {
		this.x = x;
		return this;
	}

	public int getY() {
		return y;
	}

	public ElementBase setY(int y) {
		this.y = y;
		return this;
	}

	public int getWidth(MachineBaseBlockEntity provider) {
		adjustDimensions(provider);
		return width;
	}

	public int getHeight(MachineBaseBlockEntity provider) {
		adjustDimensions(provider);
		return height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ElementBase addHoverAction(ElementBase.Action action) {
		this.hoverActions.add(action);
		return this;
	}

	public ElementBase addDragAction(ElementBase.Action action) {
		this.dragActions.add(action);
		return this;
	}

	public ElementBase addStartPressAction(ElementBase.Action action) {
		this.startPressActions.add(action);
		return this;
	}

	public ElementBase addPressAction(ElementBase.Action action) {
		this.pressActions.add(action);
		return this;
	}

	public ElementBase addReleaseAction(ElementBase.Action action) {
		this.releaseActions.add(action);
		return this;
	}

	public boolean onHover(MachineBaseBlockEntity provider, GuiBase<?> gui, double mouseX, double mouseY) {
		for (ElementBase.Action action : hoverActions) {
			action.execute(this, gui, provider, mouseX, mouseY);
		}
		return !hoverActions.isEmpty();
	}

	public boolean onDrag(MachineBaseBlockEntity provider, GuiBase<?> gui, double mouseX, double mouseY) {
		for (ElementBase.Action action : dragActions) {
			action.execute(this, gui, provider, mouseX, mouseY);
		}
		return !dragActions.isEmpty();
	}

	public boolean onStartPress(MachineBaseBlockEntity provider, GuiBase<?> gui, double mouseX, double mouseY) {
		for (ElementBase.Action action : startPressActions) {
			action.execute(this, gui, provider, mouseX, mouseY);
		}
		return !startPressActions.isEmpty();
	}

	public boolean onRelease(MachineBaseBlockEntity provider, GuiBase<?> gui, double mouseX, double mouseY) {
		for (ElementBase.Action action : releaseActions) {
			if (action.execute(this, gui, provider, mouseX, mouseY)) {
				return true;
			}
		}
		if (isPressing) {
			for (ElementBase.Action action : pressActions) {
				action.execute(this, gui, provider, mouseX, mouseY);
			}
		}
		return !releaseActions.isEmpty() || !pressActions.isEmpty();
	}

	public interface Action {
		boolean execute(ElementBase element, GuiBase<?> gui, MachineBaseBlockEntity provider, double mouseX, double mouseY);
	}

	public interface UpdateAction {
		void update(GuiBase<?> gui, ElementBase element);
	}

	public void drawRect(GuiBase<?> gui, int x, int y, int width, int height, int colour) {
		drawGradientRect(gui, x, y, width, height, colour, colour);
	}

	/*
		Taken from Gui
	*/
	public void drawGradientRect(GuiBase<?> gui, int x, int y, int width, int height, int startColor, int endColor) {
		x = adjustX(gui, x);
		y = adjustY(gui, y);

		int left = x;
		int top = y;
		int right = x + width;
		int bottom = y + height;

		RenderUtil.drawGradientRect(0, left, top, right, bottom, startColor, endColor);
	}

	public int adjustX(GuiBase<?> gui, int x) {
		return gui.getGuiLeft() + x;
	}

	public int adjustY(GuiBase<?> gui, int y) {
		return gui.getGuiTop() + y;
	}

	public boolean isInRect(GuiBase<?> gui, int x, int y, int xSize, int ySize, double mouseX, double mouseY) {
		return gui.isPointInRect(x + gui.getGuiLeft(), y + gui.getGuiTop(), xSize, ySize, mouseX, mouseY);
	}

	public void drawText(MatrixStack matrixStack, GuiBase<?> gui, Text text, int x, int y, int color) {
		x = adjustX(gui, x);
		y = adjustY(gui, y);
		gui.getTextRenderer().draw(matrixStack, text, x, y, color);
	}

	public void setTextureSheet(Identifier textureLocation) {
		MinecraftClient.getInstance().getTextureManager().bindTexture(textureLocation);
	}

	public void drawSprite(MatrixStack matrixStack, GuiBase<?> gui, ISprite iSprite, int x, int y) {
		Sprite sprite = iSprite.getSprite(gui.getMachine());
		if (sprite != null) {
			if (sprite.hasTextureInfo()) {
				RenderSystem.color3f(1F, 1F, 1F);
				setTextureSheet(sprite.textureLocation);
				gui.drawTexture(matrixStack, x + gui.getGuiLeft(), y + gui.getGuiTop(), sprite.x, sprite.y, sprite.width, sprite.height);
			}
			if (sprite.hasStack()) {
				RenderSystem.pushMatrix();
				RenderSystem.enableBlend();
				RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);

				ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
				itemRenderer.renderInGuiWithOverrides(sprite.itemStack, x + gui.getGuiLeft(), y + gui.getGuiTop());

				RenderSystem.disableLighting();
				RenderSystem.popMatrix();
			}
		}
	}

	public int getScaledBurnTime(int scale, int burnTime, int totalBurnTime) {
		return (int) (((float) burnTime / (float) totalBurnTime) * scale);
	}

	public int getPercentage(int MaxValue, int CurrentValue) {
		if (CurrentValue == 0) {
			return 0;
		}
		return (int) ((CurrentValue * 100.0f) / MaxValue);
	}

	public void drawDefaultBackground(MatrixStack matrixStack, Screen gui, int x, int y, int width, int height) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		MinecraftClient.getInstance().getTextureManager().bindTexture(GuiBuilder.defaultTextureSheet);
		gui.drawTexture(matrixStack, x, y, 0, 0, width / 2, height / 2);
		gui.drawTexture(matrixStack, x + width / 2, y, 150 - width / 2, 0, width / 2, height / 2);
		gui.drawTexture(matrixStack, x, y + height / 2, 0, 150 - height / 2, width / 2, height / 2);
		gui.drawTexture(matrixStack, x + width / 2, y + height / 2, 150 - width / 2, 150 - height / 2, width / 2, height / 2);
	}
}
