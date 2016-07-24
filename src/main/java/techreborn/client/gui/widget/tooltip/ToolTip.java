package techreborn.client.gui.widget.tooltip;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import reborncore.client.gui.GuiUtil;

import java.util.ArrayList;
import java.util.Collections;

public class ToolTip {

    protected ArrayList<ToolTipLine> lines = new ArrayList<>();

    public ToolTip(String... textLines) {
        for(String text : textLines)
            lines.add(new ToolTipLine(text));
    }

    public ToolTip(ToolTipLine... toolTipLines) {
        Collections.addAll(lines, toolTipLines);
    }

    public ToolTip(int linesSize) {
        for(int i = 0; i < linesSize; i++)
            lines.add(new ToolTipLine());
    }

    public void addLine(ToolTipLine toolTipLine) {
        lines.add(toolTipLine);
    }

    public void removeLine(int index) {
        lines.remove(index);
    }

    public ToolTipLine getLine(int index) {
        return lines.get(index);
    }

    public ArrayList<ToolTipLine> getLines() {
        return lines;
    }

    protected void refresh() {}

    public void draw(FontRenderer font, int mouseX, int mouseY) {
        refresh();
        int maxLineLength = 0;
        int textX = mouseX + 3;
        int textY = mouseY + 3;
        for(ToolTipLine toolTipLine : lines) {
            toolTipLine.draw(font, textX, textY);
            textY += (font.FONT_HEIGHT + 3);
            int lineWidth = toolTipLine.getWidth(font);
            if(lineWidth > maxLineLength)
                maxLineLength = lineWidth;
        }
        GuiUtil.drawTooltipBox(mouseX, mouseY, maxLineLength, textY + 3);
    }

}
