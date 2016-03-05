package techreborn.parts.types;

import techreborn.parts.CableMultipart;
import techreborn.parts.EnumCableType;

/**
 * Created by Mark on 05/03/2016.
 */
public class GlassFiberCable extends CableMultipart {
    @Override
    public EnumCableType getCableType() {
        return EnumCableType.GLASSFIBER;
    }
}
