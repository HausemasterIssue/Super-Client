package mod.supergamer5465.sc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mod.supergamer5465.sc.event.ScEventBus;
import mod.supergamer5465.sc.event.events.ScEventMotionUpdate;
import mod.supergamer5465.sc.event.events.ScEventMove;
import mod.supergamer5465.sc.event.events.ScEventPush;
import mod.supergamer5465.sc.event.events.ScEventSwing;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;

@Mixin(value = EntityPlayerSP.class)
public class ScMixinEntityPlayerSP extends ScMixinEntity {

	@Inject(method = "move", at = @At("HEAD"), cancellable = true)
	private void move(MoverType type, double x, double y, double z, CallbackInfo info) {

		ScEventMove event = new ScEventMove(type, x, y, z);
		ScEventBus.EVENT_BUS.post(event);

		if (event.isCancelled()) {
			super.move(type, event.get_x(), event.get_y(), event.get_z());
			info.cancel();
		}
	}

	@Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
	public void OnPreUpdateWalkingPlayer(CallbackInfo p_Info) {

		ScEventMotionUpdate l_Event = new ScEventMotionUpdate(0);
		ScEventBus.EVENT_BUS.post(l_Event);
		if (l_Event.isCancelled())
			p_Info.cancel();
	}

	@Redirect(method = {
			"onUpdateWalkingPlayer" }, at = @At(value = "FIELD", target = "net/minecraft/util/math/AxisAlignedBB.minY:D"))
	private double minYHook(AxisAlignedBB bb) {
		return bb.minY;
	}

	@Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"), cancellable = true)
	public void OnPostUpdateWalkingPlayer(CallbackInfo p_Info) {

		ScEventMotionUpdate l_Event = new ScEventMotionUpdate(1);
		ScEventBus.EVENT_BUS.post(l_Event);
		if (l_Event.isCancelled())
			p_Info.cancel();

	}

	@Inject(method = "swingArm", at = @At("RETURN"), cancellable = true)
	public void swingArm(EnumHand p_Hand, CallbackInfo p_Info) {

		ScEventSwing l_Event = new ScEventSwing(p_Hand);
		ScEventBus.EVENT_BUS.post(l_Event);
		if (l_Event.isCancelled())
			p_Info.cancel();

	}

	@Inject(method = { "pushOutOfBlocks" }, at = { @At(value = "HEAD") }, cancellable = true)
	private void pushOutOfBlocksHook(double x, double y, double z, CallbackInfoReturnable<Boolean> info) {
		ScEventPush event = new ScEventPush(1);
		ScEventBus.EVENT_BUS.post(event);
		if (event.isCancelled()) {
			info.setReturnValue(false);
		}
	}
}
