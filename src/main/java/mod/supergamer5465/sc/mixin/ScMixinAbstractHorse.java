package mod.supergamer5465.sc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mod.supergamer5465.sc.event.ScEventBus;
import mod.supergamer5465.sc.event.events.ScEventHorseSaddled;
import mod.supergamer5465.sc.event.events.ScEventSteerEntity;
import net.minecraft.entity.passive.AbstractHorse;

@Mixin(AbstractHorse.class)
public class ScMixinAbstractHorse {
	@Inject(method = "canBeSteered", at = @At("HEAD"), cancellable = true)
	public void canBeSteered(CallbackInfoReturnable<Boolean> cir) {
		ScEventSteerEntity l_Event = new ScEventSteerEntity();
		ScEventBus.EVENT_BUS.post(l_Event);

		if (l_Event.isCancelled()) {
			cir.cancel();
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "isHorseSaddled", at = @At("HEAD"), cancellable = true)
	public void isHorseSaddled(CallbackInfoReturnable<Boolean> cir) {
		ScEventHorseSaddled l_Event = new ScEventHorseSaddled();
		ScEventBus.EVENT_BUS.post(l_Event);

		if (l_Event.isCancelled()) {
			cir.cancel();
			cir.setReturnValue(true);
		}
	}
}
