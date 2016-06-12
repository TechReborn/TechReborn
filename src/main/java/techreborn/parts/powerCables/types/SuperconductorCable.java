package techreborn.parts.powerCables.types;

import techreborn.parts.powerCables.CableMultipart;
import techreborn.parts.powerCables.EnumCableType;

/**
 * Created by modmuss50 on 05/03/2016.
 */
public class SuperconductorCable extends CableMultipart
{
    @Override
    public EnumCableType getCableType()
    {
        return EnumCableType.SUPERCONDUCTOR;
    }
}
