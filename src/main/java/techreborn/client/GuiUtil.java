package techreborn.client;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

public class GuiUtil {


    public static void drawRepeated(IIcon icon, double x, double y, double width, double height, double z) {
        double iconWidthStep = (icon.getMaxU() - icon.getMinU()) / 16.0D;
        double iconHeightStep = (icon.getMaxV() - icon.getMinV()) / 16.0D;

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        for (double cy = y; cy < y + height; cy += 16.0D) {
            double quadHeight = Math.min(16.0D, height + y - cy);
            double maxY = cy + quadHeight;
            double maxV = icon.getMinV() + iconHeightStep * quadHeight;
            for (double cx = x; cx < x + width; cx += 16.0D) {
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

    public static void drawTooltipBox(int x, int y, int w, int h) {
        int bg = 0xf0100010;
        drawGradientRect(x + 1, y, w - 1, 1, bg, bg);
        drawGradientRect(x + 1, y + h, w - 1, 1, bg, bg);
        drawGradientRect(x + 1, y + 1, w - 1, h - 1, bg, bg);//center
        drawGradientRect(x, y + 1, 1, h - 1, bg, bg);
        drawGradientRect(x + w, y + 1, 1, h - 1, bg, bg);
        int grad1 = 0x505000ff;
        int grad2 = 0x5028007F;
        drawGradientRect(x + 1, y + 2, 1, h - 3, grad1, grad2);
        drawGradientRect(x + w - 1, y + 2, 1, h - 3, grad1, grad2);

        drawGradientRect(x + 1, y + 1, w - 1, 1, grad1, grad1);
        drawGradientRect(x + 1, y + h - 1, w - 1, 1, grad2, grad2);
    }

    public static void drawGradientRect(int x, int y, int w, int h, int colour1, int colour2) {
        new GuiHook().drawGradientRect(x, y, x + w, y + h, colour1, colour2);
    }

    public static class GuiHook extends Gui {
        @Override
        public void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6) {
            super.drawGradientRect(par1, par2, par3, par4, par5, par6);
        }
    }
}
