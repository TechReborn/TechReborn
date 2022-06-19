package techreborn.blockentity.machine.multiblock.FusionControlComputer;

public enum FusionControlComputerState {
	INACTIVE(-1),
	NO_RECIPE(0),
	CHARGING(1),
	CRAFTING(2),
	PAUSED(3);

	private final int value;

	FusionControlComputerState(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	public static FusionControlComputerState fromValue(int value) {
		for (FusionControlComputerState state : FusionControlComputerState.values())
			if (state.getValue() == value)
				return state;

		return INACTIVE;
	}
}
