package mod.supergamer5465.sc.module.modules.movement;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import mod.supergamer5465.sc.event.events.ScEventPacket;
import mod.supergamer5465.sc.event.events.ScEventPush;
import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

public class Velocity extends Module {

	public Velocity() {
		super("Velocity", "Stops Knockback", Category.MOVEMENT);
	}

	@EventHandler
	private Listener<ScEventPacket.ReceivePacket> PacketEvent = new Listener<>(p_Event -> {
		if (p_Event.get_packet() instanceof SPacketEntityVelocity) {
			final SPacketEntityVelocity packet = (SPacketEntityVelocity) p_Event.get_packet();
			if (packet.getEntityID() == mc.player.getEntityId()) {
				p_Event.cancel();
				return;
			}
		}
		if (p_Event.get_packet() instanceof SPacketExplosion) {
			p_Event.cancel();
			return;
		}
	});

	@EventHandler
	private Listener<ScEventPush> PushEvent = new Listener<>(p_Event -> {
		p_Event.cancel();
	});

}
