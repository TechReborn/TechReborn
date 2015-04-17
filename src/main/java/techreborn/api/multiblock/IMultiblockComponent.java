package techreborn.api.multiblock;

public interface IMultiblockComponent {

    /**
     * This gets the multiblock type, allow for different meta data for different structures,
     */
    IMultiBlock getMultiblock(int meta);
}
