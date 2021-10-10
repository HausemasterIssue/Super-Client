package mod.supergamer5465.sc.module.modules.combat;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.IntSetting;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class AutoTotem extends Module {

	IntSetting delay = new IntSetting("Delay (ms)", this, 100);

	long timer = System.currentTimeMillis();

	public AutoTotem() {
		super("AutoTotem", "Places Totems In Offhand", Category.COMBAT);
		addSetting(delay);
	}

	@Override
	public void onUpdate() {
		if (timer < System.currentTimeMillis() - delay.getValue()) {
			if (mc.player.getHeldItemOffhand().getItem() == Items.AIR) {
				for (int i = 36; i >= 0; i--) {
					final Item item = mc.player.inventory.getStackInSlot(i).getItem();
					if (item == Items.TOTEM_OF_UNDYING) {
						inv.putInOffhand(mc.player.inventory.getStackInSlot(i));
						continue;
					}
				}
				timer = System.currentTimeMillis();
			}
		}
	}

}
