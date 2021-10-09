package mod.supergamer5465.sc.module.modules.player;

import com.mojang.authlib.GameProfile;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;

public class FakePlayer extends Module {
	
	public FakePlayer() {
		super("FakePlayer", "Spawns in a fake player", Category.PLAYER);
	}
	
	public EntityOtherPlayerMP newPlayer;
	
	@Override
	public void onEnable() {
		if(mc.player != null && mc.world != null) {
			newPlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(null, "JakeThaSnake52"));
			newPlayer.copyLocationAndAnglesFrom(mc.player);
			newPlayer.rotationYawHead = mc.player.rotationYawHead;
			mc.world.addEntityToWorld(-100, newPlayer);
		} else {
			this.toggle();
		}
	}
	
	@Override
	public void onDisable() {
		if(mc.player != null && mc.world != null) {
			mc.world.removeEntity(newPlayer);
		}
	}
	
	
}