package mod.supergamer5465.sc.module.modules.combat;

import mod.supergamer5465.sc.event.events.ScUpdateEvent;
import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.BooleanSetting;
import mod.supergamer5465.sc.setting.settings.FloatSetting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Anchor extends Module {

	FloatSetting downSpeed = new FloatSetting("DownSpeed", this, 0.15f);
	FloatSetting fallHeight = new FloatSetting("FallHeight", this, 2.0f);
	BooleanSetting stopMovement = new BooleanSetting("StopMovement", this, true);

	public Anchor() {
		super("Anchor", "Get in to holes quicker", Category.COMBAT);

		this.addSetting(downSpeed);
		this.addSetting(fallHeight);
		this.addSetting(stopMovement);
	}

	@SubscribeEvent
	public void ScUpdateEvent(ScUpdateEvent event) {
		if (event.getPlayer().fallDistance > fallHeight.getValue()) {
			mc.player.motionY = downSpeed.getValue();
		}

		if (stopMovement.isEnabled() && event.getPlayer().fallDistance > fallHeight.getValue())
			mc.player.setSprinting(false);
	}
}