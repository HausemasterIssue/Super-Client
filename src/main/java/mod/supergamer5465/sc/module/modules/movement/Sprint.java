package mod.supergamer5465.sc.module.modules.movement;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.BooleanSetting;
import net.minecraft.client.Minecraft;

public class Sprint extends Module {
	public Sprint() {
		super("Sprint", "Automatically sprints", Category.MOVEMENT);
	}
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	BooleanSetting strict = new BooleanSetting("Strict", this, true);
	BooleanSetting hungerSafe = new BooleanSetting("HungerSafe", this, true);
	
	public void onUpdate() {
		if(mc.world != null) {
			if(mc.gameSettings.keyBindForward.isKeyDown()) {
				if(mc.player.collidedHorizontally && strict.isEnabled() || mc.player.getFoodStats().getFoodLevel() <= 6 && hungerSafe.isEnabled() || mc.player.isSneaking() || mc.player.isHandActive()) {
					mc.player.setSprinting(false);
					return;
				} else {
					mc.player.setSprinting(true);
				}
			} else {
          mc.player.setSprinting(false); 
          return;
      }
			
		}
				
	}

}
