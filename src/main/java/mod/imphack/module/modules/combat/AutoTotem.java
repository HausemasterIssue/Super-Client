package mod.imphack.module.modules.combat;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import mod.imphack.Client;
import mod.imphack.event.events.ImpHackEventTotemPop;
import mod.imphack.module.Category;
import mod.imphack.module.Module;
import mod.imphack.setting.settings.BooleanSetting;
import mod.imphack.setting.settings.IntSetting;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.inventory.ClickType;

public class AutoTotem extends Module {

	IntSetting delay = new IntSetting("Delay (ms)", this, 100);
	BooleanSetting chat = new BooleanSetting("Chat Totem Pop", this, true);

	long timer = System.currentTimeMillis();

	public AutoTotem() {
		super("AutoTotem", "Places Totems In Offhand", Category.COMBAT);
		addSetting(delay);
		addSetting(chat);
	}

	@Override
	public void onUpdate() {
		if (timer < System.currentTimeMillis() - delay.getValue()) {
			if (mc.player.getHeldItemOffhand().getItem() == Items.AIR) {
				for (int i = 36; i >= 0; i--) {
					final Item item = mc.player.inventory.getStackInSlot(i).getItem();
					if (item == Items.TOTEM_OF_UNDYING) {
						mc.playerController.windowClick(0, i, 1, ClickType.PICKUP, mc.player);
                                		mc.playerController.windowClick(0, 45, 1, ClickType.PICKUP, mc.player);
						continue;
					}
				}
				timer = System.currentTimeMillis();
			}
		}
	}

	int totemCount = 0;

	@EventHandler
	private Listener<ImpHackEventTotemPop> totemPopEvent = new Listener<>(p_Event -> {
		totemCount = 0;
		if (chat.enabled) {
			for (int i = 36; i >= 0; i--) {
				final Item item = mc.player.inventory.getStackInSlot(i).getItem();
				if (item == Items.TOTEM_OF_UNDYING) {
					totemCount++;
				}
			}

			if (System.currentTimeMillis() - timer <= 200)
				return;

			timer = System.currentTimeMillis();

			Client.addChatMessage("Totem Popped, " + totemCount + " totems left");
		}
	});

}
