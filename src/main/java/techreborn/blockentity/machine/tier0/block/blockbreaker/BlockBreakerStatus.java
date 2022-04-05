package techreborn.blockentity.machine.tier0.block.blockbreaker;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import reborncore.common.util.Color;
import techreborn.blockentity.machine.tier0.block.ProcessingStatus;

/**
 * An enumeration of different statuses the Block Breaker can have
 *
 * Includes {@code TranslatableText} describing the status along with a text color
 *
 * @author SimonFlapse
 */
enum BlockBreakerStatus implements ProcessingStatus {
	IDLE(new TranslatableText("gui.techreborn.block_breaker.idle"), Color.BLUE),
	IDLE_PAUSED(new TranslatableText("gui.techreborn.block.idle_redstone"), Color.BLUE),
	OUTPUT_FULL(new TranslatableText("gui.techreborn.block.output_full"), Color.RED),
	NO_ENERGY(new TranslatableText("gui.techreborn.block.no_energy"), Color.RED),
	INTERRUPTED(new TranslatableText("gui.techreborn.block.interrupted"), Color.RED),
	OUTPUT_BLOCKED(new TranslatableText("gui.techreborn.block.output_blocked"), Color.RED),
	PROCESSING(new TranslatableText("gui.techreborn.block_breaker.processing"), Color.DARK_GREEN);

	private final Text text;
	private final int color;

	BlockBreakerStatus(Text text, Color color) {
		this.text = text;
		this.color = color.getColor();
	}

	@Override
	public Text getText() {
		return text;
	}

	@Override
	public Text getProgressText(int progress) {
		progress = Math.max(progress, 0);

		if (this == PROCESSING) {
			return new TranslatableText("gui.techreborn.block.progress.active", new LiteralText("" + progress + "%"));
		} else if (this == IDLE || this == INTERRUPTED) {
			return new TranslatableText("gui.techreborn.block.progress.stopped");
		}

		return new TranslatableText("gui.techreborn.block.progress.paused", new LiteralText("" + progress + "%"));
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public int getStatusCode() {
		return this.ordinal();
	}
}
