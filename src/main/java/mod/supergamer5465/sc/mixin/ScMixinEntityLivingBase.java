package mod.supergamer5465.sc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mod.supergamer5465.sc.event.ScEventBus;
import mod.supergamer5465.sc.event.events.ScEventTotemPop;
import net.minecraft.entity.EntityLivingBase;

@Mixin(EntityLivingBase.class)
public class ScMixinEntityLivingBase {
	@Inject(method = "checkTotemDeathProtection", at = @At(value = "RETURN", ordinal = 1))
	public void onTotemPop(CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			ScEventBus.EVENT_BUS.post(new ScEventTotemPop());
		}
	}
}
