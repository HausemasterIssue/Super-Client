package mod.supergamer5465.sc.event;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.event.events.ScEventGameOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ScEventManager {
	private final Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void onUpdate(LivingEvent.LivingUpdateEvent event) {
		if (event.isCanceled()) {
			return;
		}
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (mc.player == null) {
			return;
		}

		Main.moduleManager.update();
	}

	@SubscribeEvent
	public void onWorldRender(RenderWorldLastEvent event) {
		if (event.isCanceled()) {
			return;
		}
		if (mc.gameSettings.viewBobbing) {
			mc.gameSettings.viewBobbing = false;
			System.out.println("disabled view bobbing (because it fucking sucks)");
			// view bobbing is gay
		}
		Main.moduleManager.render(event);
	}

	@SubscribeEvent
	public void onRender(RenderGameOverlayEvent.Post event) {

		if (event.isCanceled()) {
			return;
		}

		ScEventBus.EVENT_BUS.post(new ScEventGameOverlay(event.getPartialTicks(), new ScaledResolution(mc)));

		RenderGameOverlayEvent.ElementType target = RenderGameOverlayEvent.ElementType.EXPERIENCE;

		if (!mc.player.isCreative() && mc.player.getRidingEntity() instanceof AbstractHorse) {
			target = RenderGameOverlayEvent.ElementType.HEALTHMOUNT;
		}

		if (event.getType() == target) {
		}
	}

	@SubscribeEvent
	public void onInputUpdate(InputUpdateEvent event) {
		ScEventBus.EVENT_BUS.post(event);
	}
}
