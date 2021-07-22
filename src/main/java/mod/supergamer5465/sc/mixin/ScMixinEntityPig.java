package mod.supergamer5465.sc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mod.supergamer5465.sc.event.ScEventBus;
import mod.supergamer5465.sc.event.events.ScEventHorseSaddled;
import mod.supergamer5465.sc.event.events.ScEventSteerEntity;
import net.minecraft.entity.passive.EntityPig;

@Mixin(EntityPig.class)
public class ScMixinEntityPig {
	@Inject(method = "canBeSteered", at = @At("HEAD"), cancellable = true)
	public void canBeSteered(CallbackInfoReturnable<Boolean> cir) {
		ScEventSteerEntity event = new ScEventSteerEntity();
		ScEventBus.EVENT_BUS.post(event);

		if (event.isCancelled()) {
			cir.cancel();
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "getSaddled", at = @At("HEAD"), cancellable = true)
	public void getSaddled(CallbackInfoReturnable<Boolean> cir) {
		ScEventHorseSaddled event = new ScEventHorseSaddled();
		ScEventBus.EVENT_BUS.post(event);

		if (event.isCancelled()) {
			cir.cancel();
			cir.setReturnValue(true);
		}
	}
}