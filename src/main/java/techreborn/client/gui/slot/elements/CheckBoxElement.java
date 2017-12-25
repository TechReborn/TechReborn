package techreborn.client.gui.slot.elements;

import reborncore.common.tile.TileLegacyMachineBase;
import techreborn.client.gui.GuiBase;

public class CheckBoxElement extends ElementBase {
	public String label, type;
	public int labelColor, slotID;
	TileLegacyMachineBase machineBase;

	private Sprite.CheckBox checkBoxSprite;

	public CheckBoxElement(String label, int labelColor, int x, int y, String type, int slotID, Sprite.CheckBox checkBoxSprite, TileLegacyMachineBase machineBase) {
		super(x, y, checkBoxSprite.getNormal());
		this.checkBoxSprite = checkBoxSprite;
		this.type = type;
		this.slotID = slotID;
		this.machineBase = machineBase;
		this.label = label;
		this.labelColor = labelColor;
		if (isTicked()) {
			container.setSprite(0, checkBoxSprite.getTicked());
		} else {
			container.setSprite(0, checkBoxSprite.getNormal());
		}
		this.addPressAction((element, gui, provider, mouseX, mouseY) -> {
			if (isTicked()) {
				element.container.setSprite(0, checkBoxSprite.getTicked());
			} else {
				element.container.setSprite(0, checkBoxSprite.getNormal());
			}
			return true;
		});
	}

	@Override
	public void draw(GuiBase gui) {
	//	super.draw(gui);
		ISprite sprite = checkBoxSprite.getNormal();
		if(isTicked()){
			sprite = checkBoxSprite.getTicked();
		}
		drawSprite(gui, sprite, x, y );
		drawString(gui, label, x + checkBoxSprite.getNormal().width + 5, ((y + getHeight(gui.getMachine()) / 2) - (gui.mc.fontRenderer.FONT_HEIGHT / 2)), labelColor);
	}

	public boolean isTicked() {
		if(type.equalsIgnoreCase("output")){
			return machineBase.slotConfiguration.getSlotDetails(slotID).autoOutput();
		}
		return machineBase.slotConfiguration.getSlotDetails(slotID).autoInput();
	}

}
