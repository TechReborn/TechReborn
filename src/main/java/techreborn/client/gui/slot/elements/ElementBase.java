/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.client.gui.slot.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.common.tile.TileLegacyMachineBase;
import techreborn.client.gui.GuiBase;
import techreborn.lib.ModInfo;

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

	public static final ResourceLocation MECH_ELEMENTS = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/elements.png");

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

	public void adjustDimensions(TileLegacyMachineBase provider) {
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

	public void draw(GuiBase gui) {
		for (OffsetSprite sprite : getSpriteContainer().offsetSprites) {
			drawSprite(gui, sprite.getSprite(), x + sprite.getOffsetX(gui.getMachine()), y + sprite.getOffsetY(gui.getMachine()));
		}
	}

	public void renderUpdate(GuiBase gui) {
		isHoveringLast = isHovering;
		isPressingLast = isPressing;
		isDraggingLast = isDragging;
		isReleasingLast = isReleasing;
	}

	public void update(GuiBase gui) {
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

	public int getWidth(TileLegacyMachineBase provider) {
		adjustDimensions(provider);
		return width;
	}

	public int getHeight(TileLegacyMachineBase provider) {
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

	public boolean onHover(TileLegacyMachineBase provider, GuiBase gui, int mouseX, int mouseY) {
		for (ElementBase.Action action : hoverActions) {
			action.execute(this, gui, provider, mouseX, mouseY);
		}
		return !hoverActions.isEmpty();
	}

	public boolean onDrag(TileLegacyMachineBase provider, GuiBase gui, int mouseX, int mouseY) {
		for (ElementBase.Action action : dragActions) {
			action.execute(this, gui, provider, mouseX, mouseY);
		}
		return !dragActions.isEmpty();
	}

	public boolean onStartPress(TileLegacyMachineBase provider, GuiBase gui, int mouseX, int mouseY) {
		for (ElementBase.Action action : startPressActions) {
			action.execute(this, gui, provider, mouseX, mouseY);
		}
		return !startPressActions.isEmpty();
	}

	public boolean onRelease(TileLegacyMachineBase provider, GuiBase gui, int mouseX, int mouseY) {
		for (ElementBase.Action action : releaseActions) {
			if(action.execute(this, gui, provider, mouseX, mouseY)){
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
		boolean execute(ElementBase element, GuiBase gui, TileLegacyMachineBase provider, int mouseX, int mouseY);
	}

	public interface UpdateAction {
		void update(GuiBase gui, ElementBase element);
	}

	public void drawRect(GuiBase gui, int x, int y, int width, int height, int colour) {
		drawGradientRect(gui, x, y, width, height, colour, colour);
	}

	/*
		Taken from Gui
	*/
	public void drawGradientRect(GuiBase gui, int x, int y, int width, int height, int startColor, int endColor) {
		x = adjustX(gui, x);
		y = adjustY(gui, y);

		int left = x;
		int top = y;
		int right = x + width;
		int bottom = y + height;
		float f = (float) (startColor >> 24 & 255) / 255.0F;
		float f1 = (float) (startColor >> 16 & 255) / 255.0F;
		float f2 = (float) (startColor >> 8 & 255) / 255.0F;
		float f3 = (float) (startColor & 255) / 255.0F;
		float f4 = (float) (endColor >> 24 & 255) / 255.0F;
		float f5 = (float) (endColor >> 16 & 255) / 255.0F;
		float f6 = (float) (endColor >> 8 & 255) / 255.0F;
		float f7 = (float) (endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		vertexbuffer.pos((double) right, (double) top, (double) 0).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos((double) left, (double) top, (double) 0).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos((double) left, (double) bottom, (double) 0).color(f5, f6, f7, f4).endVertex();
		vertexbuffer.pos((double) right, (double) bottom, (double) 0).color(f5, f6, f7, f4).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}

	public int adjustX(GuiBase gui, int x) {
		return gui.guiLeft + x;
	}

	public int adjustY(GuiBase gui, int y) {
		return gui.guiTop + y;
	}

	public boolean isInRect(GuiBase gui, int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
		return gui.isPointInRect(x + gui.guiLeft, y + gui.guiTop, xSize, ySize, mouseX, mouseY);
	}

	public void drawString(GuiBase gui, String string, int x, int y, int color) {
		x = adjustX(gui, x);
		y = adjustY(gui, y);
		gui.mc.fontRenderer.drawString(string, x, y, color);
	}

	public void drawString(GuiBase gui, String string, int x, int y) {
		drawString(gui, string, x, y, 16777215);
	}

	public void setTextureSheet(ResourceLocation textureLocation) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(textureLocation);
	}

	public void drawCenteredString(GuiBase gui, String string, int y, int colour) {
		drawString(gui, string, (gui.getXSize() / 2 - gui.mc.fontRenderer.getStringWidth(string) / 2), y, colour);
	}

	public void drawCenteredString(GuiBase gui, String string, int x, int y, int colour) {
		drawString(gui, string, (x - gui.mc.fontRenderer.getStringWidth(string) / 2), y, colour);
	}

	public int getStringWidth(String string) {
		return Minecraft.getMinecraft().fontRenderer.getStringWidth(string);
	}

	public void drawSprite(GuiBase gui, ISprite iSprite, int x, int y) {
		Sprite sprite = iSprite.getSprite(gui.getMachine());
		if (sprite != null) {
			if (sprite.hasTextureInfo()) {
				GlStateManager.color(1F, 1F, 1F);
				setTextureSheet(sprite.textureLocation);
				gui.drawTexturedModalRect(x + gui.guiLeft, y + gui.guiTop, sprite.x, sprite.y, sprite.width, sprite.height);
			}
			if (sprite.hasStack()) {
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				RenderHelper.enableGUIStandardItemLighting();

				RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
				itemRenderer.renderItemAndEffectIntoGUI(sprite.itemStack, x + gui.guiLeft, y + gui.guiTop);

				GlStateManager.disableLighting();
				GlStateManager.popMatrix();
			}
		}
	}

	public int getScaledBurnTime(int scale, int burnTime, int totalBurnTime) {
		return (int) (((float) burnTime / (float) totalBurnTime) * scale);
	}

	public TextFormatting getPercentageColour(int percentage) {
		if (percentage <= 10) {
			return TextFormatting.RED;
		} else if (percentage >= 75) {
			return TextFormatting.GREEN;
		} else {
			return TextFormatting.YELLOW;
		}
	}

	public int getPercentage(int MaxValue, int CurrentValue) {
		if (CurrentValue == 0)
			return 0;
		return (int) ((CurrentValue * 100.0f) / MaxValue);
	}

	public void drawDefaultBackground(GuiScreen gui, int x, int y, int width, int height) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(GuiBuilder.defaultTextureSheet);
		gui.drawTexturedModalRect(x, y, 0, 0, width / 2, height / 2);
		gui.drawTexturedModalRect(x + width / 2, y, 150 - width / 2, 0, width / 2, height / 2);
		gui.drawTexturedModalRect(x, y + height / 2, 0, 150 - height / 2, width / 2, height / 2);
		gui.drawTexturedModalRect(x + width / 2, y + height / 2, 150 - width / 2, 150 - height / 2, width / 2, height / 2);
	}
}
