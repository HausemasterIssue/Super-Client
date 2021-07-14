package mod.supergamer5465.sc.event.events;

import mod.supergamer5465.sc.event.ScEventCancellable;
import net.minecraft.client.entity.AbstractClientPlayer;

public class ScEventRenderEntityName extends ScEventCancellable {
	public AbstractClientPlayer Entity;
	public double X;
	public double Y;
	public double Z;
	public String Name;
	public double DistanceSq;

	public ScEventRenderEntityName(AbstractClientPlayer entityIn, double x, double y, double z, String name,
			double distanceSq) {
		super();

		Entity = entityIn;
		x = X;
		y = Y;
		z = Z;
		Name = name;
		DistanceSq = distanceSq;
	}

}