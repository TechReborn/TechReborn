package techreborn.client.gui.slot.elements;

import techreborn.client.gui.GuiBase;

public class CheckBoxElement extends ElementBase {
	public String label;
	public int labelColor;
	public boolean isTicked;
	private Sprite.CheckBox checkBoxSprite;

	public CheckBoxElement(String label, int labelColor, int x, int y, boolean isTicked, Sprite.CheckBox checkBoxSprite) {
		super(x, y, checkBoxSprite.getNormal());
		this.checkBoxSprite = checkBoxSprite;
		this.isTicked = isTicked;
		this.label = label;
		this.labelColor = labelColor;
		if (isTicked) {
			container.setSprite(0, checkBoxSprite.getTicked());
		} else {
			container.setSprite(0, checkBoxSprite.getNormal());
		}
		this.addPressAction((element, gui, provider, mouseX, mouseY) -> {
			this.isTicked = !this.isTicked;
			if (this.isTicked) {
				element.container.setSprite(0, checkBoxSprite.getTicked());
			} else {
				element.container.setSprite(0, checkBoxSprite.getNormal());
			}
			return true;
		});
	}

	public CheckBoxElement(int x, int y, boolean isTicked, Sprite.CheckBox checkBoxSprite) {
		this("", 0x0, x, y, isTicked, checkBoxSprite);
	}

	@Override
	public void draw(GuiBase gui) {
		super.draw(gui);
		drawString(gui, label, x + checkBoxSprite.getNormal().width + 5, ((y + getHeight(gui.getMachine()) / 2) - (gui.mc.fontRenderer.FONT_HEIGHT / 2)), labelColor);
	}

	public boolean isTicked() {
		return isTicked;
	}

	public void setTicked(boolean ticked) {
		isTicked = ticked;
		if (isTicked) {
			container.setSprite(0, checkBoxSprite.getTicked());
		} else {
			container.setSprite(0, checkBoxSprite.getNormal());
		}
	}
}
