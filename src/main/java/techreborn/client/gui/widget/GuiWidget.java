package techreborn.client.gui.widget;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.ArrayList;

public abstract class GuiWidget<T extends Container> extends GuiContainer {

	public static final LanguageMap translate = ObfuscationReflectionHelper.getPrivateValue(LanguageMap.class, null, 2);

	private final ArrayList<Widget> widgets = new ArrayList<>();
	private final ResourceLocation background;

	public GuiWidget(T inventorySlotsIn, ResourceLocation background, int xSize, int ySize) {
		super(inventorySlotsIn);
		this.xSize = xSize;
		this.ySize = ySize;
		this.background = background;
	}

	public T getContainer() {
		return (T) inventorySlots;
	}

	@Override
	public void initGui() {
		super.initGui();
		widgets.clear();
		initWidgets();
	}

	public void addWidget(Widget widget) {
		widgets.add(widget);
	}

	public void removeWidget(Widget widget) {
		widgets.remove(widget);
	}

	public abstract void initWidgets();

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(background);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		String name = translate.translateKey("tile.techreborn.industrialgrinder.name");

		fontRendererObj.drawString(name, xSize / 2 - fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		fontRendererObj.drawString(translate.translateKey("container.inventory"), 8, ySize - 94, 4210752);

		for (Widget widget : widgets)
			widget.drawWidget(this, x, y, mouseX, mouseY);
	}

	public FontRenderer getFontRenderer() {
		return fontRendererObj;
	}

}
