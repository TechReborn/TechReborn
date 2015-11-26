package techreborn.blocks;


public interface IAdvancedRotationTexture {

    String getFront(boolean isActive);

    String getSide(boolean isActive);

    String getTop(boolean isActive);

    String getBottom(boolean isActive);
}
