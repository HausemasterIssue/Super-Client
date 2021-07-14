package mod.supergamer5465.sc.module.modules.player;

import java.lang.reflect.Field;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.IntSetting;
import mod.supergamer5465.sc.util.PlayerUtil;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class AutoEat extends Module {

	IntSetting hunger = new IntSetting("Hunger", this, 17);

	public AutoEat() {
		super("AutoEat", "Eats Automatically", Category.PLAYER);

		addSetting(hunger);
	}

	private boolean m_WasEating = false;

	private static Field keyBindUseItemPressed;
	Class<KeyBinding> keyBindingClass = KeyBinding.class;

	@Override
	public void onDisable() {
		super.onDisable();

		if (m_WasEating) {
			m_WasEating = false;

			try {
				keyBindUseItemPressed = keyBindingClass.getDeclaredField("pressed");
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(
						"Super Client error: no such field " + e.getMessage() + " in class KeyBinding");
			}

			keyBindUseItemPressed.setAccessible(true);

			try {
				keyBindUseItemPressed.setBoolean(mc.gameSettings.keyBindUseItem, false);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onUpdate() {
		if (hunger.value > 20)
			hunger.value = 0;

		if (!PlayerUtil.IsEating() && hunger.value >= mc.player.getFoodStats().getFoodLevel()) {
			boolean l_CanEat = false;

			for (int l_I = 0; l_I < 9; ++l_I) {
				ItemStack l_Stack = mc.player.inventory.getStackInSlot(l_I);

				if (mc.player.inventory.getStackInSlot(l_I).isEmpty())
					continue;

				if (l_Stack.getItem() instanceof ItemFood) {
					l_CanEat = true;
					mc.player.inventory.currentItem = l_I;
					mc.playerController.updateController();
					break;
				}
			}

			if (l_CanEat) {
				if (mc.currentScreen == null) {
					try {
						keyBindUseItemPressed = keyBindingClass.getDeclaredField("pressed");
					} catch (NoSuchFieldException e) {
						throw new RuntimeException(
								"Super Client error: no such field " + e.getMessage() + " in class KeyBinding");
					}

					keyBindUseItemPressed.setAccessible(true);

					try {
						keyBindUseItemPressed.setBoolean(mc.gameSettings.keyBindUseItem, true);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				} else
					mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);

				m_WasEating = true;
			}
		}

		if (m_WasEating) {
			m_WasEating = false;

			try {
				keyBindUseItemPressed = keyBindingClass.getDeclaredField("pressed");
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(
						"Super Client error: no such field " + e.getMessage() + " in class KeyBinding");
			}

			keyBindUseItemPressed.setAccessible(true);

			try {
				keyBindUseItemPressed.setBoolean(mc.gameSettings.keyBindUseItem, false);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}
