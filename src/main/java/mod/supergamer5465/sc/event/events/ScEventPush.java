package mod.supergamer5465.sc.event.events;

import me.zero.alpine.event.type.Cancellable;
import net.minecraft.entity.Entity;

public class ScEventPush extends Cancellable {
	public Entity entity;
	public double x;
	public double y;
	public double z;
	public boolean airbone;

	public int stage;

	public ScEventPush(Entity entity, double x, double y, double z, boolean airbone) {
		super();
		this.entity = entity;
		this.x = x;
		this.y = y;
		this.z = z;
		this.airbone = airbone;
	}

	public ScEventPush(int stage, Entity entity) {
		this.entity = entity;
		this.stage = stage;
	}

	public ScEventPush(int stage) {
		this.stage = stage;
	}
}