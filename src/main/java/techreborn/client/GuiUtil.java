package techreborn.client;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

public class GuiUtil {

    public static void drawRepeated(IIcon icon, double x, double y, double width, double height, double z)
    {
        double iconWidthStep = (icon.getMaxU() - icon.getMinU()) / 16.0D;
        double iconHeightStep = (icon.getMaxV() - icon.getMinV()) / 16.0D;

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        for (double cy = y; cy < y + height; cy += 16.0D)
        {
            double quadHeight = Math.min(16.0D, height + y - cy);
            double maxY = cy + quadHeight;
            double maxV = icon.getMinV() + iconHeightStep * quadHeight;
            for (double cx = x; cx < x + width; cx += 16.0D)
            {
                double quadWidth = Math.min(16.0D, width + x - cx);
                double maxX = cx + quadWidth;
                double maxU = icon.getMinU() + iconWidthStep * quadWidth;

                tessellator.addVertexWithUV(cx, maxY, z, icon.getMinU(), maxV);
                tessellator.addVertexWithUV(maxX, maxY, z, maxU, maxV);
                tessellator.addVertexWithUV(maxX, cy, z, maxU, icon.getMinV());
                tessellator.addVertexWithUV(cx, cy, z, icon.getMinU(), icon.getMinV());
            }
        }
        tessellator.draw();
    }
}
