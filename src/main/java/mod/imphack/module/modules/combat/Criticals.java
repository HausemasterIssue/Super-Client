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

	final ModeSetting mode = new ModeSetting("Mode", this, "NCPStrict", "Packet", "NCPStrict", "Jump");

	CPacketUseEntity packet;
	final Minecraft mc = Minecraft.getMinecraft();

	@EventHandler
	private final Listener<ImpHackEventPacket.SendPacket> listener = new Listener<>(event -> {
		if (event.get_packet() instanceof CPacketUseEntity) {
            CPacketUseEntity packet = (CPacketUseEntity)event.get_packet();
            
            if (packet.getAction() == CPacketUseEntity.Action.ATTACK) {
                if (packet.getEntityFromWorld(mc.world) instanceof EntityLivingBase && mc.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown()) {
                	
                	if(mode.is("Jump")) {
                		mc.player.jump();
                	}
                	
                	if(mode.is("Packet")) {
                        	mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1f, mc.player.posZ, false));
                        	mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                	}
			
			if(mode.is("NCPStrict")) {
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.062602401692772D, mc.player.posZ, false));
                    		mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.0726023996066094D, mc.player.posZ, false));
                    		mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
			}
               
                }
            }
		}
	});
}
