package mod.supergamer5465.sc.event.events;

import mod.supergamer5465.sc.event.ScEventCancellable;
import net.minecraft.util.EnumHand;

public class ScEventSwing extends ScEventCancellable {

	public EnumHand hand;

	public ScEventSwing(EnumHand hand) {
		super();
		this.hand = hand;
	}

}