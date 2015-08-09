package techreborn.cofhLib.gui.element.listbox;

import net.minecraft.client.Minecraft;
import techreborn.cofhLib.gui.element.ElementListBox;

public class ListBoxElementText implements IListBoxElement {

    private final String _text;

    public ListBoxElementText(String text) {

        _text = text;
    }

    @Override
    public Object getValue() {

        return _text;
    }

    @Override
    public int getHeight() {

        return 10;
    }

    @Override
    public int getWidth() {

        return Minecraft.getMinecraft().fontRenderer.getStringWidth(_text);
    }

    @Override
    public void draw(ElementListBox listBox, int x, int y, int backColor, int textColor) {

        Minecraft.getMinecraft().fontRenderer.drawString(_text, x, y, textColor);
    }

}
