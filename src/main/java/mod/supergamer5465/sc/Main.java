package mod.supergamer5465.sc;

import org.lwjgl.input.Keyboard;

import mod.supergamer5465.sc.command.CommandManager;
import mod.supergamer5465.sc.config.Config;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.module.ModuleManager;
import mod.supergamer5465.sc.proxy.CommonProxy;
import mod.supergamer5465.sc.setting.SettingManager;
import mod.supergamer5465.sc.ui.Hud;
import mod.supergamer5465.sc.ui.clickgui.ClickGuiController;
import mod.supergamer5465.sc.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//main class, contains all event handlers etc

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
@SideOnly(Side.CLIENT)
public class Main {

	Minecraft mc = Minecraft.getMinecraft();

	public static ModuleManager moduleManager;
	public static Config config;
	public static Hud hud = new Hud();
	public static CommandManager cmdManager;
	public static SettingManager settingManager;
	private ClickGuiController gui;

	public static boolean configLoaded = false;

	@Instance
	public Main instance;

	@SidedProxy(clientSide = Reference.CLIENTPROXYCLASS, serverSide = Reference.COMMONPROXYCLASS)
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		if (event.getSide() == Side.SERVER)
			return;

		MinecraftForge.EVENT_BUS.register(instance);
		MinecraftForge.EVENT_BUS.register(hud);
		moduleManager = new ModuleManager();
		cmdManager = new CommandManager();
		settingManager = new SettingManager();
		config = new Config();
		config.Load();
		config.Save();
		configLoaded = true;
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
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
					for (Module m : moduleManager.modules) {
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
			cmdManager.callCommand(command);
			event.setCanceled(true);
			Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(message);
		}
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (mc.player == null) {
			return;
		}

		moduleManager.update();
	}

	public ClickGuiController getGui() {
		return gui;
	}
}
