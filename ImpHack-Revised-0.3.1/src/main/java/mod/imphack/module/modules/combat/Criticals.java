package mod.imphack.module.modules.combat;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import mod.imphack.event.events.ImpHackEventPacket;
import mod.imphack.module.Category;
import mod.imphack.module.Module;
import mod.imphack.setting.settings.ModeSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;

public class Criticals extends Module {

	public Criticals() {
		super("Criticals", "Makes normal hits critical hits", Category.COMBAT);

		this.addSetting(mode);
	}

	final ModeSetting mode = new ModeSetting("Mode", this, "NCPStrict", "NCPStrict", "Packet", "Jump");

	CPacketUseEntity packet;
	final Minecraft mc = Minecraft.getMinecraft();

	@EventHandler
	private final Listener<ImpHackEventPacket.SendPacket> listener = new Listener<>(event -> {
		if (event.get_packet() instanceof CPacketUseEntity) {
			if (mc.player != null
					&& ((CPacketUseEntity) event.get_packet()).getAction() == CPacketUseEntity.Action.ATTACK
					&& packet.getEntityFromWorld(this.mc.world) instanceof EntityLivingBase
					&& mc.player.onGround && !mc.player.isInLava() && !mc.player.isInWater()) {
				if (mode.getMode().equalsIgnoreCase("NCPStrict")) {
					mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,
							mc.player.posY + 0.062602401692772D, mc.player.posZ, false));
					mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,
							mc.player.posY + 0.0726023996066094D, mc.player.posZ, false));
					mc.player.connection.sendPacket(
							new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
				} else if (mode.getMode().equalsIgnoreCase("Packet")) {
					mc.player.connection.sendPacket(
							new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.2, mc.player.posZ, false));
					mc.player.connection.sendPacket(
							new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
				} else if (mode.getMode().equalsIgnoreCase("Jump")) {
					mc.player.jump();
					mc.player.connection.sendPacket(
							new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 0.05, mc.player.posZ, false));
					mc.player.connection.sendPacket(
							new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
				}
			}
		}

	});
}
