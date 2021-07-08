package mod.supergamer5465.sc.event.events;

import mod.supergamer5465.sc.event.ScEventCancellable;

public class ScEventMotionUpdate extends ScEventCancellable {

	public int stage;

	public ScEventMotionUpdate(int stage) {
		super();
		this.stage = stage;
	}

}