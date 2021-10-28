package mod.imphack.module.modules.movement;

import me.zero.alpine.event.type.Cancellable;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import mod.imphack.event.events.ImpHackEventPacket;
import mod.imphack.event.events.ImpHackEventPush;
import mod.imphack.module.Category;
import mod.imphack.module.Module;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

public class Velocity extends Module {

	public Velocity() {
		super("Velocity", "Stops Knockback", Category.MOVEMENT);
	}

	@EventHandler
	private final Listener<ImpHackEventPacket.ReceivePacket> PacketEvent = new Listener<>(p_Event -> {
		if (p_Event.get_packet() instanceof SPacketEntityVelocity) {
			final SPacketEntityVelocity packet = (SPacketEntityVelocity) p_Event.get_packet();
			if (packet.getEntityID() == mc.player.getEntityId()) {
				p_Event.cancel();
				return;
			}
		}
		if (p_Event.get_packet() instanceof SPacketExplosion) {
			p_Event.cancel();
		}
	});

	@EventHandler
	private final Listener<ImpHackEventPush> PushEvent = new Listener<>(Cancellable::cancel);

}
