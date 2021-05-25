package mod.supergamer5465.sc.event.events;

import mod.supergamer5465.sc.event.ScEventCancellable;
import net.minecraft.client.gui.ScaledResolution;

public class ScEventGameOverlay extends ScEventCancellable {

	public float partial_ticks;
	private ScaledResolution scaled_resolution;

	public ScEventGameOverlay(float partial_ticks, ScaledResolution scaled_resolution) {

		this.partial_ticks = partial_ticks;
		this.scaled_resolution = scaled_resolution;

	}

	public ScaledResolution get_scaled_resoltion() {
		return scaled_resolution;
	}

}