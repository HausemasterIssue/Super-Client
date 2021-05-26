package mod.supergamer5465.sc.event.events;

import me.zero.alpine.event.type.Cancellable;

public class ScEventPlayerTravel extends Cancellable {
	public float Strafe;
	public float Vertical;
	public float Forward;

	public ScEventPlayerTravel(float p_Strafe, float p_Vertical, float p_Forward) {
		Strafe = p_Strafe;
		Vertical = p_Vertical;
		Forward = p_Forward;
	}
}
