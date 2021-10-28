package mod.imphack;

import mod.imphack.command.CommandManager;
import mod.imphack.config.Config;
import mod.imphack.event.ImpHackEventBus;
import mod.imphack.event.ImpHackEventHandler;
import mod.imphack.event.ImpHackEventManager;
import mod.imphack.module.ModuleManager;
import mod.imphack.module.modules.hud.HudArrayList;
import mod.imphack.module.modules.hud.HudCoords;
import mod.imphack.setting.SettingManager;
import mod.imphack.ui.Hud;
import mod.imphack.ui.clickgui.ClickGuiController;
import mod.imphack.util.Reference;
import mod.imphack.util.font.ImpHackFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//main class, contains all event handlers etc.

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
@SideOnly(Side.CLIENT)
public class Main {

	public static long startTimeStamp = 0;
	public static ImpHackFontRenderer customFontRenderer;

	Minecraft mc = Minecraft.getMinecraft();

	public static ModuleManager moduleManager;
	public static Config config;
	
	//Hud loader
	public static final Hud hud = new Hud();
	public static final HudArrayList hudArrayList = new HudArrayList();
	public static final HudCoords hudCoords = new HudCoords();
	
	
	
	public static CommandManager cmdManager;
	public static SettingManager settingManager;
	public static ImpHackEventManager eventManager;
	private ClickGuiController gui;

	public static boolean configLoaded = false;

	@Instance
	public Main instance;

	@EventHandler
	public void init(FMLInitializationEvent event) {
		if (event.getSide() == Side.SERVER)
			return;

		ImpHackEventHandler.INSTANCE = new ImpHackEventHandler();

		MinecraftForge.EVENT_BUS.register(instance);
		MinecraftForge.EVENT_BUS.register(hud);
		MinecraftForge.EVENT_BUS.register(hudArrayList);
		MinecraftForge.EVENT_BUS.register(hudCoords);

		

		moduleManager = new ModuleManager();
		cmdManager = new CommandManager();
		settingManager = new SettingManager();
		eventManager = new ImpHackEventManager();
		config = new Config();
		config.Load();
		configLoaded = true;

		MinecraftForge.EVENT_BUS.register(eventManager);

		ImpHackEventBus.EVENT_BUS.subscribe(eventManager);

		startTimeStamp = System.currentTimeMillis();
	}

	public ClickGuiController getGui() {
		return gui;
	}

	public static ImpHackEventHandler get_event_handler() {
		return ImpHackEventHandler.INSTANCE;
	}
}
