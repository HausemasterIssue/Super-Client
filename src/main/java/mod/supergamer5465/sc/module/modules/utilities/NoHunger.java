package mod.supergamer5465.sc.module.modules.utilities;

import static net.minecraft.network.play.client.CPacketEntityAction.Action.START_SPRINTING;
import static net.minecraft.network.play.client.CPacketEntityAction.Action.STOP_SPRINTING;

import java.lang.reflect.Field;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import mod.supergamer5465.sc.event.events.ScEventPacket;
import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.BooleanSetting;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;

public class NoHunger extends Module {

	BooleanSetting cancelSprint = new BooleanSetting("Cancel Sprint Packets", this, true);
	BooleanSetting onGround = new BooleanSetting("On Ground", this, true);

	public NoHunger() {
		super("NoHunger", "Prevents You From Losing Hunger", Category.UTILITIES);

		addSetting(cancelSprint);
		addSetting(onGround);
	}

	private Field cPacketPlayerOnGround;

	@EventHandler
	private Listener<ScEventPacket.ReceivePacket> PacketEvent = new Listener<>(p_Event -> {
		if (p_Event.get_packet() instanceof CPacketPlayer && onGround.enabled && !mc.player.isElytraFlying()) {
			final CPacketPlayer l_Packet = (CPacketPlayer) p_Event.get_packet();

			Class<CPacketPlayer> cPacketPlayerClass = CPacketPlayer.class;
			try {
				cPacketPlayerOnGround = cPacketPlayerClass.getDeclaredField("onGround");
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(
						"Super Client error: no such field " + e.getMessage() + " in class CPacketPlayer");
			}
			cPacketPlayerOnGround.setAccessible(true);

			if (mc.player.fallDistance > 0 || mc.playerController.getIsHittingBlock()) {
				try {
					cPacketPlayerOnGround.setBoolean(l_Packet, true);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new RuntimeException("Super Client error: " + e.getMessage());
				}
			} else {
				try {
					cPacketPlayerOnGround.setBoolean(l_Packet, false);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new RuntimeException("Super Client error: " + e.getMessage());
				}
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
