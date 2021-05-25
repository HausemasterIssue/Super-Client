package mod.supergamer5465.sc.event.events;

import mod.supergamer5465.sc.event.ScEventCancellable;

public class ScEventJump extends ScEventCancellable {

	public double motion_x;
	public double motion_y;

	public ScEventJump(double motion_x, double motion_y) {
		super();

		this.motion_x = motion_x;
		this.motion_y = motion_y;
	}

}