package mod.supergamer5465.sc.util;

import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;

public class InventoryUtil {
	public static boolean isItemStackNull(final ItemStack stack) {
		return stack == null || stack.getItem() instanceof ItemAir;
	}
}
