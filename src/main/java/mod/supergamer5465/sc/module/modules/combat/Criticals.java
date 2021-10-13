package mod.supergamer5465.sc.module.modules.combat;

import mod.supergamer5465.sc.event.events.ScUpdateEvent;
import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.FloatSetting;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Criticals extends Module {

	public Criticals() {
		super("Criticals", "Get Critical hits", Category.COMBAT);
	}

	@SubscribeEvent
	public void ScUpdateEvent(ScUpdateEvent event) {
		mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.2, mc.player.posZ, false));
		mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
	}
}