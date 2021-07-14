package mod.supergamer5465.sc.event;

import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listenable;
import me.zero.alpine.listener.Listener;
import mod.supergamer5465.sc.Client;
import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.event.events.ScEventGameOverlay;
import mod.supergamer5465.sc.event.events.ScEventPacket;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.ui.clickgui.ClickGuiController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ScEventManager implements Listenable {
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

	@SubscribeEvent
	public void key(KeyInputEvent k) {
		if (Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null)
			return;
		try {
			if (Keyboard.isCreated()) {
				if (Keyboard.getEventKeyState()) {
					int keyCode = Keyboard.getEventKey();
					if (keyCode <= 0)
						return;
					for (Module m : Main.moduleManager.modules) {
						if (m.toggled)
							m.onKeyPress();
						if (m.getKey() == keyCode && keyCode > 0) {
							m.toggle();
							return;
						}
					}
					if (keyCode == Keyboard.KEY_PERIOD) {
						mc.displayGuiScreen(new GuiChat("."));
						return;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public void tickKeybind(TickEvent event) {
		if (Client.getNextKeyPressForKeybinding) {
			for (int i = 0; i < Keyboard.getKeyCount(); i++) {
				if (Keyboard.isKeyDown(i)) {
					Client.getNextKeyPressForKeybinding = false;
					Client.keybindModule.setKey(i);
					Client.keybindModule = null;
					ClickGuiController.INSTANCE.settingController.refresh(false);
					Main.config.Save();
					return;
				}
			}
		}
	}

	@SubscribeEvent
	public void onChatMessage(ClientChatEvent event) {
		String message = event.getMessage();
		if (message.startsWith(Client.getCommandPrefix())) {
			String command = message.substring(message.indexOf(Client.getCommandPrefix()) + 1);
			Client.addChatMessage("\247c" + message, false);
			Main.cmdManager.callCommand(command);
			event.setCanceled(true);
			Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(message);
		}
	}

	private Field ip;
	private Field port;

	@EventHandler
	private Listener<ScEventPacket.ReceivePacket> PacketEvent = new Listener<>(p_Event -> {
		if (p_Event.get_packet() instanceof C00Handshake) {
			final C00Handshake packet = (C00Handshake) p_Event.get_packet();
			if (packet.getRequestedState() == EnumConnectionState.LOGIN) {

				Class<C00Handshake> c00HandshakeClass = C00Handshake.class;
				try {
					ip = c00HandshakeClass.getDeclaredField("ip");
				} catch (NoSuchFieldException e) {
					throw new RuntimeException(
							"Super Client error: no such field " + e.getMessage() + " in class C00Handshake");
				}
				ip.setAccessible(true);
				try {
					port = c00HandshakeClass.getDeclaredField("port");
				} catch (NoSuchFieldException e) {
					throw new RuntimeException(
							"Super Client error: no such field " + e.getMessage() + " in class C00Handshake");
				}
				port.setAccessible(true);

				try {
					Client.lastConnectedIP = (String) ip.get(packet);

					Client.lastConnectedPort = port.getInt(packet);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	});
}
