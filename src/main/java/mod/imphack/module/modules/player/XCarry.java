package mod.imphack.module.modules.player;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import mod.imphack.event.events.ImpHackEventPacket;
import mod.imphack.module.Category;
import mod.imphack.module.Module;
import net.minecraft.network.play.client.CPacketCloseWindow;

public class XCarry extends Module {

	public XCarry() {
		super("XCarry", "Keep items in your inventory slots", Category.PLAYER);
	}

	@EventHandler
	private Listener<ImpHackEventPacket.ReceivePacket> PacketRecieveEvent = new Listener<>(event -> {

		if (event.get_packet() instanceof CPacketCloseWindow)
			event.cancel();

	});

	@EventHandler
	private Listener<ImpHackEventPacket.SendPacket> PacketSendEvent = new Listener<>(event -> {

		if (event.get_packet() instanceof CPacketCloseWindow)
			event.cancel();

	});
}