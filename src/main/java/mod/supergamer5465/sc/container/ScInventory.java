package mod.supergamer5465.sc.container;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public class ScInventory {
	Minecraft mc = Minecraft.getMinecraft();

	public void putInOffhand(ItemStack stack) {
		int slot = mc.player.inventory.getSlotFor(stack);
		mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot < 9 ? slot + 36 : slot, 0,
				ClickType.PICKUP, mc.player);
		mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);

		if (!mc.player.inventory.getItemStack().isEmpty()) {
			if (mc.player.inventory.getFirstEmptyStack() != -1) {
				mc.playerController.windowClick(mc.player.inventoryContainer.windowId,
						mc.player.inventory.getFirstEmptyStack(), 0, ClickType.PICKUP, mc.player);
			}
		}
	}
}
