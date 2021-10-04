package mod.supergamer5465.sc;

import mod.supergamer5465.sc.command.CommandManager;
import mod.supergamer5465.sc.config.Config;
import mod.supergamer5465.sc.event.ScEventBus;
import mod.supergamer5465.sc.event.ScEventHandler;
import mod.supergamer5465.sc.event.ScEventManager;
import mod.supergamer5465.sc.module.ModuleManager;
import mod.supergamer5465.sc.setting.SettingManager;
import mod.supergamer5465.sc.ui.Hud;
import mod.supergamer5465.sc.ui.clickgui.ClickGuiController;
import mod.supergamer5465.sc.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//main class, contains all event handlers etc.

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
@SideOnly(Side.CLIENT)
public class Main {

	Minecraft mc = Minecraft.getMinecraft();

	public static ModuleManager moduleManager;
	public static Config config;
	public static Hud hud = new Hud();
	public static CommandManager cmdManager;
	public static SettingManager settingManager;
	public static ScEventManager eventManager;
	private ClickGuiController gui;

	public static boolean configLoaded = false;

	@Instance
	public Main instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		if (event.getSide() == Side.SERVER)
			return;

		ScEventHandler.INSTANCE = new ScEventHandler();

		MinecraftForge.EVENT_BUS.register(instance);
		MinecraftForge.EVENT_BUS.register(hud);

		moduleManager = new ModuleManager();
		cmdManager = new CommandManager();
		settingManager = new SettingManager();
		eventManager = new ScEventManager();
		config = new Config();
		config.Load();
		configLoaded = true;

		MinecraftForge.EVENT_BUS.register(eventManager);

		ScEventBus.EVENT_BUS.subscribe(eventManager);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}

	public ClickGuiController getGui() {
		return gui;
	}

	public static ScEventHandler get_event_handler() {
		return ScEventHandler.INSTANCE;
	}
}
