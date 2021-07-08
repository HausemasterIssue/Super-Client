package mod.supergamer5465.sc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import mod.supergamer5465.sc.event.ScEventBus;
import mod.supergamer5465.sc.event.events.ScEventEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;

@Mixin(value = Entity.class)
public class ScMixinEntity {
	// Inject.
	@Redirect(method = "applyEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
	public void velocity(Entity entity, double x, double y, double z) {
		ScEventEntity.ScEventCollision event = new ScEventEntity.ScEventCollision(entity, x, y, z);

		ScEventBus.EVENT_BUS.post(event);

		if (event.isCancelled()) {
			return;
		}

		entity.motionX += x;
		entity.motionY += y;
		entity.motionZ += z;

		entity.isAirBorne = true;
	}

	@Shadow
	public void move(MoverType type, double x, double y, double z) {

	}

	@Shadow
	public double motionX;

	@Shadow
	public double motionY;

	@Shadow
	public double motionZ;

}