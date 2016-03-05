package techreborn.parts;

import net.minecraft.util.IStringSerializable;

/**
 * Created by Mark on 05/03/2016.
 */
public enum CableTypes implements IStringSerializable {
    COPPER("copper"),
    TIN("tin");

    private String friendlyName;

    CableTypes(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String getName() {
        return friendlyName;
    }
}
