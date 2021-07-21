package mod.supergamer5465.sc.module.modules.combat;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class AutoTotem extends Module {

	public AutoTotem() {
		super("AutoTotem", "Places Totems In Offhand", Category.COMBAT);
	}

	@Override
	public void onUpdate() {

		if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory) {

			if (mc.player.getHeldItemOffhand().getItem() == Items.AIR) {
				for (int i = 36; i >= 0; i--) {
					final Item item = mc.player.inventory.getStackInSlot(i).getItem();
					if (item == Items.TOTEM_OF_UNDYING) {
						inv.putInOffhand(mc.player.inventory.getStackInSlot(i));
						return;
					}
				}
				return;
			}

		}

	}

}
