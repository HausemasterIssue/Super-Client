package mod.supergamer5465.sc.event.events;

import me.zero.alpine.event.type.Cancellable;

public class ScEventMotionUpdate extends Cancellable {

	public int stage;

	public ScEventMotionUpdate(int stage) {
		super();
		this.stage = stage;
	}
}
