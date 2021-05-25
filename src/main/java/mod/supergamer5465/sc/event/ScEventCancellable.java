package mod.supergamer5465.sc.event;

import me.zero.alpine.event.type.Cancellable;
import net.minecraft.client.Minecraft;

public class ScEventCancellable extends Cancellable {
	private final Era era_switch = Era.EVENT_PRE;
	private final float partial_ticks;

	public ScEventCancellable() {
		partial_ticks = Minecraft.getMinecraft().getRenderPartialTicks();
	}

	public Era get_era() {
		return era_switch;
	}

	public float get_partial_ticks() {
		return partial_ticks;
	}

	public enum Era {
		EVENT_PRE, EVENT_PERI, EVENT_POST;
	}
}
