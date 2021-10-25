package mod.imphack.module.modules.utilities;

import static net.minecraft.network.play.client.CPacketEntityAction.Action.START_SPRINTING;
import static net.minecraft.network.play.client.CPacketEntityAction.Action.STOP_SPRINTING;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import mod.imphack.event.events.ImpHackEventPacket;
import mod.imphack.module.Category;
import mod.imphack.module.Module;
import mod.imphack.setting.settings.BooleanSetting;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;

public class NoHunger extends Module {

	BooleanSetting cancelSprint = new BooleanSetting("Cancel Sprint Packets", this, true);
	BooleanSetting onGround = new BooleanSetting("On Ground", this, true);

	public NoHunger() {
		super("NoHunger", "Prevents You From Losing Hunger", Category.PLAYER);

		addSetting(cancelSprint);
		addSetting(onGround);
	}

	@EventHandler
	private Listener<ImpHackEventPacket.ReceivePacket> PacketEvent = new Listener<>(p_Event -> {
		if (p_Event.get_packet() instanceof CPacketPlayer && onGround.enabled && !mc.player.isElytraFlying()) {
			final CPacketPlayer l_Packet = (CPacketPlayer) p_Event.get_packet();

			if (mc.player.fallDistance > 0 || mc.playerController.getIsHittingBlock()) {
				l_Packet.onGround = true;
			} else {
				l_Packet.onGround = false;
			}
		}

		if (p_Event.get_packet() instanceof CPacketEntityAction && cancelSprint.enabled) {
			final CPacketEntityAction l_Packet = (CPacketEntityAction) p_Event.get_packet();
			if (l_Packet.getAction() == START_SPRINTING || l_Packet.getAction() == STOP_SPRINTING) {
				p_Event.cancel();
			}
		}
	});
}
