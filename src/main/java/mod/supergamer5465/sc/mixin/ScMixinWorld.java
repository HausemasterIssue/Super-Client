package mod.supergamer5465.sc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import mod.supergamer5465.sc.event.ScEventBus;
import mod.supergamer5465.sc.event.events.ScEventPush;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

@Mixin(World.class)
public class ScMixinWorld {
	@Redirect(method = {
			"handleMaterialAcceleration" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isPushedByWater()Z"))
	public boolean isPushedbyWaterHook(Entity entity) {
		ScEventPush event = new ScEventPush(2, entity);
		ScEventBus.EVENT_BUS.post(event);
		return entity.isPushedByWater() && !event.isCancelled();
	}
}
