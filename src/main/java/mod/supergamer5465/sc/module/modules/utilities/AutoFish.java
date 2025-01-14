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
	long recastTimer = 0;
	long reelTimer = 0;
	long timeSinceCast = 0;

	BooleanSetting findRod = new BooleanSetting("Hotbar Rod Finder", this, true);
	BooleanSetting autoRepair = new BooleanSetting("Offhand Mending Repair", this, true);
	IntSetting recastDelay = new IntSetting("Recast Delay (ms)", this, 5000);
	IntSetting reelDelay = new IntSetting("Reel Delay (ms)", this, 300);

	public AutoFish() {
		super("AutoFish", "Fishes Automatically", Category.UTILITIES);

		addSetting(findRod);
		addSetting(autoRepair);
		addSetting(recastDelay);
		addSetting(reelDelay);
	}

	@Override
	public void onUpdate() {
		if (autoRepair.enabled) {
			if (!mc.player.getHeldItem(EnumHand.OFF_HAND).isItemDamaged()) {
				for (int i = 0; i < 36; i++) {
					ItemStack stack = mc.player.inventory.getStackInSlot(i);
					if (stack.getItem().isDamaged(stack) && mc.player.getHeldItemMainhand() != stack) {
						for (Map.Entry<?, ?> me : EnchantmentHelper.getEnchantments(stack).entrySet()) {
							if (((Enchantment) me.getKey()).getName().equalsIgnoreCase("enchantment.mending")) {
								inv.putInOffhand(stack);
								break;
							}
						}
					}
				}
			}
		}

		boolean reelIn = false;

		if (mc.player.fishEntity != null && mc.player.fishEntity.isEntityAlive()) {
			double x = mc.player.fishEntity.motionX;
			double y = mc.player.fishEntity.motionY;
			double z = mc.player.fishEntity.motionZ;
			if (y < -0.075 && (mc.player.fishEntity.isInWater()) && x == 0 && z == 0) {
				reelTimer = System.currentTimeMillis();
				return;
			}
		}

		if (System.currentTimeMillis() - reelDelay.value <= reelTimer && reelTimer != 0) {
			reelIn = true;
			reelTimer = 0;
		}

		if (reelIn) {
			if (timeSinceCast > 0 && System.currentTimeMillis() - timeSinceCast > 3000) {
				mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
				return;
			}
		}

		boolean cast;

		if (mc.player.fishEntity == null) {
			cast = false;
			if (recastTimer == 0) {
				recastTimer = System.currentTimeMillis();
				return;
			}
			if (System.currentTimeMillis() - recastDelay.value >= recastTimer) {
				cast = true;
				recastTimer = 0;
			}
		} else {
			cast = false;
		}

		if (cast) {
			if (mc.player.getHeldItemMainhand().getItem() instanceof ItemFishingRod) {
				mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
				timeSinceCast = System.currentTimeMillis();
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
			}
		}
	}
}
