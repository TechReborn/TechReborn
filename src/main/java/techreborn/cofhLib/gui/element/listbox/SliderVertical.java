package techreborn.cofhLib.gui.element.listbox;

public class SliderVertical extends techreborn.cofhLib.gui.element.ElementSlider
{

    public SliderVertical(techreborn.cofhLib.gui.GuiBase containerScreen, int x, int y, int width, int height, int maxValue)
    {

        this(containerScreen, x, y, width, height, maxValue, 0);
    }

    public SliderVertical(techreborn.cofhLib.gui.GuiBase containerScreen, int x, int y, int width, int height, int maxValue, int minValue)
    {

        super(containerScreen, x, y, width, height, maxValue, minValue);
        int dist = maxValue - minValue;
        setSliderSize(width, dist <= 0 ? height : Math.max(height / ++dist, 9));
    }

    @Override
    public int getSliderY()
    {

        int dist = _valueMax - _valueMin;
        int maxPos = sizeY - _sliderHeight;
        return Math.min(dist == 0 ? 0 : maxPos * (_value - _valueMin) / dist, maxPos);
    }

    @Override
    public void dragSlider(int x, int v)
    {

        v += Math.round(_sliderHeight * (v / (float) sizeY) + (_sliderHeight * 0.25f));
        setValue(_valueMin + ((_valueMax - _valueMin) * v / sizeY));
    }
}
