package mod.supergamer5465.sc.module.modules.player;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import mod.supergamer5465.sc.event.events.ScEventPacket;
import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.IntSetting;
import net.minecraft.network.play.client.CPacketCloseWindow;

public class XCarry extends Module {

	public XCarry() {
		super("XCarry", "Keep items in your inventory slots", Category.PLAYER);
	}

	@EventHandler
	private Listener<ScEventPacket.ReceivePacket> PacketRecieveEvent = new Listener<>(event -> {

		if (event.get_packet() instanceof CPacketCloseWindow)
			event.cancel();

	});

	private Listener<ScEventPacket.SendPacket> PacketSendEvent = new Listener<>(event -> {

		if (event.get_packet() instanceof CPacketCloseWindow)
			event.cancel();

	});
}
