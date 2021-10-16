package mod.supergamer5465.sc.module.modules.combat;

import mod.supergamer5465.sc.event.events.ScEventPacket;
import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.ModeSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.world.World;

public class Criticals extends Module {

	public Criticals() {
		super("Criticals", "Makes normal hits critical hits", Category.COMBAT);
		
		this.addSetting(mode);
	}
	
	ModeSetting mode = new ModeSetting("Mode", this, "NCPStrict", new String[] { "NCPStrict", "Packet", "Jump" });
	
	CPacketUseEntity packet;
	final Minecraft mc = Minecraft.getMinecraft();
	
	@EventHandler
    private final Listener<ScEventPacket.SendPacket> listener = new Listener<>(event -> {
        if (event.get_packet() instanceof CPacketUseEntity) {
            if (((CPacketUseEntity) event.get_packet()).getAction() == CPacketUseEntity.Action.ATTACK && packet.getEntityFromWorld((World)this.mc.world) instanceof EntityLivingBase &&
            		mc.player.onGround && !mc.player.isInLava() && !mc.player.isInWater()) {
            	if(mode.getMode().equalsIgnoreCase("NCPStrict")) {
            		mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.062602401692772D, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.0726023996066094D, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
            	} else if (mode.getMode().equalsIgnoreCase("Packet")) {
            		mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.2, mc.player.posZ, false));
            		mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
            	} else if (mode.getMode().equalsIgnoreCase("Jump")) {
            		mc.player.jump();
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 0.05, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
            	}
            }
        	}
        
    	}
    );
}
