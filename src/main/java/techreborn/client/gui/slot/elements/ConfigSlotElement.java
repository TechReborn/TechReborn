package techreborn.client.gui.slot.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import reborncore.common.tile.SlotConfiguration;
import techreborn.client.gui.GuiBase;
import techreborn.client.gui.slot.GuiSlotConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ConfigSlotElement extends ElementBase {
	SlotType type;
	IInventory inventory;
	int id;
	public List<ElementBase> elements = new ArrayList<>();


	public ConfigSlotElement(IInventory slotInventory, int slotId, SlotType type, int x, int y, GuiBase gui) {
		super(x, y, type.getButtonSprite());
		this.type = type;
		this.inventory = slotInventory;
		this.id = slotId;

		SlotConfigPopupElement popupElement;

		elements.add(popupElement = new SlotConfigPopupElement(this.id, x - 22, y - 22, this));
		elements.add(new ButtonElement(x + 29, y - 25, Sprite.EXIT_BUTTON).addReleaseAction((element, gui1, provider, mouseX, mouseY) -> {
			GuiSlotConfiguration.slectedSlot = -1;
			return true;
		}));

		SlotConfiguration.SlotConfigHolder slotConfigHolder = gui.getMachine().slotConfiguration.getSlotDetails(id);

		elements.add(new CheckBoxElement("Auto Input", 0xFFFFFFFF, x - 26, y + 42, "input", slotId, Sprite.LIGHT_CHECK_BOX, gui.getMachine()).addPressAction((element, gui12, provider, mouseX, mouseY) -> {
			popupElement.updateCheckBox((CheckBoxElement) element, "input", gui12);
			return true;
		}));
		elements.add(new CheckBoxElement("Auto Output", 0xFFFFFFFF, x - 26, y + 57,"output", slotId, Sprite.LIGHT_CHECK_BOX, gui.getMachine()).addPressAction((element, gui13, provider, mouseX, mouseY) -> {
			popupElement.updateCheckBox((CheckBoxElement) element, "output", gui13);
			return true;
		}));
	}

	@Override
	public void draw(GuiBase gui) {
		super.draw(gui);
		ItemStack stack = inventory.getStackInSlot(id);
		int xPos = x + 1 + gui.guiLeft;
		int yPos = y + 1 + gui.guiTop;

		GlStateManager.enableDepth();
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableGUIStandardItemLighting();
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.renderItemAndEffectIntoGUI(gui.mc.player, stack, xPos, yPos);
		renderItem.renderItemOverlayIntoGUI(gui.mc.fontRenderer, stack, xPos, yPos, null);
		GlStateManager.disableDepth();
		GlStateManager.disableLighting();
		GlStateManager.popMatrix();
		if (isHovering) {
			drawSprite(gui, type.getButtonHoverOverlay(), x, y);
		}
		elements.forEach(elementBase -> elementBase.draw(gui));
	}

	public SlotType getType() {
		return type;
	}

	public int getId() {
		return id;
	}
}