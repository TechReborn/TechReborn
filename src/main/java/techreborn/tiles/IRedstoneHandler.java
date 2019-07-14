package techreborn.tiles;

/**
 * @author estebes
 *
 * Interface for tile entities that handle restone
 * TODO: move to API - for now it stays here to not break anything
 */
public interface IRedstoneHandler {
	int getRedstoneLevel();

	int getComparatorValue();
}
