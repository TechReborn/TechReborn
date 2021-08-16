package reborncore.common.powerSystem;

/**
 * Standard input / output limits for RC-based machines.
 */
public enum RcEnergyTier {
	MICRO(8, 8),
	LOW(32, 32),
	MEDIUM(128, 128),
	HIGH(512, 512),
	EXTREME(2048, 2048),
	INSANE(8192, 8192),
	INFINITE(Integer.MAX_VALUE, Integer.MAX_VALUE);

	private final int maxInput;
	private final int maxOutput;

	RcEnergyTier(int maxInput, int maxOutput) {
		this.maxInput = maxInput;
		this.maxOutput = maxOutput;
	}

	public int getMaxInput() {
		return maxInput;
	}

	public int getMaxOutput() {
			return maxOutput;
		}

	public static RcEnergyTier getTier(long power) {
		for (RcEnergyTier tier : RcEnergyTier.values()) {
			if (tier.getMaxInput() >= power) {
				return tier;
			}
		}
		return RcEnergyTier.INFINITE;
	}
}
