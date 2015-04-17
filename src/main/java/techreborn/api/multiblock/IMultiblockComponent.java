package techreborn.api.multiblock;

public interface IMultiblockComponent {

    /**
     * This gets the instance of the multiblock , allow for different meta data for different structures,
     */
    Class getMultiblockType();
}
