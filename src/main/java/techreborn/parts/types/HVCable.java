package techreborn.parts.types;

import techreborn.parts.CableMultipart;
import techreborn.parts.EnumCableType;

/**
 * Created by Mark on 05/03/2016.
 */
public class HVCable extends CableMultipart {
    @Override
    public EnumCableType getCableType() {
        return EnumCableType.HV;
    }
}
