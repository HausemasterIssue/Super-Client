package mod.imphack.container;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public class ImpHackInventory {
	Minecraft mc = Minecraft.getMinecraft();

	public void putInOffhand(ItemStack stack) {
		int slot = mc.player.inventory.getSlotFor(stack);
		mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot < 9 ? slot + 36 : slot, 0,
				ClickType.PICKUP, mc.player);
		mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
		mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot < 9 ? slot + 36 : slot, 0,
				ClickType.PICKUP, mc.player);

		mc.playerController.updateController();
	}
}
