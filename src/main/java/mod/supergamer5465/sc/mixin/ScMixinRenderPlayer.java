package mod.supergamer5465.sc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mod.supergamer5465.sc.event.ScEventBus;
import mod.supergamer5465.sc.event.events.ScEventRenderEntityName;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;

@Mixin(RenderPlayer.class)
public class ScMixinRenderPlayer {
	@Inject(method = "renderEntityName", at = @At("HEAD"), cancellable = true)
	public void renderLivingLabel(AbstractClientPlayer entityIn, double x, double y, double z, String name,
			double distanceSq, CallbackInfo info) {
		ScEventRenderEntityName l_Event = new ScEventRenderEntityName(entityIn, x, y, z, name, distanceSq);
		ScEventBus.EVENT_BUS.post(l_Event);
		if (l_Event.isCancelled())
			info.cancel();
	}
}