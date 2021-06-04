package mod.supergamer5465.sc.module.modules.utilities;

import java.util.Map;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.BooleanSetting;
import mod.supergamer5465.sc.setting.settings.IntSetting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class AutoFish extends Module {
	boolean isCast = false;
	long recastTimer;

	BooleanSetting findRod = new BooleanSetting("Hotbar Rod Finder", this, true);
	BooleanSetting autoRepair = new BooleanSetting("Offhand Mending Repair (From Hotbar Only)", this, true);
	IntSetting recastDelay = new IntSetting("Recast Delay (ms)", this, 5000);

	public AutoFish() {
		super("AutoFish", "Fishes Automatically", Category.UTILITIES);

		addSetting(findRod);
		addSetting(autoRepair);
		addSetting(recastDelay);
	}

	@Override
	public void onUpdate() {
		boolean reelIn = false;

		if (mc.player.fishEntity != null && isCast) {
			isCast = true;
			double x = mc.player.fishEntity.motionX;
			double y = mc.player.fishEntity.motionY;
			double z = mc.player.fishEntity.motionZ;
			if (y < -0.075 && (mc.player.fishEntity.isInWater()) && x == 0 && z == 0)
				reelIn = true;
		}
		if (reelIn) {
			mc.player.getHeldItemMainhand().useItemRightClick(mc.world, mc.player, EnumHand.MAIN_HAND);
			return;
		}

		boolean cast = false;

		if (mc.player.fishEntity == null) {
			isCast = false;
			cast = true;
			if (recastTimer == 0) {
				recastTimer = System.currentTimeMillis();
				return;
			}
			if (System.currentTimeMillis() - 5000 <= recastTimer) {
				return;
			} else {
				recastTimer = 0;
			}
		}

		if (cast) {
			if (mc.player.getHeldItemMainhand().getItem() instanceof ItemFishingRod) {
				mc.player.getHeldItemMainhand().useItemRightClick(mc.world, mc.player, EnumHand.MAIN_HAND);
				isCast = true;
				return;
			} else {
				if (findRod.enabled) {
					if (!(mc.player.inventory.getCurrentItem().getItem() instanceof ItemFishingRod)) {
						for (int i = 0; i < 9; i++) {
							ItemStack stack = mc.player.inventory.getStackInSlot(i);
							if (stack.getItem() instanceof ItemFishingRod) {
								mc.player.inventory.currentItem = i;
								break;
							}
						}
					}
				}
				return;
			}
		}

		if (autoRepair.enabled) {
			System.out.println("bruh");
			if (!mc.player.getHeldItem(EnumHand.OFF_HAND).isItemDamaged()) {
				for (int i = 0; i < 9; i++) {
					ItemStack stack = mc.player.inventory.getStackInSlot(i);
					if (stack.getItem().isDamaged(stack)) {
						for (Map.Entry<?, ?> me : EnchantmentHelper.getEnchantments(stack).entrySet()) {
							System.out.println(((Enchantment) me.getKey()).getName());
							if (((Enchantment) me.getKey()).getName().equalsIgnoreCase("mending")) {
								mc.player.setHeldItem(EnumHand.OFF_HAND, stack);
								break;
							}
						}
					}
				}
			}
		}
	}
}
